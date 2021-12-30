package me.mrabar.weatherapp.weather;

import me.mrabar.weatherapp.location.Location;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class WeatherComponentTest {

  @Test
  void returnsResultFromWorkingLookupComponent() {
    var weatherComponent = new WeatherComponent(List.of(loc -> Mono.just(new WeatherResult("loc", "desc"))));

    var result = weatherComponent.getWeather(new Location(Optional.empty(), "")).single().block();
    assertNotNull(result);
    assertEquals("loc", result.location());
    assertEquals("desc", result.weather());
  }

  @Test
  void returnsNothingWithBrokenLookupComponent() {
    var weatherComponent = new WeatherComponent(List.of(loc -> Mono.empty()));

    var result = weatherComponent.getWeather(new Location(Optional.empty(), "")).block();
    assertNull(result);
  }

  @Test
  void returnsFirstSuccessfullyLoadedResult() {
    var weatherComponent = new WeatherComponent(List.of(
        loc -> Mono.empty(),
        loc -> Mono.empty(),
        loc -> Mono.just(new WeatherResult("loc", "desc")),
        loc -> Mono.just(new WeatherResult("gibberish", "you just paid 0.01$ for this query")),
        loc -> {throw new RuntimeException("Whoops");}
    ));

    var result = weatherComponent.getWeather(new Location(Optional.empty(), "")).block();

    assertNotNull(result);
    assertEquals("loc", result.location());
    assertEquals("desc", result.weather());
  }

}