package com.web.registration.repository;

import com.web.registration.entity.Admin;
import com.web.registration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUser(User user);

    boolean existsByUser(User user);
}
