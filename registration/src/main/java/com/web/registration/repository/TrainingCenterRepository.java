package com.web.registration.repository;

import com.web.registration.entity.TrainingCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingCenterRepository extends JpaRepository<TrainingCenter, Long> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);
}
