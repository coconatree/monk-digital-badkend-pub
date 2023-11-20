package com.monk.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Letter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long    id;
    private String  title;
    @Lob
    private String  content;
    @Lob
    private String  prompt;
    private Date    createdAt;
    private Date    publishedAt;

    @OneToOne
    private FileRecord voiceover;
}
