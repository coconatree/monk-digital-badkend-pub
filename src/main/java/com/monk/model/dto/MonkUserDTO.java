package com.monk.model.dto;

import com.monk.model.pojo.EMonkRole;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MonkUserDTO {
    private Long    id;
    private String  email;
    private boolean isEmailValidated;
    private boolean isGoogleAccountConnected;
    private boolean isFacebookAccountConnected;
    private boolean isLinkedinAccountConnected;
    private Date    createdAt;
    private String  role;
}
