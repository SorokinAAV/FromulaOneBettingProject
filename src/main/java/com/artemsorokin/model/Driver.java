package com.artemsorokin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Driver {
    private String fullName;
    private String driverId;
    private int odds;
}
