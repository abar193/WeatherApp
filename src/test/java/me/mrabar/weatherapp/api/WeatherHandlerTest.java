package me.mrabar.weatherapp.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@ComponentScan
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeatherHandlerTest {
  // Normally, I would mock the components, so that this test would not rely on external service data

  @Autowired
  private WeatherHandler handler;

  @Autowired
  private WebTestClient webClient;

  @Test
  void testWeatherEndpoint() {
    webClient.get().uri("/weather")
        .exchange()
        .expectStatus().is4xxClientError()
        .expectBody().json("{\"error\":\"Could not obtain weather data for ip 127.0.0.1\"}");
  }

  @Test
  void testWeatherEndpointWithParam() {
    webClient.get().uri("/weather?ip=8.8.8.8")
        .exchange()
        .expectStatus().isOk();
  }
}