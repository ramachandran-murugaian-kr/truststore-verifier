package com.rmurugaian.truststoreverifier.spi;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange(contentType = APPLICATION_JSON_VALUE, accept = APPLICATION_JSON_VALUE)
public interface FutureExClient {

  @PostExchange(url = "/guardian")
  Mono<ResponseEntity<FutureExResponse>> guardian(
      @RequestHeader(name = "X-Correlation-Id") final String correlationId,
      @RequestBody final FutureExRequest futureExRequest);
}
