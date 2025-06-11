package com.artemsorokin.controller;

import com.artemsorokin.model.EventOutcome;
import com.artemsorokin.service.EventOutcomeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/f1/events")
@AllArgsConstructor
public class EventOutcomeController {

  private final EventOutcomeService eventOutcomeService;

  @PostMapping("/outcome")
  public ResponseEntity<String> submitOutcome(@RequestBody EventOutcome eventOutcome) {
    String result = eventOutcomeService.processEventOutcome(eventOutcome);
    return ResponseEntity.ok(result);
  }
}
