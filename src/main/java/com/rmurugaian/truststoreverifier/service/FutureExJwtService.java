package com.rmurugaian.truststoreverifier.service;

import reactor.core.publisher.Mono;

public interface FutureExJwtService {

  Mono<String> jwt();
}
