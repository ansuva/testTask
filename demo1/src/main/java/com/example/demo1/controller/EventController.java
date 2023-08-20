package com.example.demo1.controller;

import com.example.demo1.model.Event;
import com.example.demo1.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@RequestBody Event event){
        return eventService.create(event);
    }

    @GetMapping("{id}")
    public ResponseEntity<Event> findEventById (@PathVariable int id) {
        return (ResponseEntity<Event>) eventService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public List<Event> findAll () {
        return eventService.findAll();
    }

    @PutMapping("{id}")
    public ResponseEntity<Event> update(@PathVariable("id") int id,
                                           @RequestBody Event event){
        return eventService.findById(id)
                .map(savedEvent -> {
                    savedEvent.setDescription(event.getDescription());
                    savedEvent.setUsers(event.getUsers());
                    savedEvent.setDateEvent(event.getDateEvent());

                    Event updatedEvent = eventService.update(savedEvent);
                    return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete (@PathVariable("id")int id) {
        eventService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PostMapping("{id}")
    public void sendMessageToUsers(@PathVariable int id, @RequestBody Event event) {
        eventService.findById(id);
        eventService.message(event);
    }
}
