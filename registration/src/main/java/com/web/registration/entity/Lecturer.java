package com.web.registration.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lecturers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "lecturer_code", unique = true, nullable = false, length = 20)
    private String lecturerCode;

    @Column(length = 100)
    private String department;

    @Column(name = "academic_rank", length = 100)
    private String academicRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_center_id", nullable = false)
    private TrainingCenter trainingCenter;
}
