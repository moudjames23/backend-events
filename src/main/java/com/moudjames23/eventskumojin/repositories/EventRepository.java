package com.moudjames23.eventskumojin.repositories;

import com.moudjames23.eventskumojin.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
