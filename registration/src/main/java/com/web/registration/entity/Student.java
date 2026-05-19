package com.web.registration.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "student_code", unique = true, nullable = false, length = 20)
    private String studentCode;

    @Column(length = 100)
    private String major;

    @Column(name = "class_name", length = 50)
    private String className;

    @Column(precision = 3, scale = 2)
    private BigDecimal gpa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_center_id", nullable = false)
    private TrainingCenter trainingCenter;
}
