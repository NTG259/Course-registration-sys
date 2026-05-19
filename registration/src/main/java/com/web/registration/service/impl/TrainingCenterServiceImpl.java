package com.web.registration.service.impl;

import com.web.registration.dto.request.TrainingCenterRequest;
import com.web.registration.dto.response.TrainingCenterResponse;
import com.web.registration.entity.TrainingCenter;
import com.web.registration.exception.BusinessException;
import com.web.registration.exception.ErrorCode;
import com.web.registration.repository.TrainingCenterRepository;
import com.web.registration.service.TrainingCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrainingCenterServiceImpl implements TrainingCenterService {

    private final TrainingCenterRepository repository;

    @Override
    public List<TrainingCenterResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TrainingCenterResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional
    public TrainingCenterResponse create(TrainingCenterRequest request) {
        if (repository.existsByCode(request.getCode())) {
            throw new BusinessException(ErrorCode.TRAINING_CENTER_CODE_DUPLICATED);
        }
        TrainingCenter entity = TrainingCenter.builder()
                .code(request.getCode())
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
        return toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public TrainingCenterResponse update(Long id, TrainingCenterRequest request) {
        TrainingCenter entity = findOrThrow(id);
        if (repository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new BusinessException(ErrorCode.TRAINING_CENTER_CODE_DUPLICATED);
        }
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setAddress(request.getAddress());
        entity.setPhone(request.getPhone());
        entity.setEmail(request.getEmail());
        return toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findOrThrow(id);
        repository.deleteById(id);
    }

    private TrainingCenter findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRAINING_CENTER_NOT_FOUND));
    }

    private TrainingCenterResponse toResponse(TrainingCenter tc) {
        return new TrainingCenterResponse(
                tc.getId(),
                tc.getCode(),
                tc.getName(),
                tc.getAddress(),
                tc.getPhone(),
                tc.getEmail(),
                tc.getCreatedAt()
        );
    }
}
