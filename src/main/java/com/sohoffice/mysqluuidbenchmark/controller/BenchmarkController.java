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
import java.util.LongSummaryStatistics;
import java.util.Random;
import java.util.function.Function;
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
    Random r = new Random();
    return r.longs(constants.getBenchmarkTestSize(), 0, constants.getBenchmarkDataSize())
        .map(l -> {
          int n = Math.toIntExact(l);
          ByteBuffer bb = uuidCache.getUuidBytes(n);
          BigInteger bi1 = BigInteger.valueOf(bb.getLong());
          BigInteger bi2 = BigInteger.valueOf(bb.getLong());
          long begin = System.currentTimeMillis();
          NumberUuid found = numberUuidRepository.getByUuid1AndUuid2(bi1, bi2);
          long end = System.currentTimeMillis();
          if (found == null) {
            throw new RuntimeException(MessageFormat.format(
                "Can not find data at position {0}: {1}", l,
                UuidTools.fromByteBuffer(uuidCache.getUuidBytes(n)).toString()));
          }
          return end - begin;
        }).summaryStatistics();
  }

  @RequestMapping(value = "/benchmark/bin", method = RequestMethod.POST)
  public LongSummaryStatistics benchmarkBinary() {
    return benchmark(UuidTools::fromByteBuffer, binaryUuidRepository::getByUuid);
  }

  private <T, R> LongSummaryStatistics benchmark(
      Function<ByteBuffer, T> converter,
      Function<T, R> finder) {
    Random r = new Random();
    return r.longs(constants.getBenchmarkTestSize(), 0, constants.getBenchmarkDataSize())
        .map(l -> {
          int n = Math.toIntExact(l);
          ByteBuffer bb = uuidCache.getUuidBytes(n);
          T arg = converter.apply(bb);
          long begin = System.currentTimeMillis();
          R found = finder.apply(arg);
          long end = System.currentTimeMillis();
          if (found == null) {
            throw new RuntimeException(MessageFormat.format(
                "Can not find data at position {0}: {1}", l,
                UuidTools.fromByteBuffer(uuidCache.getUuidBytes(n)).toString()));
          }
          return end - begin;
        }).summaryStatistics();
  }
}
