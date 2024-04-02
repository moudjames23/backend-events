package com.moudjames23.eventskumojin.controllers;

import com.moudjames23.eventskumojin.entities.Event;
import com.moudjames23.eventskumojin.model.requests.EventRequest;
import com.moudjames23.eventskumojin.model.responses.EventResponse;
import com.moudjames23.eventskumojin.model.responses.HttpResponse;
import com.moudjames23.eventskumojin.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/events")
@Tag(name = "Gestion des évènements")
@CrossOrigin("*")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Liste tous les événements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des événements récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping
    public ResponseEntity<HttpResponse> list() {
        List<EventResponse> responses = this.eventService.list().stream()
                .map(event -> EventResponse.builder()
                        .id(event.getId())
                        .name(event.getName())
                        .description(event.getDescription())
                        .startDate(event.getStartDate())
                        .endDate(event.getEndDate())
                        .build()
                )
                .toList();

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("OK")
                .data((Map.of("events", responses)))
                .build();

        return ResponseEntity.ok(httpResponse);
    }

    @PostMapping
    /*@Operation(
            description = "Ajout d'un évènement",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "NOK BAD REQUEST"
                    ),
            }
    )*/
    @Operation(summary = "Crée un nouvel événement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Événement créé avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides")
    })
    public ResponseEntity<HttpResponse> create(@Valid @RequestBody EventRequest eventRequest) {
        Event event = Event.builder()
                .name(eventRequest.getName())
                .description(eventRequest.getDescription())
                .startDate("" +eventRequest.getStartDate())
                .endDate(eventRequest.getEndDate())
                .build();


        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("OK")
                .data((Map.of("event", this.eventService.create(event))))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(httpResponse);
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Récupère les détails d'un événement spécifique par son ID",
            description = "Fournit les détails complets d'un événement, y compris son nom, sa description, et les dates de début et de fin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Détails de l'événement récupérés avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "404", description = "Événement non trouvé")
    })

    public ResponseEntity<HttpResponse> show(@PathVariable("eventId") Long eventId) {
        Event event = this.eventService.show(eventId);

        EventResponse response = EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("OK")
                .data((Map.of("event", response)))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }
}
