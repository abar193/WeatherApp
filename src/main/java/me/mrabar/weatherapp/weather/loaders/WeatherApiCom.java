package me.mrabar.weatherapp.weather.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.mrabar.weatherapp.location.Location;
import me.mrabar.weatherapp.weather.WeatherLookup;
import me.mrabar.weatherapp.weather.WeatherResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class WeatherApiCom implements WeatherLookup {
  // in any normal project this would be configured outside the code base and passed as @Value, too lazy for it now
  private static final String API_KEY = "eb6ccc01f6424f9d9d0204349212912";
  private static final ObjectMapper mapper = new ObjectMapper();

  private final WebClient webClient;

  @Override
  public Mono<WeatherResult> describeWeather(Location location) {
    return webClient.get()
        .uri(String.format(
            "http://api.weatherapi.com/v1/current.json?key=%s&q=%s&aqi=no",
            API_KEY,
            location.toString()
        ))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, (f) -> Mono.empty())
        .bodyToMono(Map.class)
        .map(WeatherApiCom::parseResponseMap)
        .onErrorContinue((a, e) -> Mono.empty());
  }

  private static WeatherResult parseResponseMap(Map<String, Object> map) {
    var loc = (Map<String, String>) map.get("location");
    var current = (Map<String, Object>) map.get("current");
    var condition = (Map<String, String>) current.get("condition");

    var locationDesc = String.format(
        "%s, %s, %s",
        loc.get("name"),
        loc.get("region"),
        loc.get("country")
    );

    var weatherDesc = String.format(
        "%s, %s°C (feels like %s)",
        condition.get("text"),
        current.get("temp_c"),
        current.get("feelslike_c")
    );

    return new WeatherResult(locationDesc, weatherDesc);
  }
}
