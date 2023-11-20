package com.monk.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NewVoiceoverRequest {
    private String              text;
    private String              model_id;
    private VoiceSettingRequest voice_settings;
}
