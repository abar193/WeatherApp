package me.mrabar.weatherapp.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class LocationComponent {
  private final List<LocationFinder> finders;

  @PostConstruct
  public void init() {
    finders.sort(Comparator.comparing(LocationFinder::order));
  }

  public Mono<Location> getLocation(String ip) {
    return Flux.fromIterable(finders)
        .flatMapSequential(f -> f.lookup(ip), 1) // here we could experiment with concurrency, depending on what we need
        .filter(Objects::nonNull)
        .take(1)
        .singleOrEmpty()
        .cache(Duration.ofDays(1));
  }
}
