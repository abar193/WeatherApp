package me.mrabar.weatherapp.location;

import java.util.Optional;

public record Location(Optional<String> city, String country) {
  @Override
  public String toString() {
    if(city().isPresent()) {
      return String.format("%s, %s", city().get(), country());
    }
    return country();
  }
}
