package me.mrabar.weatherapp.weather;

import lombok.RequiredArgsConstructor;
import me.mrabar.weatherapp.location.Location;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WeatherComponent {
  private final List<WeatherLookup> lookups;

  @Cacheable("weatherData")
  public Mono<WeatherResult> getWeather(Location location) {
    return Flux.fromIterable(lookups)
        .flatMap(l -> l.describeWeather(location))
        .filter(Objects::nonNull)
        .take(1)
        .singleOrEmpty()
        .cache();
  }
}
