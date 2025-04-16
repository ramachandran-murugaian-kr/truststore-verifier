package com.rmurugaian.truststoreverifier;

import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableReactiveCosmosRepositories(basePackages = "com.rmurugaian.truststoreverifier.repository")
public class TruststoreVerifierApplication {

  public static void main(String[] args) {
    SpringApplication.run(TruststoreVerifierApplication.class, args);
  }
}
