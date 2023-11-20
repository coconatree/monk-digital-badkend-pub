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
public class FileRecordDTO {
    private long   id;
    private String filename;
    private String extensionType;
    private Date   createdAt;
}
