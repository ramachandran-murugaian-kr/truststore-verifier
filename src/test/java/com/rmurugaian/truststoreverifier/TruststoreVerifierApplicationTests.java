package com.rmurugaian.truststoreverifier;

import com.azure.cosmos.models.PartitionKey;
import com.rmurugaian.truststoreverifier.model.FutureExClientRequest;
import com.rmurugaian.truststoreverifier.model.FutureExClientResponse;
import com.rmurugaian.truststoreverifier.repository.TransactionRepository;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = TruststoreVerifierApplication.class)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
@ActiveProfiles("integration")
class TruststoreVerifierApplicationTests {

  @Autowired private WebTestClient webClient;
  @Autowired private TransactionRepository transactionRepository;

  @Test
  void contextLoads() {

    // Decrypt the value of the TKTW command
    final var response =
        webClient
            .post()
            .uri("/decrypt")
            .header("X-Correlation-ID", UUID.randomUUID().toString())
            .bodyValue(FutureExClientRequest.builder().command("TKTW").build())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(FutureExClientResponse.class)
            .returnResult()
            .getResponseBody();

    Assertions.assertThat(response).isNotNull();

    // Verify transaction saved into DB
    StepVerifier.create(
            transactionRepository.findById(
                response.getId(), new PartitionKey(response.getId().toString())))
        .expectNextMatches(transactionDocument -> "TKTW".equals(transactionDocument.getCommand()))
        .verifyComplete();
  }
}
