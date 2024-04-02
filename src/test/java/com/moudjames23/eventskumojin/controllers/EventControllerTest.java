package com.moudjames23.eventskumojin.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moudjames23.eventskumojin.entities.Event;
import com.moudjames23.eventskumojin.repositories.EventRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;
    private final String BASE_URL = "http://127.0.0.1:8080/api/v1";

    private final String contentType = "application/json";

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {

        mapper = new ObjectMapper();

        eventRepository.deleteAll();
    }

    @AfterAll
    void clearDatabase() {
        eventRepository.deleteAll();
    }

    @Test
    @Order(1)
    void itShouldListEmptyEvent() throws Exception {
        // Given
        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/events")
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("OK")))
                .andExpect(jsonPath("$.data.events", hasSize(0)));
    }

    @Test
    @Order(2)
    void itShouldListEvents() throws Exception {
        //given
        Event event1 = Event.builder()
                .name("Event 1")
                .description("Description 1")
                .startDate("2024-03-15T14:00:00+02:00")
                .endDate("2024-03-30T14:00:00+02:00")
                .build();

        Event event2 = Event.builder()
                .name("Event 2")
                .description("Description 2")
                .startDate("2024-03-01T14:00:00+02:00")
                .endDate("2024-04-30T14:00:00+02:00")
                .build();

        eventRepository.saveAll(Arrays.asList(event1, event2));

        //then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/events")
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("OK")))
                .andExpect(jsonPath("$.data.events", hasSize(2)))
                .andExpect(jsonPath("$.data.events[0].name", is(event1.getName())))
                .andExpect(jsonPath("$.data.events[1].name", is(event2.getName())));

    }

    @Test
    @Order(3)
    void itShouldNotShowEventById() throws Exception {

        // Given
        int eventId = 65;

        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/events/" + eventId)
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("L'évênenement avec pour ID " + eventId + " n'existe pas")));

    }

    @Test
    @Order(4)
    void itShouldNotShowEventWhenParamIsNotANumber() throws Exception {

        // Given
        String eventId = "moud";

        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/events/" + eventId)
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("'eventId' doit être de type 'Long' et '"+eventId+"' n'en est pas un")));

    }

    @Test
    @Order(5)
    void itShouldShowEventById() throws Exception {
        //given
        Event event = eventRepository.save(Event.builder()
                .name("Event 4")
                .description("Description 4")
                .startDate("2024-03-01T14:00:00+02:00")
                .endDate("2024-04-30T14:00:00+02:00")
                .build());
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/events/" + event.getId())
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("OK")))
                .andExpect(jsonPath("$.data.event.name", is(event.getName())));

    }

    @Test
    @Order(6)
    void itShouldCreateEvent() throws Exception {
        //given
        Event event = eventRepository.save(Event.builder()
                .name("Event 5")
                .description("Description 5")
                .startDate("2024-03-01T14:00:00+02:00")
                .endDate("2024-04-30T14:00:00+02:00")
                .build());
        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/events")
                                .content(mapper.writeValueAsString(event))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(201)))
                .andExpect(jsonPath("$.message", is("OK")))
                .andExpect(jsonPath("$.data.event.name", is(event.getName())));

    }

    @Test
    @Order(7)
    void itShouldNotSaveEventWhenMissingFields() throws Exception {

        Event event = eventRepository.save(Event.builder()
                .build());
        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/events")
                                .content(mapper.writeValueAsString(event))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("NOK")))
                .andExpect(jsonPath("$.errors.name", is("Le nom est obligatoire")))
                .andExpect(jsonPath("$.errors.description", is("La description est obligatoire")))
                .andExpect(jsonPath("$.errors.endDate", is("La date de fin est obligatoire")))
                .andExpect(jsonPath("$.errors.startDate", is("La date de début est obligatoire")));

    }

    @Test
    @Order(8)
    void itShouldNotSaveEventWhenNameIsMissing() throws Exception {

        Event event = eventRepository.save(Event.builder()
                .description("Description 5")
                .startDate("2024-03-01T14:00:00+02:00")
                .endDate("2024-04-30T14:00:00+02:00")
                .build());
        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/events")
                                .content(mapper.writeValueAsString(event))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("NOK")))
                .andExpect(jsonPath("$.errors.name", is("Le nom est obligatoire")));

    }

    @Test
    @Order(9)
    void itShouldNotSaveEventWhenNameIsBiggerThan32Characteres() throws Exception {

        Event event = eventRepository.save(Event.builder()
                .name("12345678901234567890123456789012356")
                .description("Description 5")
                .startDate("2024-03-01T14:00:00+02:00")
                .endDate("2024-04-30T14:00:00+02:00")
                .build());
        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/events")
                                .content(mapper.writeValueAsString(event))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("NOK")))
                .andExpect(jsonPath("$.errors.name", is("Le nom ne doit pas depassé 32 caracetères")));

    }



}

