package com.sohoffice.mysqluuidbenchmark.entity;

import com.sohoffice.mysqluuidbenchmark.UuidTools;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity(name = "NumberUuid")
@Table(name = "NUMBER_UUID", schema = "uuid", indexes = @Index(
    name = "number_uuid_idx1", columnList = "uuid1, uuid2", unique = true
))
public class NumberUuid {

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "UUID1")
  private BigInteger uuid1;

  @Column(name = "UUID2")
  private BigInteger uuid2;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigInteger getUuid1() {
    return uuid1;
  }

  public void setUuid1(BigInteger uuid1) {
    this.uuid1 = uuid1;
  }

  public BigInteger getUuid2() {
    return uuid2;
  }

  public void setUuid2(BigInteger uuid2) {
    this.uuid2 = uuid2;
  }

  public UUID getUuidObject() {
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.put(uuid1.toByteArray());
    bb.put(uuid2.toByteArray());
    return UUID.nameUUIDFromBytes(bb.array());
  }

  public void setUuidObject(UUID uuid) {
    ByteBuffer bb = UuidTools.toByteBuffer(uuid);
    uuid1 = BigInteger.valueOf(bb.getLong());
    uuid2 = BigInteger.valueOf(bb.getLong());
  }
}
