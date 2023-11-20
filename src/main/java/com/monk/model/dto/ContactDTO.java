package com.monk.model.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Getter
@Setter
@Builder
@ToString
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    private long    id;
    private String  email;
    private String  message;
    private Date    createdAt;
    private boolean isResponded = false;
}
