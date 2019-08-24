package com.sohoffice.mysqluuidbenchmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableAsync
public class MysqlUuidBenchmarkApplication extends WebMvcConfigurerAdapter {

  public static void main(String[] args) {
    SpringApplication.run(MysqlUuidBenchmarkApplication.class, args);
  }

  @Bean(name = "cache_bytes")
  public byte[] getCacheBytes(Constants constants) {
    return new byte[constants.getBenchmarkSize() * 16];
  }
}
