package com.template.multimodule.api.service;

import com.template.multimodule.api.dto.SampleRequest;
import com.template.multimodule.api.dto.SampleResponse;
import com.template.multimodule.api.repository.SampleRepository;
import com.template.multimodule.common.exception.BusinessException;
import com.template.multimodule.common.exception.ErrorCode;
import com.template.multimodule.domain.sample.SampleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SampleService {

    private final SampleRepository sampleRepository;

    public List<SampleResponse> findAll() {
        return sampleRepository.findAll().stream()
                .map(SampleResponse::from)
                .collect(Collectors.toList());
    }

    public SampleResponse findById(Long id) {
        SampleEntity entity = sampleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SAMPLE_NOT_FOUND));
        return SampleResponse.from(entity);
    }

    @Transactional
    public SampleResponse create(SampleRequest request) {
        SampleEntity entity = SampleEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        SampleEntity saved = sampleRepository.save(entity);
        log.info("Sample created: {}", saved.getId());

        return SampleResponse.from(saved);
    }

    @Transactional
    public SampleResponse update(Long id, SampleRequest request) {
        SampleEntity entity = sampleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SAMPLE_NOT_FOUND));

        entity.updateName(request.getName());
        entity.updateDescription(request.getDescription());

        log.info("Sample updated: {}", id);

        return SampleResponse.from(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!sampleRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.SAMPLE_NOT_FOUND);
        }

        sampleRepository.deleteById(id);
        log.info("Sample deleted: {}", id);
    }
}
