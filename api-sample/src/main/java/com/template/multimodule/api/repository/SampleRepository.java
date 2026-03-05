package com.template.multimodule.api.repository;

import com.template.multimodule.domain.sample.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends JpaRepository<SampleEntity, Long> {
}
