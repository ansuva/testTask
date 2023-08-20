package com.example.demo1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Component
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "description")
    String description;
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @OneToMany(cascade = CascadeType.ALL)
    public List<User> users;
    @Column(name = "date_event")
    private String dateEvent;
    public Event(String description, String dateEvent) {
        this.description = description;
        this.dateEvent = LocalDateTime.now().toString();
    }

}


