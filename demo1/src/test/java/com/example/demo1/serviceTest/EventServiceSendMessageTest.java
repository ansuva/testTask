package com.example.demo1.serviceTest;

import com.example.demo1.model.Event;
import com.example.demo1.model.User;
import com.example.demo1.service.EventService;
import com.example.demo1.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.util.Calendar;

@SpringBootTest
public class EventServiceSendMessageTest {

    private UserService userService;

    private EventService eventService;

    @Autowired
    public EventServiceSendMessageTest(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    Calendar calendar = Calendar.getInstance();
    int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

    //Тест проверяет отправку сообщений
    @Test
        public void messageTest() {
        //Иванов Иван Иванович не должен получить сообщение в этом тесте, тк удобный день недели для него другой.
         User user = User.builder()
                 .name("Иванов Иван Иванович")
                 .dateUser(currentDay+1)
                 .startHour(currentHour)
                 .endHour(currentHour)
                 .build();
         userService.create(user);
        //Степанов Степан Степанович должен получить сообщение.
         User user2 = User.builder()
                 .name("Степанов Степан Степанович")
                 .dateUser(currentDay)
                 .startHour(currentHour)
                 .endHour(currentHour)
                 .build();
         userService.create(user2);
         Event event = Event.builder()
                 .description("произошла утечка масла во втором редукторе.")
                 .dateEvent(LocalDateTime.now().toString())
                 .build();
         eventService.create(event);
         eventService.message(event);
        //В логах должно появится сообщение
         userService.delete(user.getId());
         userService.delete(user2.getId());
        //Пользователи удаляются, тк больше они не нужны
     }
    //Тест проверяет отправку сообщений, если пустой список
    @Test
    public void messageTestEmptyList() {
        Event event = Event.builder()
                .description("произошла утечка масла во втором редукторе.")
                .dateEvent(LocalDateTime.now().toString())
                .build();
        eventService.create(event);
        eventService.message(event);
        //В логах должно появится сообщение
    }
}