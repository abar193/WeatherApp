package me.mrabar.weatherapp.location;

import reactor.core.publisher.Mono;

public interface LocationFinder {
  default int order() { return 0; }
  Mono<Location> lookup(String ip);
}
