package com.sohoffice.mysqluuidbenchmark.repository;

import com.sohoffice.mysqluuidbenchmark.entity.StringUuid;
import org.springframework.data.repository.CrudRepository;

public interface StringUuidRepository extends CrudRepository<StringUuid, Long> {

  StringUuid getByUuid(String uuid);
}
