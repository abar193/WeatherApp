package me.mrabar.weatherapp.api;

import lombok.RequiredArgsConstructor;
import me.mrabar.weatherapp.location.LocationComponent;
import me.mrabar.weatherapp.weather.WeatherComponent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@RequiredArgsConstructor
@Component
public class WeatherHandler {
  private final LocationComponent locationComponent;
  private final WeatherComponent weatherComponent;

  public Mono<ServerResponse> weather(ServerRequest request) {
    var ip = request
        .queryParam("ip")
        .orElse(RequestUtils.ipFromRequest(request));

    if (ip == null) {
      return error("Unable to determine your ip"); // ...you sneaky bastard");
    }

    return getWeatherData(ip);
  }

  private Mono<ServerResponse> getWeatherData(String ip) {
    return locationComponent.getLocation(ip)
        .flatMap(weatherComponent::getWeather)
        .flatMap(w -> ServerResponse.ok()
            .body(BodyInserters.fromValue(w))
        ).switchIfEmpty(error("Could not obtain weather data for ip " + ip));
  }

  private Mono<ServerResponse> error(String message) {
    return ServerResponse.badRequest().body(fromValue(Map.of("error", message)));
  }

  public Mono<ServerResponse> ipLookup(ServerRequest request) {
    var ip = request
        .queryParam("ip")
        .orElse(RequestUtils.ipFromRequest(request));

    if (ip == null) {
      return error("Unable to determine your ip");
    }

    return locationComponent
        .getLocation(ip)
        .flatMap(w -> ServerResponse
            .ok()
            .body(BodyInserters.fromValue(w))
        );
  }

}
