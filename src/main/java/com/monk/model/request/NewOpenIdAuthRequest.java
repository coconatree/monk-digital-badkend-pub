package com.monk.model.request;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@ToString
@Component
@NoArgsConstructor
@AllArgsConstructor
public class NewOpenIdAuthRequest {
    private String token;
}
