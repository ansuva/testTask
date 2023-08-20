package com.example.demo1.service;

import com.example.demo1.model.Event;
import com.example.demo1.model.User;

import java.util.List;
import java.util.Optional;

public interface EventService {
    Event create (Event event);
    Event update (Event event);
    void delete (int id);
    List<Event> findAll();
    Optional<Event> findById (int id);
    void message(Event event);
}
