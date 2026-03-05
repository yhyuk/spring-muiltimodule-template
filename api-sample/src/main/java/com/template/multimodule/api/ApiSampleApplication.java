package com.template.multimodule.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.template.multimodule.api",
        "com.template.multimodule.common"
})
@EntityScan(basePackages = "com.template.multimodule.domain")
@EnableJpaRepositories(basePackages = "com.template.multimodule.api.repository")
public class ApiSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSampleApplication.class, args);
    }
}
