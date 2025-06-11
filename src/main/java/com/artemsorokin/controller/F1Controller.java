package com.artemsorokin.controller;

import com.artemsorokin.model.SessionF1;
import com.artemsorokin.service.SessionF1Service;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class F1Controller {

    private final SessionF1Service sessionService;

    @GetMapping("/api/v1/f1/sessions")
    public List<SessionF1> getSessions(
            @RequestParam(required = false) String sessionType,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String country) {

        return sessionService.fetchSessions(country, sessionType, year);
    }
}
