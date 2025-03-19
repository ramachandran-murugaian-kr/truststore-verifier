package com.rmurugaian.truststoreverifier;

import com.rmurugaian.truststoreverifier.model.FutureExClientRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = TruststoreVerifierApplication.class)
class TruststoreVerifierApplicationTests {

  @Autowired private WebTestClient webClient;

  @Test
  void contextLoads() {
    webClient
        .post()
        .uri("/decrypt")
        .header("X-Correlation-ID", UUID.randomUUID().toString())
        .bodyValue(FutureExClientRequest.builder().command("TKTW").build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.decrypted")
        .isEmpty()
        .jsonPath("$.error")
        .isEqualTo("[AOERRO;AM34;ANAO;BBINVALID TOKEN;]");
  }
}
