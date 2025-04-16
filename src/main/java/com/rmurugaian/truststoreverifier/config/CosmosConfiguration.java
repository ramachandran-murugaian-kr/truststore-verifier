package com.rmurugaian.truststoreverifier.config;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CosmosConfiguration extends AbstractCosmosConfiguration {
  @Override
  protected String getDatabaseName() {
    return "transaction";
  }

  @Bean
  public CosmosClientBuilder cosmosClientBuilder(
      @Value("${spring.cloud.azure.cosmos.endpoint}") final String endpoint,
      @Value("${spring.cloud.azure.cosmos.key}") final String key) {

    return new CosmosClientBuilder()
        .endpoint(endpoint)
        .key(key)
        .gatewayMode()
        .endpointDiscoveryEnabled(false);
  }
}
