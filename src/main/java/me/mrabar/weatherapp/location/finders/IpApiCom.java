package me.mrabar.weatherapp.location.finders;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import me.mrabar.weatherapp.location.Location;
import me.mrabar.weatherapp.location.LocationFinder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.logging.Level;

@RequiredArgsConstructor
@Component
@Log
public class IpApiCom implements LocationFinder {
  private static final String ENDPOINT = "http://ip-api.com/json/%s?fields=status,country,city,query";

  private final WebClient webClient;

  @Override
  public int order() {
    return 0;
  }

  @Override
  public Mono<Location> lookup(String ip) {
    return webClient.get()
        .uri(String.format(ENDPOINT, ip))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(ResponseDTO.class)
        .map(r -> {
          if (r.status.equals("fail")) {
            throw new IllegalStateException("Lookup failed");
          }
          return new Location(Optional.of(r.city), r.country);
        })
        .onErrorResume(t -> {
          log.log(Level.WARNING, "Error in ip-api.com", t);
          return Mono.empty();
        });
  }

  private record ResponseDTO(String query, String status, String country, String city) {

  }
}
