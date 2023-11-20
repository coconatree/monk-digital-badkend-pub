package com.monk.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Date;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long   id;
    private String filename;
    private String extensionType;
    private Date   createdAt;
    private Date   updatedAt;
}
