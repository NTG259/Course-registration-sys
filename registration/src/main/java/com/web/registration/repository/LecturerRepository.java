package com.web.registration.repository;

import com.web.registration.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {

    boolean existsByLecturerCode(String lecturerCode);
}
