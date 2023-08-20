package com.example.demo1.controller;

import com.example.demo1.model.User;
import com.example.demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user){
        return service.create(user);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findUserById (@PathVariable int id) {
        return (ResponseEntity<User>) service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public List<User> findAll () {
        return service.findAll();
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id,
                                                   @RequestBody User user){
        return service.findById(id)
                .map(savedUser -> {
                    savedUser.setName(user.getName());
                    savedUser.setDateUser(user.getDateUser());
                    savedUser.setStartHour(user.getStartHour());
                    savedUser.setEndHour(user.getEndHour());

                    User updatedUser = service.update(savedUser);
                    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete (@PathVariable("id")int id) {
        service.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}

