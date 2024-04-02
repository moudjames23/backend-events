package com.moudjames23.eventskumojin.model.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Length(max = 32, message = "Le nom ne doit pas depassé 32 caracetères")
    private String name;

    @NotBlank(message = "La description est obligatoire")
    private String description;


    @NotBlank(message =  "La date de début est obligatoire")
    private String startDate;

    @NotBlank(message = "La date de fin est obligatoire")
    private String endDate;
}
