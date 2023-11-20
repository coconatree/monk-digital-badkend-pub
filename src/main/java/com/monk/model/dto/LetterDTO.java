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
public class LetterDTO {
    private Long   id;
    private String content;
    private Date   createdAt;
}
