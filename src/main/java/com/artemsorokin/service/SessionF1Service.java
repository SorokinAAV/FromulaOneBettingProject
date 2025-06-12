package com.artemsorokin.service;

import com.artemsorokin.model.SessionF1;
import java.util.List;

public interface SessionF1Service {

    List<SessionF1> fetchSessions(String countryName, String sessionType, String year);

    List<SessionF1> getSessionByKey(int sessionKey);
}
