package com.example.demo1.controllerTest;

import com.example.demo1.controller.EventController;
import com.example.demo1.model.Event;
import com.example.demo1.model.User;
import com.example.demo1.service.EventServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventServiceImpl eventService;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;
    Instant instant = LocalDateTime.of(2023, 8, 29, 15,8,00).toInstant(ZoneOffset.UTC);
    Clock clock = Clock.fixed(instant, ZoneId.of("UTC"));

    //Тест проверяет создание события
    @Test
    public void createEventTest() throws Exception{

        Event event = Event.builder()
                .id(1)
                .description("произошла утечка масла во втором редукторе.")
                .dateEvent(LocalDateTime.now(clock).toString())
                .users(List.of(new User("Иванов Иван Иванович", 7, 16 , 17), new User("Степанов Степан Степанович", 7,17,18)))
                .build();
        given(eventService.create(any(Event.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)));

        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",
                        is(event.getId())))
                .andExpect(jsonPath("$.description",
                        is(event.getDescription())))
                .andExpect(jsonPath("$.dateEvent",
                        is(event.getDateEvent())))
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.users[0].name",
                        is(event.getUsers().get(0).getName())))
                .andExpect(jsonPath("$.users[1].name",
                        is(event.getUsers().get(1).getName())));
    }
    //Тест проверяет поиск всех событий
    @Test
    public void findAllTest() throws Exception{

        List<Event> listOfEvents = new ArrayList<>();
        listOfEvents.add(
                Event.builder()
                        .id(1)
                        .description("произошла утечка масла во втором редукторе.")
                        .dateEvent(LocalDateTime.now(clock).toString())
                        .users(List.of(new User("Иванов Иван Иванович", 7, 16 , 17), new User("Степанов Степан Степанович", 7,17,18)))
                        .build());
        listOfEvents.add(
                Event.builder()
                        .id(2)
                        .description("день рождение у Иванова Ивана Ивановича.")
                        .dateEvent(LocalDateTime.now(clock).toString())
                        .users(List.of(new User("Иванов Иван Иванович", 7, 16 , 17), new User("Степанов Степан Степанович", 7,17,18)))
                        .build());
        given(eventService.findAll()).willReturn(listOfEvents);

        ResultActions response = mockMvc.perform(get("/api/event"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfEvents.size())));
    }
    //Тест проверяет поиск события по Id
    @Test
    public void findByIdTest() throws Exception{

        int id = 1;
        Event event = Event.builder()
                .description("произошла утечка масла во втором редукторе.")
                .dateEvent(LocalDateTime.now(clock).toString())
                .users(List.of(new User("Иванов Иван Иванович", 7, 16 , 17), new User("Степанов Степан Степанович", 7,17,18)))
                .build();
        given(eventService.findById(id)).willReturn(Optional.of(event));

        ResultActions response = mockMvc.perform(get("/api/event/{id}", id));

        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(event.getId())))
                .andExpect(jsonPath("$.description",
                        is(event.getDescription())))
                .andExpect(jsonPath("$.dateEvent",
                        is(event.getDateEvent())))
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.users[0].name",
                        is(event.getUsers().get(0).getName())))
                .andExpect(jsonPath("$.users[1].name",
                        is(event.getUsers().get(1).getName())));
    }
    //Тест проверяет поиск события в случае, если Id пустой
    @Test
    public void findByIdTestEmpty() throws Exception{

        int id = 1;
        Event event = Event.builder()
                .description("событие1")
                .dateEvent(LocalDateTime.now(clock).toString())
                .users(List.of(new User("Иванов Иван Иванович", 7, 16 , 17), new User("Степанов Степан Степанович", 7,17,18)))
                .build();
        given(eventService.findById(id)).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/api/event/{id}", id));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }
    //Тест проверяет обновление события
    @Test
    public void updateUserTest() throws Exception{

        int id = 1;
        Event savedEvent = Event.builder()
                .description("произошла утечка масла во втором редукторе.")
                .dateEvent(LocalDateTime.now(clock).toString())
                .users(List.of(new User("Иванов Иван Иванович", 7, 16 , 17), new User("Степанов Степан Степанович", 7,17,18)))
                .build();

        Event updatedEvent = Event.builder()
                .description("день рождение у Иванова Ивана Ивановича.")
                .dateEvent(LocalDateTime.now(clock).toString())
                .users(List.of(new User("Иванов Иван Иванович", 7, 16 , 17), new User("Степанов Степан Степанович", 7,17,18)))
                .build();
        given(eventService.findById(id)).willReturn(Optional.of(savedEvent));
        given(eventService.update(any(Event.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/event/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEvent)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.description",
                        is(updatedEvent.getDescription())))
                .andExpect(jsonPath("$.dateEvent",
                        is(updatedEvent.getDateEvent())))
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.users[0].name",
                        is(updatedEvent.getUsers().get(0).getName())))
                .andExpect(jsonPath("$.users[1].name",
                        is(updatedEvent.getUsers().get(1).getName())));
    }
    //Тест проверяет обновление события в случае, если ошибка 404
    @Test
    public void updateEventTest404() throws Exception{

        int id = 1;
        Event savedEvent = Event.builder()
                .description("произошла утечка масла во втором редукторе.")
                .dateEvent(LocalDateTime.now(clock).toString())
                .users(List.of(new User("Иванов Иван Иванович", 7, 16 , 17), new User("Степанов Степан Степанович", 7,17,18)))
                .build();

        Event updatedEvent = Event.builder()
                .description("день рождение у Иванова Ивана Ивановича.")
                .dateEvent(LocalDateTime.now(clock).toString())
                .users(List.of(new User("Иванов Иван Иванович", 7, 16 , 17), new User("Степанов Степан Степанович", 7,17,18)))
                .build();
        given(eventService.findById(id)).willReturn(Optional.empty());
        given(eventService.update(any(Event.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/event/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEvent)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }
    //Тест проверяет удаление события
    @Test
    public void deleteTest() throws Exception{

        int id = 1;
        willDoNothing().given(eventService).delete(id);

        ResultActions response = mockMvc.perform(delete("/api/event/{id}", id));

        response.andExpect(status().isOk())
                .andDo(print());
    }
    //Тест проверяет работу контроллера по отправке сообщений
    @Test
    public void sendMessageToUsersTest() throws Exception{
        int id = 1;
        Event event = Event.builder()
                .description("произошла утечка масла во втором редукторе.")
                .dateEvent(LocalDateTime.now(clock).toString())
                .users(List.of(new User("Иванов Иван Иванович", 7, 16 , 17), new User("Степанов Степан Степанович", 7,17,18)))
                .build();

        ResultActions response = mockMvc.perform(post("/api/event/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk());

        verify(eventService).findById(id);
        verify(eventService).message(event);

        response.andExpect(status().isOk())
                .andDo(print());
    }
}





