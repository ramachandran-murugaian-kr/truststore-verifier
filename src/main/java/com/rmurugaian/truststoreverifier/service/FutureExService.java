package com.rmurugaian.truststoreverifier.service;

import com.rmurugaian.truststoreverifier.model.FutureExClientRequest;
import com.rmurugaian.truststoreverifier.model.FutureExClientResponse;
import com.rmurugaian.truststoreverifier.spi.FutureExClient;
import com.rmurugaian.truststoreverifier.spi.FutureExRequest;
import com.rmurugaian.truststoreverifier.spi.FutureExResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FutureExService {

  private final FutureExJwtService futureExJwtService;
  private final FutureExClient futureExClient;

  public Mono<FutureExClientResponse> decrypt(
      final String correlationId, final FutureExClientRequest futureExClientRequest) {

    return futureExJwtService
        .jwt()
        .map(
            token ->
                FutureExRequest.builder()
                    .ai("baseDerivationKey")
                    .ao(futureExClientRequest.getCommand())
                    .ti("1")
                    .ak(futureExClientRequest.getValue())
                    .gm("keySerialNumber")
                    .ro("8")
                    .tw("tweak")
                    .fs("6")
                    .va("2")
                    .td("2")
                    .wt("6")
                    .jw(token)
                    .build())
        .flatMap(it -> futureExClient.guardian(correlationId, it))
        .mapNotNull(HttpEntity::getBody)
        .map(this::handleResponse)
        .onErrorResume(
            Exception.class,
            e -> Mono.just(FutureExClientResponse.builder().error(e.getMessage()).build()));
  }

  private FutureExClientResponse handleResponse(final FutureExResponse futureExResponse) {

    if (futureExResponse == null) {
      return FutureExClientResponse.builder().error("No response received").build();
    }

    final var ao = futureExResponse.getAo();
    if ("ERRO".equals(ao)) {
      return FutureExClientResponse.builder().error(futureExResponse.getCommand()).build();
    }

    return FutureExClientResponse.builder().decrypted(futureExResponse.getAo()).build();
  }
}
