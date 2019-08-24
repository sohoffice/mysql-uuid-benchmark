package com.sohoffice.mysqluuidbenchmark;

import org.springframework.stereotype.Component;

@Component
public class Constants {

  private final int BENCHMARK_DATA_SIZE = 10000000;

  public int getBenchmarkDataSize() {
    return BENCHMARK_DATA_SIZE;
  }

  public int getBenchmarkTestSize() {
    return 10000;
  }
}
