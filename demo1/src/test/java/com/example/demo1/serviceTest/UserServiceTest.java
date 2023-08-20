package com.example.demo1.serviceTest;

import com.example.demo1.model.User;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1)
                .name("Иванов Иван Иванович")
                .dateUser(2)
                .startHour(2)
                .endHour(4)
                .build();
    }
    //Проверяет создание пользователя
    @Test
    public void createUserTest (){
        given(userRepository.save(user)).willReturn(user);
        System.out.println(userRepository);
        System.out.println(userService);
        User savedEvent = userService.create(user);
        System.out.println("Сохраненный результат: " + savedEvent);
        assertThat(savedEvent).isNotNull();
    }
    //Проверяет получение всех пользователей
    @Test
    public void findAllEventTest(){
        User user1 = User.builder()
                .id(2)
                .name("Степанов Степан Степанович")
                .dateUser(2)
                .startHour(2)
                .endHour(4)
                .build();
        given(userRepository.findAll()).willReturn(List.of(user,user1));
        List<User> eventList = userService.findAll();
        assertThat(eventList).isNotNull();
        assertThat(eventList.size()).isEqualTo(2);
    }

    //Проверяет получение всех пользователей в случае, если вернулся пустой список
    @Test
    public void findAllEventTestEmpty(){
        User user1 = User.builder()
                .id(2)
                .name("Степанов Степан Степанович")
                .dateUser(2)
                .startHour(2)
                .endHour(4)
                .build();
        given(userRepository.findAll()).willReturn(Collections.emptyList());
        List<User> eventList = userService.findAll();
        assertThat(eventList).isEmpty();
        assertThat(eventList.size()).isEqualTo(0);
    }
    //Получает пользователя по id
    @Test
    public void findByIdTest(){
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        User savedEvent = userService.findById(user.getId()).get();
        assertThat(savedEvent).isNotNull();
    }
    //Обновляет пользователя
    @Test
    public void updateEventTest(){
        given(userRepository.save(user)).willReturn(user);
        user.setName("Степанов Степан Степанович");
        User updatedEmployee = userService.update(user);
        assertThat(updatedEmployee.getName()).isEqualTo("Степанов Степан Степанович");
    }
    //Удаляет пользователя
    @Test
    public void deleteUserTest(){
        int id = 1;
        willDoNothing().given(userRepository).deleteById(id);
        userService.delete(id);
        verify(userRepository, times(1)).deleteById(id);
    }
}
