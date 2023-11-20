package com.monk.model.request;

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
public class NewHttpProjectNotificationRequest {
    private Date usedAt;
}
