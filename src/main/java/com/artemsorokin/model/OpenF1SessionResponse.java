package com.artemsorokin.model;

import lombok.Data;

@Data
public class OpenF1SessionResponse {
  private int circuit_key;
  private String circuit_short_name;
  private String country_code;
  private int country_key;
  private String country_name;
  private String date_end;
  private String date_start;
  private String gmt_offset;
  private String location;
  private int meeting_key;
  private int session_key;
  private String session_name;
  private String session_type;
  private int year;
}
