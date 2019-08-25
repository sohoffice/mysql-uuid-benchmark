package com.sohoffice.mysqluuidbenchmark.controller;

import com.sohoffice.mysqluuidbenchmark.Constants;
import com.sohoffice.mysqluuidbenchmark.UuidCache;
import com.sohoffice.mysqluuidbenchmark.UuidTools;
import com.sohoffice.mysqluuidbenchmark.entity.NumberUuid;
import com.sohoffice.mysqluuidbenchmark.repository.BinaryUuidRepository;
import com.sohoffice.mysqluuidbenchmark.repository.NumberUuidRepository;
import com.sohoffice.mysqluuidbenchmark.repository.StringUuidRepository;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json")
public class BenchmarkController {

  @Autowired
  private Constants constants;

  @Autowired
  private BinaryUuidRepository binaryUuidRepository;

  @Autowired
  private NumberUuidRepository numberUuidRepository;

  @Autowired
  private StringUuidRepository stringUuidRepository;

  @Autowired
  private UuidCache uuidCache;

  @RequestMapping(value = "/benchmark/str", method = RequestMethod.POST)
  public LongSummaryStatistics benchmarkString() {
    return benchmark(bb -> UuidTools.fromByteBuffer(bb).toString(),
        stringUuidRepository::getByUuid);
  }

  @RequestMapping(value = "/benchmark/num", method = RequestMethod.POST)
  public LongSummaryStatistics benchmarkNumber() {
    Stream<UuidEntry> uuidEntryStream = prepareRandomUuidStream();
    return getNumberBenchmark(uuidEntryStream);
  }

  @RequestMapping(value = "/benchmark/bin", method = RequestMethod.POST)
  public LongSummaryStatistics benchmarkBinary() {
    return benchmark(UuidTools::fromByteBuffer, binaryUuidRepository::getByUuid);
  }

  @RequestMapping(value = "/benchmark/all", method = RequestMethod.POST)
  public Map<String, LongSummaryStatistics> benchmarkAll() {
    List<UuidEntry> uuidByteBuffers = prepareRandomUuidStream().collect(Collectors.toList());

    Map<String, LongSummaryStatistics> map = new HashMap<>();
    // string
    map.put("string", doBenchmark(uuidByteBuffers.stream(),
        bb -> UuidTools.fromByteBuffer(bb).toString(),
        stringUuidRepository::getByUuid));
    // number
    // map.put("number", getNumberBenchmark(uuidByteBuffers.stream()));
    // binary
    map.put("binary", doBenchmark(uuidByteBuffers.stream(),
        UuidTools::fromByteBuffer, binaryUuidRepository::getByUuid));

    return map;
  }

  private <T, R> LongSummaryStatistics benchmark(
      Function<ByteBuffer, T> converter,
      Function<T, R> finder) {
    Stream<UuidEntry> uuidEntryStream = prepareRandomUuidStream();
    return doBenchmark(uuidEntryStream, converter, finder);
  }

  private Stream<UuidEntry> prepareRandomUuidStream() {
    Random r = new Random();
    return r.longs(constants.getBenchmarkTestSize(), 0, constants.getBenchmarkDataSize())
        .mapToObj(l -> {
          int n = Math.toIntExact(l);
          ByteBuffer bb = uuidCache.getUuidBytes(n);
          return new UuidEntry(n, bb);
        });
  }

  private <T, R> LongSummaryStatistics doBenchmark(
      Stream<UuidEntry> stream,
      Function<ByteBuffer, T> converter, Function<T, R> finder) {

    return stream.mapToLong(ent -> {
      T arg = converter.apply(ent.getBuffer());
      long begin = System.currentTimeMillis();
      R found = finder.apply(arg);
      long end = System.currentTimeMillis();
      if (found == null) {
        throw new RuntimeException(MessageFormat.format(
            "Can not find data at position {0}: {1}", ent.pos,
            UuidTools.fromByteBuffer(uuidCache.getUuidBytes(ent.pos)).toString()));
      }
      return end - begin;
    }).summaryStatistics();
  }

  private LongSummaryStatistics getNumberBenchmark(Stream<UuidEntry> uuidStream) {
    Random r = new Random();
    return uuidStream.mapToLong(ent -> {
      BigInteger bi1 = BigInteger.valueOf(ent.getBuffer().getLong());
      BigInteger bi2 = BigInteger.valueOf(ent.getBuffer().getLong());
      long begin = System.currentTimeMillis();
      NumberUuid found = numberUuidRepository.getByUuid1AndUuid2(bi1, bi2);
      long end = System.currentTimeMillis();
      if (found == null) {
        throw new RuntimeException(MessageFormat.format(
            "Can not find data at position {0}: {1}", ent.pos,
            UuidTools.fromByteBuffer(uuidCache.getUuidBytes(ent.pos)).toString()));
      }
      return end - begin;
    }).summaryStatistics();
  }

  private static class UuidEntry {

    private int pos;
    private ByteBuffer buffer;

    public UuidEntry(int pos, ByteBuffer buffer) {
      this.pos = pos;
      this.buffer = buffer;
    }

    public ByteBuffer getBuffer() {
      return buffer.duplicate();
    }
  }
}
