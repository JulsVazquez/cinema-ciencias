package mx.unam.fciencias.cinema_ciencias.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
  // Por defecto se usa el nombre del servicio en Eureka, no una dirección fija.
  @Value("${microservices.audit.url:http://audit-service}")
  private String auditServiceUrl;

  // Con @LoadBalanced el host "audit-service" se resuelve contra Eureka
  // (Spring Cloud LoadBalancer elige una de las instancias registradas).
  @Bean
  @LoadBalanced
  public RestClient.Builder loadBalancedRestClientBuilder() {
    return RestClient.builder();
  }

  @Bean("auditRestClient")
  public RestClient auditRestClient(RestClient.Builder builder) {
    return builder.baseUrl(auditServiceUrl).build();
  }
}
