package com.monk.model.pojo;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@ToString
@Component
@NoArgsConstructor
@AllArgsConstructor
public class MonkApiResponse {
    private long   status;
    private String message;
    private Object data;
}
