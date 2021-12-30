package me.mrabar.weatherapp.weather.loaders;

import me.mrabar.weatherapp.location.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WeatherApiComTest {
  @Autowired
  private WeatherApiCom weatherApiCom;

  @Test
  void testForLatvia() {
    var weather = weatherApiCom.describeWeather(new Location(Optional.empty(), "Latvia")).block();
    assertNotNull(weather);
    assertTrue(weather.toString().contains("Latvia"), "Should contain something about Latvia");
  }

  @Test
  void testForGibberish() {
    var weather = weatherApiCom.describeWeather(new Location(Optional.empty(), "ASDASDASDASDASD")).block();
    assertNull(weather);
  }
}