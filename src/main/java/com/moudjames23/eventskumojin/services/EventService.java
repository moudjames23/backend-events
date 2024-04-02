package com.moudjames23.eventskumojin.services;

import com.moudjames23.eventskumojin.entities.Event;
import com.moudjames23.eventskumojin.exceptions.ResourceNotFoundException;
import com.moudjames23.eventskumojin.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EventService {

    private  final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> list ()
    {
        return this.eventRepository.findAll();
    }

    public Event create(Event event)
    {
        return this.eventRepository.save(event);
    }

    public Event show(Long eventId)
    {
        return  this.eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("L'évênenement avec pour ID " + eventId + " n'existe pas"));
    }

}
