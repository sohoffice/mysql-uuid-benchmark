package com.sohoffice.mysqluuidbenchmark.controller;

import com.sohoffice.mysqluuidbenchmark.Constants;
import com.sohoffice.mysqluuidbenchmark.UuidTools;
import com.sohoffice.mysqluuidbenchmark.entity.BinaryUuid;
import com.sohoffice.mysqluuidbenchmark.entity.NumberUuid;
import com.sohoffice.mysqluuidbenchmark.entity.StringUuid;
import com.sohoffice.mysqluuidbenchmark.repository.BinaryUuidRepository;
import com.sohoffice.mysqluuidbenchmark.repository.NumberUuidRepository;
import com.sohoffice.mysqluuidbenchmark.repository.StringUuidRepository;
import java.nio.ByteBuffer;
import java.util.LongSummaryStatistics;
import java.util.UUID;
import java.util.stream.LongStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/data", produces = "application/json")
public class DataController {

  @Autowired
  private Constants constants;

  @Autowired
  private PlatformTransactionManager platformTransactionManager;

  @Autowired
  private BinaryUuidRepository binaryUuidRepository;

  @Autowired
  private NumberUuidRepository numberUuidRepository;

  @Autowired
  private StringUuidRepository stringUuidRepository;

  @RequestMapping(value = "/prepare", method = RequestMethod.POST)
  public LongSummaryStatistics postPrepare() {
    // init my transaction template
    TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
    // loop for n / 1000 groups
    return LongStream.range(0, constants.getBenchmarkSize() / 1000)
        .flatMap(gn -> {
          // wrap in a transaction
          return transactionTemplate.execute(status -> {
            // loop 1000 times within a group
            LongStream outStream = LongStream.range(0, 1000).map(i -> {
              long n = gn * 1000 + i;
              UUID uuid = UUID.randomUUID();
              ByteBuffer bb = UuidTools.toByteBuffer(uuid);
              BinaryUuid bu = new BinaryUuid();
              bu.setId(n);
              bu.setUuid(uuid);
              binaryUuidRepository.save(bu);

              NumberUuid nb = new NumberUuid();
              nb.setId(n);
              nb.setUuidObject(uuid);
              numberUuidRepository.save(nb);

              StringUuid su = new StringUuid();
              su.setId(n);
              su.setUuid(uuid.toString());
              stringUuidRepository.save(su);

              return 1;
            });

            logger.info("{} records loaded", (gn + 1) * 1000);
            return outStream;
          });
        }).summaryStatistics();
  }

  private static Logger logger = LoggerFactory.getLogger(DataController.class.getName());
}
