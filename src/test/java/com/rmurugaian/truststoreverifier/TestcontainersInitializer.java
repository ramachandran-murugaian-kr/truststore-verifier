package com.rmurugaian.truststoreverifier;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.testcontainers.containers.wait.strategy.Wait.forLogMessage;
import static org.testcontainers.utility.DockerImageName.parse;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.CosmosDBEmulatorContainer;
import org.testcontainers.lifecycle.Startables;

public class TestcontainersInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  static final String COSMOS_DOCKER_IMAGE_NAME =
      "mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:vnext-EN20250122";

  static final CosmosDBEmulatorContainer COSMOS_DB_EMULATOR_CONTAINER;
  static final WireMockServer WIRE_MOCK_SERVER;

  static final String SUPERUSER_1 = "superuser-1";
  static final String SUPER_USER_PWD = "test";

  static {
    COSMOS_DB_EMULATOR_CONTAINER =
        new CosmosDBEmulatorContainer(parse(COSMOS_DOCKER_IMAGE_NAME))
            .withEnv("PROTOCOL", "https")
            .withEnv("ENABLE_EXPLORER", "true")
            .waitingFor(forLogMessage(".*Now listening on:.*", 1))
            .withStartupTimeout(Duration.ofMinutes(2));

    Startables.deepStart(COSMOS_DB_EMULATOR_CONTAINER).join();

    WIRE_MOCK_SERVER =
        new WireMockServer(
            options()
                .withRootDirectory("src/test/resources")
                .notifier(new ConsoleNotifier(true))
                .dynamicPort());

    WIRE_MOCK_SERVER.start();
  }

  @Override
  public void initialize(@NotNull ConfigurableApplicationContext ctx) {

    try {
      CosmosKeyStoreUtils.setupTrustStore(
          COSMOS_DB_EMULATOR_CONTAINER.getHost(),
          COSMOS_DB_EMULATOR_CONTAINER.getFirstMappedPort(),
          COSMOS_DB_EMULATOR_CONTAINER.getEmulatorKey());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    TestPropertyValues.of(
            "spring.cloud.azure.cosmos.endpoint="
                + COSMOS_DB_EMULATOR_CONTAINER.getEmulatorEndpoint(),
            "spring.cloud.azure.cosmos.key=" + COSMOS_DB_EMULATOR_CONTAINER.getEmulatorKey(),
            "embedded.kafka.saslPlaintext.user=" + SUPERUSER_1,
            "embedded.kafka.saslPlaintext.password=" + SUPER_USER_PWD,
            "wiremock.server.port=" + WIRE_MOCK_SERVER.port())
        .applyTo(ctx.getEnvironment());
  }
}
