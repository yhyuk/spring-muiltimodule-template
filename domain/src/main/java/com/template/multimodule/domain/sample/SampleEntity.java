package com.template.multimodule.domain.sample;

import com.template.multimodule.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sample")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Builder
    public SampleEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
