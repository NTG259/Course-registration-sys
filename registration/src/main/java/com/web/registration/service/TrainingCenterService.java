package com.web.registration.service;

import com.web.registration.dto.request.TrainingCenterRequest;
import com.web.registration.dto.response.TrainingCenterResponse;

import java.util.List;

public interface TrainingCenterService {

    List<TrainingCenterResponse> getAll();

    TrainingCenterResponse getById(Long id);

    TrainingCenterResponse create(TrainingCenterRequest request);

    TrainingCenterResponse update(Long id, TrainingCenterRequest request);

    void delete(Long id);
}
