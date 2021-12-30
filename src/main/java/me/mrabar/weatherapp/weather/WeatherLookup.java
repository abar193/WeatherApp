package me.mrabar.weatherapp.weather;

import me.mrabar.weatherapp.location.Location;
import reactor.core.publisher.Mono;

public interface WeatherLookup {
  Mono<WeatherResult> describeWeather(Location location);
}
