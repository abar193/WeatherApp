package me.mrabar.weatherapp.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class LocationComponent {
  private final List<LocationFinder> finders;

  public Flux<Location> getLocation(String ip) {
    return Flux.fromIterable(finders)
        .flatMap(f -> f.lookup(ip))
        .filter(Objects::nonNull)
        .take(1);
  }
}
