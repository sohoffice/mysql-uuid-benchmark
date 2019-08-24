package com.sohoffice.mysqluuidbenchmark;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UuidTools {

  public static ByteBuffer toByteBuffer(UUID uuid) {
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    // reset the position so it can be read later on.
    bb.position(0);
    return bb;
  }

  public static UUID fromByteBuffer(ByteBuffer bb) {
    return UUID.nameUUIDFromBytes(bb.array());
  }

}
