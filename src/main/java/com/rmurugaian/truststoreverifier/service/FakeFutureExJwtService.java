package com.rmurugaian.truststoreverifier.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class FakeFutureExJwtService implements FutureExJwtService {
  @Override
  public Mono<String> jwt() {
    return Mono.just("test");
  }
}
