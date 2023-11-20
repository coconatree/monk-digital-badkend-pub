package com.monk.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class VoiceSettingRequest {
    private double stability;
    private double similarity_boost;
}
