package com.rmurugaian.truststoreverifier.config;

import com.rmurugaian.truststoreverifier.spi.FutureExClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import javax.net.ssl.SSLException;
import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.netty.util.NettySslUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration(proxyBeanMethods = false)
class WebClientConfiguration {

  @Bean
  @Scope("prototype")
  public SSLFactory sslFactory(final FutureExConfigProperties configProperties) {
    final var properties = configProperties.getSslConfig();
    Assert.hasLength(properties.getKeyStorePath(), "Keystore path must not be empty");
    Assert.hasLength(
        new String(properties.getKeyStorePassword()), "Keystore password must not be empty");
    return SSLFactory.builder()
        .withLoggingIdentityMaterial()
        .withLoggingTrustMaterial()
        .withTrustingAllCertificatesWithoutValidation()
        .withIdentityMaterial(properties.getKeyStorePath(), properties.getKeyStorePassword())
        .build();
  }

  @Bean
  @Scope("prototype")
  public reactor.netty.http.client.HttpClient nettyHttpClient(
      final SSLFactory sslFactory, final FutureExConfigProperties configProperties)
      throws SSLException {

    var sslContext = NettySslUtils.forClient(sslFactory).build();
    return reactor.netty.http.client.HttpClient.create()
        .wiretap("reactor.netty.http", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
        .secure(sslSpec -> sslSpec.sslContext(sslContext))
        .responseTimeout(configProperties.getResponseTimeout())
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, configProperties.getConnectTimeout());
  }

  @Bean
  public WebClient webClient(
      reactor.netty.http.client.HttpClient httpClient, FutureExConfigProperties properties) {
    return WebClient.builder()
        .baseUrl(properties.getUrl())
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
  }

  @Bean
  FutureExClient futureExClient(WebClient webClient) {
    return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
        .build()
        .createClient(FutureExClient.class);
  }
}
