package com.template.multimodule.api.service;

import com.template.multimodule.api.dto.SampleRequest;
import com.template.multimodule.api.dto.SampleResponse;
import com.template.multimodule.api.repository.SampleRepository;
import com.template.multimodule.common.exception.BusinessException;
import com.template.multimodule.domain.sample.SampleEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SampleServiceTest {

    @InjectMocks
    private SampleService sampleService;

    @Mock
    private SampleRepository sampleRepository;

    @Test
    @DisplayName("전체 샘플 조회")
    void findAll() {
        // given
        SampleEntity entity = SampleEntity.builder()
                .name("test")
                .description("desc")
                .build();
        given(sampleRepository.findAll()).willReturn(List.of(entity));

        // when
        List<SampleResponse> result = sampleService.findAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("test");
    }

    @Test
    @DisplayName("ID로 샘플 조회 - 존재하는 경우")
    void findById_success() {
        // given
        SampleEntity entity = SampleEntity.builder()
                .name("test")
                .description("desc")
                .build();
        given(sampleRepository.findById(1L)).willReturn(Optional.of(entity));

        // when
        SampleResponse result = sampleService.findById(1L);

        // then
        assertThat(result.getName()).isEqualTo("test");
    }

    @Test
    @DisplayName("ID로 샘플 조회 - 존재하지 않는 경우")
    void findById_notFound() {
        // given
        given(sampleRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> sampleService.findById(1L))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("샘플 생성")
    void create() {
        // given
        SampleRequest request = new SampleRequest();
        SampleEntity entity = SampleEntity.builder()
                .name("test")
                .description("desc")
                .build();
        given(sampleRepository.save(any(SampleEntity.class))).willReturn(entity);

        // when
        SampleResponse result = sampleService.create(request);

        // then
        assertThat(result).isNotNull();
        verify(sampleRepository).save(any(SampleEntity.class));
    }

    @Test
    @DisplayName("샘플 삭제 - 존재하지 않는 경우")
    void delete_notFound() {
        // given
        given(sampleRepository.existsById(1L)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> sampleService.delete(1L))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("샘플 삭제 - 성공")
    void delete_success() {
        // given
        given(sampleRepository.existsById(1L)).willReturn(true);

        // when
        sampleService.delete(1L);

        // then
        verify(sampleRepository).deleteById(1L);
    }
}
