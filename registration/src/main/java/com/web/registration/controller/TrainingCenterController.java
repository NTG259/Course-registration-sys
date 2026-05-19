package com.web.registration.controller;

import com.web.registration.dto.request.TrainingCenterRequest;
import com.web.registration.dto.response.TrainingCenterResponse;
import com.web.registration.service.TrainingCenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/training-centers")
@RequiredArgsConstructor
public class TrainingCenterController {

    private final TrainingCenterService service;

    @GetMapping
    public ResponseEntity<List<TrainingCenterResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingCenterResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainingCenterResponse> create(@Valid @RequestBody TrainingCenterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainingCenterResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TrainingCenterRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
