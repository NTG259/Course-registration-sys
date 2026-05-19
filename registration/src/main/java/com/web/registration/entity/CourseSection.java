package com.web.registration.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "section_code", unique = true, nullable = false, length = 30)
    private String sectionCode;

    @Column(nullable = false, length = 20)
    private String semester;

    @Column(name = "academic_year", nullable = false, length = 20)
    private String academicYear;

    @Column(name = "max_students")
    @Builder.Default
    private Integer maxStudents = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_center_id")
    private TrainingCenter trainingCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
