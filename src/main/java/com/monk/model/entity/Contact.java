package com.monk.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Entity
@Getter
@Setter
@Builder
@ToString
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long    id;
    private String  email;
    @Lob
    private String  message;
    private Date    createdAt;
    private boolean isResponded = false;
    private boolean isAdminNotified = false;
    private boolean hasAdminDismissed = false;
}
