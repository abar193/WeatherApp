package me.mrabar.weatherapp.api;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

public class RequestUtils {
  public static String ipFromRequest(ServerRequest request) {
    return Optional.of(request.exchange())
        .map(ServerWebExchange::getRequest)
        .map(ServerHttpRequest::getRemoteAddress)
        .map(InetSocketAddress::getAddress)
        .map(InetAddress::getHostAddress)
        .orElse(null);
  }
}
