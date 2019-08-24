package com.sohoffice.mysqluuidbenchmark.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity(name = "StringUuid")
@Table(name = "STRING_UUID", schema = "uuid", indexes = @Index(columnList = "uuid", unique = true))
public class StringUuid {

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "UUID")
  private String uuid;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public UUID getUuidObject() {
    return UUID.fromString(uuid);
  }
}
