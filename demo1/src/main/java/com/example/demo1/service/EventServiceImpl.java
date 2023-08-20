package com.example.demo1.service;

import com.example.demo1.model.Event;
import com.example.demo1.model.User;
import com.example.demo1.repository.EventRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@org.springframework.stereotype.Service
@EnableScheduling
@Log
public class EventServiceImpl implements EventService{
    private final EventRepository repository;
    private final UserService userService;
    private final User user;
    private ExecutorService executorService;

    @Autowired
    public EventServiceImpl(EventRepository repository, UserService userService, User user) {
        this.repository = repository;
        this.userService = userService;
        this.user = user;
        this.executorService = Executors.newCachedThreadPool();

    }
    @Transactional
    @Override
    public Event create (Event event){
        event.setUsers(userService.findAll());
        return repository.save(event);
    }

    @Transactional
    @Override
    public Event update(Event event) {
        return repository.save(event);
    }
    @Transactional
    @Override
    public void delete (int id) {
        repository.deleteById(id);
    }
    @Transactional
    @Override
    public List<Event> findAll() {
        return repository.findAll();
    }
    @Transactional
    @Override
    public Optional<Event> findById(int id) {
        return repository.findById(id);
    }

    //Метод message создает для каждого события свой поток.
    //Если событие прошло проверки, то пользователь получит сообщение.
    //Если нет, то оно будет повторно проходить проверки, пока не пройдет.
    public void message(Event event) {
        List<User> users = event.getUsers();
        if(!users.isEmpty()){
            for (User user : users) {
                executorService.execute(() -> {
                    while (!user.isNotified()) {
                        if (!user.isNotified() && userService.checkNotificationPeriod(user.getStartHour(), user.getEndHour(), user.getDateUser())) {
                            log.info(event.getDateEvent() + " Пользователю " + user.getName() + " отправлено оповещение с текстом: " + event.getDescription());
                            user.setNotified(true);
                        }
                    }
                });
            }
        } else {
            log.info("Список пользователей пустой. Сообщение не было отправлено. Название сообщения: " + event.getDescription() +"Время: " + event.getDateEvent());
        }
    }
}



