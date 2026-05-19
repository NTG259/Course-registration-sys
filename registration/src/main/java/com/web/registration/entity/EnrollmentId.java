package com.web.registration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EnrollmentId implements Serializable {

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "course_section_id")
    private Long courseSectionId;
}
