package com.sohoffice.mysqluuidbenchmark.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity(name = "BinaryUuid")
@Table(name = "BINARY_UUID", schema = "uuid", indexes = @Index(columnList = "uuid", unique = true))
public class BinaryUuid {

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "UUID", unique = true, columnDefinition = "BINARY(16)")
  private UUID uuid;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }
}
