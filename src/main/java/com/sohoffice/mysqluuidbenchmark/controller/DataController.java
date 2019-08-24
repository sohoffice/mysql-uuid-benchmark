package com.sohoffice.mysqluuidbenchmark.controller;

import com.sohoffice.mysqluuidbenchmark.Constants;
import com.sohoffice.mysqluuidbenchmark.entity.BinaryUuid;
import com.sohoffice.mysqluuidbenchmark.entity.NumberUuid;
import com.sohoffice.mysqluuidbenchmark.entity.StringUuid;
import com.sohoffice.mysqluuidbenchmark.repository.BinaryUuidRepository;
import com.sohoffice.mysqluuidbenchmark.repository.NumberUuidRepository;
import com.sohoffice.mysqluuidbenchmark.repository.StringUuidRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

  @PersistenceContext
  private EntityManager entityManager;

  @RequestMapping(value = "/prepare", method = RequestMethod.POST)
  public void prepareData(@RequestParam(defaultValue = "0") long from) {
    // init my transaction template
    TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
    // loop for n / 1000 groups
    LongStream.range(from, constants.getBenchmarkSize() / 1000)
        .forEach(gn -> {
          // wrap in a transaction
          transactionTemplate.execute(status -> {
            // loop 1000 times within a group
            List<UuidObjects> list = LongStream.range(0, 1000).mapToObj(i -> {
              long n = gn * 1000 + i;
              UUID uuid = UUID.randomUUID();

              return new UuidObjects(n, uuid);
            }).collect(Collectors.toList());

            populateUuids(list);

            logger.info("{} records loaded", (gn + 1) * 1000);
            return null;
          });
        });
  }

  private void populateUuids(List<UuidObjects> list) {
    list.forEach(obj -> {
      BinaryUuid bu = new BinaryUuid();
      bu.setId(obj.id);
      bu.setUuid(obj.uuid);
      entityManager.persist(bu);
    });

    list.forEach(obj -> {
      NumberUuid nu = new NumberUuid();
      nu.setId(obj.id);
      nu.setUuidObject(obj.uuid);
      entityManager.persist(nu);
    });

    list.forEach(obj -> {
      StringUuid su = new StringUuid();
      su.setId(obj.id);
      su.setUuid(obj.uuid.toString());
      entityManager.persist(su);
    });
    entityManager.flush();
    entityManager.clear();
  }

  private static class UuidObjects {

    private long id;
    private UUID uuid;

    public UuidObjects(long id, UUID uuid) {
      this.id = id;
      this.uuid = uuid;
    }
  }

  private static Logger logger = LoggerFactory.getLogger(DataController.class.getName());
}
