package com.example.demo1.serviceTest;

import com.example.demo1.model.Event;
import com.example.demo1.repository.EventRepository;
import com.example.demo1.service.EventServiceImpl;



import com.example.demo1.service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private EventServiceImpl eventService;
    Instant instant = LocalDateTime.of(2023, 8, 29, 15,8,00).toInstant(ZoneOffset.UTC);
    Clock clock = Clock.fixed(instant, ZoneId.of("UTC"));
    private Event event;

    @BeforeEach
    public void setup() {

        event = Event.builder()
                .id(1)
                .description("произошла утечка масла во втором редукторе.")
                .dateEvent(LocalDateTime.now(clock).toString())
                .build();
    }
    //Проверяет создание события
    @Test
    public void createEventTest (){
        given(eventRepository.save(event)).willReturn(event);
        System.out.println(eventRepository);
        System.out.println(eventService);
        Event savedEvent = eventService.create(event);
        System.out.println("Сохраненный результат: " + savedEvent);
        assertThat(savedEvent).isNotNull();
    }
    //Проверяет получение всех событий
    @Test
    public void findAllEventTest(){
        Event event1 = Event.builder()
                .description("день рождение у Иванова Ивана Ивановича.")
                .dateEvent(LocalDateTime.now(clock).toString())
                .build();
        given(eventRepository.findAll()).willReturn(List.of(event,event1));
        List<Event> eventList = eventService.findAll();
        assertThat(eventList).isNotNull();
        assertThat(eventList.size()).isEqualTo(2);
    }

    //Проверяет получение всех событий в случае, если вернулся пустой список
    @Test
    public void findAllEventTestEmpty(){
        Event event1 = Event.builder()
                .description("день рождение у Иванова Ивана Ивановича.")
                .dateEvent(LocalDateTime.now(clock).toString())
                .build();
        given(eventRepository.findAll()).willReturn(Collections.emptyList());
        List<Event> eventList = eventService.findAll();
        assertThat(eventList).isEmpty();
        assertThat(eventList.size()).isEqualTo(0);
    }
    //Получает событие по id
    @Test
    public void findByIdTest(){
        given(eventRepository.findById(1)).willReturn(Optional.of(event));
        Event savedEvent = eventService.findById(event.getId()).get();
        assertThat(savedEvent).isNotNull();
    }
    //Обновляет событие
    @Test
    public void updateEventTest(){
        given(eventRepository.save(event)).willReturn(event);
        event.setDescription("день рождение у Иванова Ивана Ивановича.");
        Event updatedEmployee = eventService.update(event);
        assertThat(updatedEmployee.getDescription()).isEqualTo("день рождение у Иванова Ивана Ивановича.");

    }
    //Удаляет событие
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        int id = 1;
        willDoNothing().given(eventRepository).deleteById(id);
        eventService.delete(id);
        verify(eventRepository, times(1)).deleteById(id);
    }
}

