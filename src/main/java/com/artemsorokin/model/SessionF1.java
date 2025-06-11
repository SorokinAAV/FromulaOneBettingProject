package com.artemsorokin.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class SessionF1 {
    private String sessionType;
    private String year;
    private String country;
    private String date_end;
    private String date_start;
    private String location;
    private String sessionName;
    private int sessionKey;
    private List<Driver> drivers;
}
