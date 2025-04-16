package com.rmurugaian.truststoreverifier.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AppConfiguration {

  @Bean
  Jackson2ObjectMapperBuilderCustomizer customizer() {
    return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.failOnUnknownProperties(false);
  }
}
