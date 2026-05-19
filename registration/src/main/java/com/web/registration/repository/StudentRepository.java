package com.web.registration.repository;

import com.web.registration.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByStudentCode(String studentCode);
}
