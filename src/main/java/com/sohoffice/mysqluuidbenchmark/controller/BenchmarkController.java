package com.sohoffice.mysqluuidbenchmark.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json")
public class BenchmarkController {

  @RequestMapping(value = "/benchmark", method = RequestMethod.POST)
  public String postBenchmark() {
    return null;
  }

}
