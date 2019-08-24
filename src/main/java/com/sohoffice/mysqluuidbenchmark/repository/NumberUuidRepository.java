package com.sohoffice.mysqluuidbenchmark.repository;

import com.sohoffice.mysqluuidbenchmark.entity.NumberUuid;
import java.math.BigInteger;
import org.springframework.data.repository.CrudRepository;

public interface NumberUuidRepository extends CrudRepository<NumberUuid, Long> {

  NumberUuid getByUuid1AndUuid2(BigInteger uuid1, BigInteger uuid2);

}
