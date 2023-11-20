package com.monk.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NewPhoneMessageRequest {
    private String countryCode;
    private String number;
    private String message;
}
