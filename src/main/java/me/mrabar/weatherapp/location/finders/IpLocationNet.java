package me.mrabar.weatherapp.location.finders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import me.mrabar.weatherapp.location.Location;
import me.mrabar.weatherapp.location.LocationFinder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.logging.Level;

@RequiredArgsConstructor
@Component
@Log
public class IpLocationNet implements LocationFinder {
  private static final String ENDPOINT = "https://api.iplocation.net";
  // This service ignores accept header and always returns "text/html".
  // Need to parse String to Map manually and extract whatever fields I need myself.
  private static final ObjectMapper mapper = new ObjectMapper();

  private final WebClient webClient;

  @Override
  public int order() {
    return 1;
  }

  @Override
  public Mono<Location> lookup(String ip) {
    return webClient.get()
        .uri(String.format("%s?ip=%s", ENDPOINT, ip))
        .accept(MediaType.APPLICATION_JSON)
        .exchangeToMono(IpLocationNet::parseResponse)
        .onErrorResume(t -> {
          log.log(Level.WARNING, "Error in iplocation.net", t);
          return Mono.empty();
        });
  }

  private static Mono<Location> parseResponse(ClientResponse response) {
    if (!response.statusCode().equals(HttpStatus.OK)) {
      return Mono.empty();
    }
    return response
        .bodyToMono(String.class)
        .flatMap(IpLocationNet::convertBody);
  }

  private static Mono<Location> convertBody(String body) {
    try {
      var tree = mapper.readTree(body);
      if(tree.get("response_code").asText().equals("200")) {
        var result = new Location(Optional.empty(), tree.get("country_name").asText());
        return Mono.just(result);
      }
    } catch (JsonProcessingException ignored) {
    }
    return Mono.empty();
  }
}
