package com.sohoffice.mysqluuidbenchmark;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * A pre allocated cache in memory. Capable to hold 10 million uuid bytes.
 */
@Service
public class UuidCache {

  @Autowired
  private Constants constants;

  private int endPos;

  @Autowired
  @Qualifier("cache_bytes")
  private byte[] cache;

  public synchronized void append(UUID uuid) {
    ByteBuffer bb = UuidTools.toByteBuffer(uuid);
    System.arraycopy(bb.array(), 0, cache, endPos, 16);
    endPos += 16;
  }

  public ByteBuffer getUuidBytes(int pos) {
    byte[] bar = new byte[16];
    System.arraycopy(cache, pos * 16, bar, 0, 16);
    return ByteBuffer.wrap(bar);
  }
}
