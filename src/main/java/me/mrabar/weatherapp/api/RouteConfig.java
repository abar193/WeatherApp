package me.mrabar.weatherapp.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class RouteConfig {
  @Bean
  public RouterFunction<ServerResponse> route(WeatherHandler someHandler) {
    return RouterFunctions
        .route(GET("/weather"), someHandler::weather);
  }
}
