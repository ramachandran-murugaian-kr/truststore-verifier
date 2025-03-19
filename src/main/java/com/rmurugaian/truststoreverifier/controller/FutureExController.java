package com.rmurugaian.truststoreverifier.controller;

import com.rmurugaian.truststoreverifier.model.FutureExClientRequest;
import com.rmurugaian.truststoreverifier.model.FutureExClientResponse;
import com.rmurugaian.truststoreverifier.service.FutureExService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class FutureExController {

  private final FutureExService futureExService;

  @PostMapping("/decrypt")
  public Mono<FutureExClientResponse> decrypt(
      @RequestHeader("X-Correlation-ID") final String correlationId,
      final FutureExClientRequest futureExClientRequest) {

    return futureExService.decrypt(correlationId, futureExClientRequest);
  }
}
