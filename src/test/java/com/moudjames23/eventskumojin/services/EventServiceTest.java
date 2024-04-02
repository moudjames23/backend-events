package com.moudjames23.eventskumojin.services;

import com.moudjames23.eventskumojin.entities.Event;
import com.moudjames23.eventskumojin.exceptions.ResourceNotFoundException;
import com.moudjames23.eventskumojin.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventService = new EventService(eventRepository);
    }

    @Test
    void itShouldListEmptyEvent() {
        //given
        //when
        List<Event> expected = eventService.list();

        //then
        assertThat(expected).isEmpty();

    }

    @Test
    void itShouldNotEmptyEvent() {
        //given
        Event event = Event.builder()
                .id(1L)
                .name("Event 1")
                .description("Description 1")
                .startDate("2024-03-15T14:00:00+02:00")
                .endDate("2024-03-30T14:00:00+02:00")
                .build();

        List<Event> data = new LinkedList<>();
        data.add(event);

        when(eventService.list()).thenReturn(data);

        //when
        List<Event> expected = eventService.list();

        //then
        assertThat(expected).isNotEmpty();

    }

    @Test
    void itShouldNotDisplayEventById() {
        Long eventId= 4L;

        assertThatThrownBy(() ->eventService.show(eventId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("L'évênenement avec pour ID " + eventId + " n'existe pas");

    }


    @Test
    void itShouldDisplayEventById() {
        Event event = Event.builder()
                .id(2L)
                .name("Event 2")
                .description("Description 2")
                .startDate("2024-03-03T14:00:00+02:00")
                .endDate("2024-03-23T14:00:00+02:00")
                .build();

        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        //when
        Event expected = eventService.show(event.getId());

        //then
        assertThat(expected).isEqualTo(event);
    }

    @Test
    void itShouldCreateEvent() {
        Event event = Event.builder()
                .id(3L)
                .name("Event 3")
                .description("Description 3")
                .startDate("2024-03-03T14:00:00+02:00")
                .endDate("2024-03-23T14:00:00+02:00")
                .build();
        // When
        this.eventService.create(event);

        //then
        ArgumentCaptor<Event> argumentCaptor =ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(argumentCaptor.capture());

        Event expected = argumentCaptor.getValue();
        assertThat(expected).isEqualTo(event);
    }

}