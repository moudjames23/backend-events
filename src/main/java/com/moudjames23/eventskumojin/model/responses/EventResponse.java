package com.moudjames23.eventskumojin.model.responses;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private Long id;

    private String name;

    private String description;

    private String startDate;

    private String endDate;
}
