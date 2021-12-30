package me.mrabar.weatherapp.location.finders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.mrabar.weatherapp.location.Location;
import me.mrabar.weatherapp.location.LocationFinder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class IpLocationNet implements LocationFinder {
  private static final String ENDPOINT = "https://api.iplocation.net";
  // This service ignores accept header and always returns "text/html".
  // Need to parse String to Map manually and extract whatever fields I need myself.
  private static final ObjectMapper mapper = new ObjectMapper();

  private final WebClient webClient;

  @Override
  public Mono<Location> lookup(String ip) {
    return webClient.get()
        .uri(String.format("%s?ip=%s", ENDPOINT, ip))
        .accept(MediaType.APPLICATION_JSON)
        .exchangeToMono(IpLocationNet::parseResponse);
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
