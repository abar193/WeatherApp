package me.mrabar.weatherapp.location;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LocationComponentTest {
  @Test
  void returnsResultFromWorkingFinderComponent() {
    var locationComponent = new LocationComponent(List.of(ip -> Mono.just(new Location(Optional.empty(), "Example"))));

    var result = locationComponent.getLocation("8.8.8.8").single().block();
    assertNotNull(result);
    assertEquals(Optional.empty(), result.city());
    assertEquals("Example", result.country());
  }

  @Test
  void returnsEmptyFromBrokenFinderComponent() {
    var locationComponent = new LocationComponent(List.of(ip -> Mono.empty()));

    var result = locationComponent.getLocation("8.8.8.8").block();
    assertNull(result);
  }

  @Test
  void returnsFirstResultFromSeveralSources() {
    var locationComponent = new LocationComponent(List.of(
        ip -> Mono.empty(),
        ip -> Mono.just(new Location(Optional.empty(), "CoolCountry")),
        ip -> Mono.just(new Location(Optional.of("Very cool city"), "This request cost you 0.05$"))
    ));

    var result = locationComponent.getLocation("8.8.8.8").block();
    assertNotNull(result);
    assertEquals(Optional.empty(), result.city());
    assertEquals("CoolCountry", result.country());

  }
}