package com.artemsorokin.model;

import lombok.Data;

@Data
public class F1DriverResponse {
    private String broadcast_name;
    private String country_code;
    private int driver_number;
    private String first_name;
    private String full_name;
    private String headshot_url;
    private String last_name;
    private int meeting_key;
    private String name_acronym;
    private int session_key;
    private String team_colour;
    private String team_name;
}
