package com.artemsorokin.client;

import com.artemsorokin.model.F1DriverResponse;
import com.artemsorokin.model.OpenF1SessionResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "OpenF1Client", url = "${client.url}")
public interface OpenF1ApiClient {

  @GetMapping("/sessions")
  List<OpenF1SessionResponse> getSessions(
      @RequestParam(name = "country_name", required = false) String countryName,
      @RequestParam(name = "session_type", required = false) String sessionType,
      @RequestParam(name = "year", required = false) String year);

  @GetMapping("/drivers")
  List<F1DriverResponse> getDrivers(@RequestParam(name = "session_key") int sessionKey);

  @GetMapping("/sessions")
  List<OpenF1SessionResponse> getSessionByKey(@RequestParam(name = "session_key") int sessionKey);
}
