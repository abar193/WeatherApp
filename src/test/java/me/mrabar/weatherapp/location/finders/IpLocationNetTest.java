package me.mrabar.weatherapp.location.finders;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class IpLocationNetTest {

  @Autowired
  private IpLocationNet ipLocationNet;

  @Test
  void shouldReturnSomethingForGoogle() {
    var google = ipLocationNet.lookup("8.8.8.8");
    var location = google.block();

    assertNotNull(location);
    assertTrue(location.city().isEmpty());
    assertNotNull(location.country());
  }

  @Test
  void shouldReturnEmptyForIncorrectInput() {
    var res = ipLocationNet.lookup("asdasdasd");
    var location = res.block();
    assertNull(location);
  }
}