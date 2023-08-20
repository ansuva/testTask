package com.example.demo1.service;

import com.example.demo1.model.User;
import com.example.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Transactional
    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Transactional
    @Override
    public User update(User updatedUser) {
        return repository.save(updatedUser);
    }

    @Transactional
    @Override
    public Optional<User> findById(int id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    public boolean checkNotificationPeriod(int startHour, int endHour, int dateUser) {
        // Получаем текущее время
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        // Проверяем, находится ли текущее время в периоде информирования
        if (currentHour >= startHour && currentHour <= endHour && dateUser == currentDay) {
            return true;
            // Текущее время находится в периоде информирования
        } else {
            return false;
            // Текущее время не находится в периоде информирования
        }
    }


}
