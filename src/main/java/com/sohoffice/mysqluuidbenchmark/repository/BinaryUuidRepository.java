package com.sohoffice.mysqluuidbenchmark.repository;

import com.sohoffice.mysqluuidbenchmark.entity.BinaryUuid;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BinaryUuidRepository extends CrudRepository<BinaryUuid, Long> {

  BinaryUuid getByUuid(byte[] uuid);

  @Query("select bu FROM BinaryUuid bu ORDER BY bu.id ASC")
  Stream<BinaryUuid> streamAll();

}
