package com.rmurugaian.truststoreverifier.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("future-ex")
@Getter
@Setter
public class FutureExConfigProperties {
  private String url;
  private int connectTimeout = 10_000;
  private Duration responseTimeout = Duration.ofMillis(30_000);

  @NestedConfigurationProperty
  private ClientSslConfigProperties sslConfig = new ClientSslConfigProperties();

  @Getter
  @Setter
  public class ClientSslConfigProperties {
    String keyStorePath;
    char[] keyStorePassword;
    String trustStorePath;
    char[] trustStorePassword;
  }
}
