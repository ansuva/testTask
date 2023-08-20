package com.example.demo1.service;

import com.example.demo1.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User create(User user);
    void delete(int id);
    User update(User updatedUser);
    Optional<User> findById(int id);
    List<User> findAll();
    boolean checkNotificationPeriod(int startHour, int endHour, int dateUser);


}
