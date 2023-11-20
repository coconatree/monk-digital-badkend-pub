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
public class MonkTokenDTO {
    private String issuer;
    private String token;
    private Date   expirationDate;
    private Date   issuedAt;
}

