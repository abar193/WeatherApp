package me.mrabar.weatherapp.location;

import reactor.core.publisher.Mono;

public interface LocationFinder {
  Mono<Location> lookup(String ip);
}
