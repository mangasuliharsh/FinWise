package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportName;
    private String reportType;
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
