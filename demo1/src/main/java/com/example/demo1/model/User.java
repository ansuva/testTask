package com.example.demo1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Min(value = 1, message = "Укажите, пожалуйста, номер дня недели от 1 до 7. Нумерация дней недели идет с воскресенья, соответственно воскресенье равно 1 и тд.")
    @Max(value = 7, message = "Укажите, пожалуйста, номер дня недели от 1 до 7. Нумерация дней недели идет с воскресенья, соответственно воскресенье равно 1 и тд.")
    private int dateUser;

    @Min(value = 0, message = "Укажите, пожалуйста, время в часах от 0 до 23")
    @Max(value = 23, message = "Укажите, пожалуйста, время в часах от 0 до 23")
    private int startHour;

    @Min(value = 0, message = "Укажите, пожалуйста, время в часах от 0 до 23")
    @Max(value = 23, message = "Укажите, пожалуйста, время в часах от 0 до 23")
    private int endHour;
    private boolean notified = false;

    public User(String name, int dateUser, int startHour, int endHour) {
        this.name = name;
        this.dateUser = dateUser;
        this.startHour = startHour;
        this.endHour = endHour;

    }
}
