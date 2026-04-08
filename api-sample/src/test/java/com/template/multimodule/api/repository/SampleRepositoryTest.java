package com.template.multimodule.api.repository;

import com.template.multimodule.domain.sample.SampleEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SampleRepositoryTest {

    @Autowired
    private SampleRepository sampleRepository;

    @Test
    @DisplayName("샘플 저장 및 조회")
    void saveAndFind() {
        // given
        SampleEntity entity = SampleEntity.builder()
                .name("test-name")
                .description("test-description")
                .build();

        // when
        SampleEntity saved = sampleRepository.save(entity);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("test-name");
        assertThat(saved.getDescription()).isEqualTo("test-description");
    }

    @Test
    @DisplayName("샘플 삭제")
    void delete() {
        // given
        SampleEntity entity = SampleEntity.builder()
                .name("to-delete")
                .description("desc")
                .build();
        SampleEntity saved = sampleRepository.save(entity);

        // when
        sampleRepository.deleteById(saved.getId());

        // then
        Optional<SampleEntity> found = sampleRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 ID 조회")
    void findById_notFound() {
        // when
        Optional<SampleEntity> result = sampleRepository.findById(999L);

        // then
        assertThat(result).isEmpty();
    }
}
