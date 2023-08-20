package com.example.demo1.controllerTest;

import com.example.demo1.controller.UserController;
import com.example.demo1.model.User;
import com.example.demo1.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl service;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    //Тест проверяет создание пользователя
    @Test
    public void createUserTest() throws Exception{

        User user = User.builder()
                .id(1)
                .name("Иванов Иван Иванович")
                .dateUser(2)
                .startHour(2)
                .endHour(4)
                        .build();
        given(service.create(any(User.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",
                        is(user.getId())))
                .andExpect(jsonPath("$.name",
                        is(user.getName())))
                .andExpect(jsonPath("$.dateUser",
                        is(user.getDateUser())))
                .andExpect(jsonPath("$.startHour",
                        is(user.getStartHour())))
                .andExpect(jsonPath("$.endHour",
                is(user.getEndHour())));
    }
    //Тест проверяет получение всех пользователей
    @Test
    public void findAllTest() throws Exception{

        List<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(
                User.builder()
                        .id(1)
                        .name("Иванов Иван Иванович")
                        .dateUser(2)
                        .startHour(2)
                        .endHour(4)
                            .build());
        listOfUsers.add(
                User.builder()
                        .id(2)
                        .name("Степанов Степан Степанович")
                        .dateUser(2)
                        .startHour(2)
                        .endHour(4)
                        .build());
        given(service.findAll()).willReturn(listOfUsers);

        ResultActions response = mockMvc.perform(get("/api/user"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfUsers.size())));
    }
    //Тест проверяет получение пользователя по Id
    @Test
    public void findByIdTest() throws Exception{

        int id = 1;
        User user = User.builder()
                .name("Иванов Иван Иванович")
                .dateUser(2)
                .startHour(2)
                .endHour(4)
                    .build();
        given(service.findById(id)).willReturn(Optional.of(user));

        ResultActions response = mockMvc.perform(get("/api/user/{id}", id));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.dateUser", is(user.getDateUser())))
                .andExpect(jsonPath("$.startHour", is(user.getStartHour())))
                .andExpect(jsonPath("$.endHour", is(user.getEndHour())));
    }
    //Тест проверяет получение пользователя по Id в случае, если Id пустое
    @Test
    public void findByIdTestEmpty() throws Exception{

        int id = 1;
        User user = User.builder()
                .name("Иванов Иван Иванович")
                .dateUser(2)
                .startHour(2)
                .endHour(4)
                .build();
        given(service.findById(id)).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/api/user/{id}", id));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }
    //Тест проверяет обновление пользователя
    @Test
    public void updateUserTest() throws Exception{

        int id = 1;
        User savedUser = User.builder()
                .name("Иванов Иван Иванович")
                .dateUser(2)
                .startHour(2)
                .endHour(4)
                .build();

        User updatedUser = User.builder()
                .name("Степанов Степан Степанович")
                .dateUser(2)
                .startHour(2)
                .endHour(4)
                .build();
        given(service.findById(id)).willReturn(Optional.of(savedUser));
        given(service.update(any(User.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(updatedUser.getName())))
                .andExpect(jsonPath("$.dateUser", is(updatedUser.getDateUser())))
                .andExpect(jsonPath("$.startHour", is(updatedUser.getStartHour())))
                .andExpect(jsonPath("$.endHour", is(updatedUser.getEndHour())));
    }

    // Тест проверяет обновление пользователя в случае, если ошибка 404
    @Test
    public void updateUserTest404() throws Exception{

        int id = 1;
        User savedUser = User.builder()
                .name("Иванов Иван Иванович")
                .dateUser(2)
                .startHour(2)
                .endHour(4)
                .build();

        User updatedUser = User.builder()
                .name("Степанов Степан Степанович")
                .dateUser(2)
                .startHour(2)
                .endHour(4)
                .build();
        given(service.findById(id)).willReturn(Optional.empty());
        given(service.update(any(User.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }
    //Тест проверяет удаление пользователя
    @Test
    public void deleteTest() throws Exception{

        int id = 1;
        willDoNothing().given(service).delete(id);

        ResultActions response = mockMvc.perform(delete("/api/user/{id}", id));

        response.andExpect(status().isOk())
                .andDo(print());
    }
}
