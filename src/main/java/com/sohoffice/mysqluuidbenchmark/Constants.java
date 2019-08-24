package com.sohoffice.mysqluuidbenchmark;

import org.springframework.stereotype.Component;

@Component
public class Constants {

  private final int BENCHMARK_SIZE = 10000000;

  public int getBenchmarkSize() {
    return BENCHMARK_SIZE;
  }
}
