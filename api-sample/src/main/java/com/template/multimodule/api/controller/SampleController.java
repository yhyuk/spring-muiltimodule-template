package com.template.multimodule.api.controller;

import com.template.multimodule.api.dto.SampleRequest;
import com.template.multimodule.api.dto.SampleResponse;
import com.template.multimodule.api.service.SampleService;
import com.template.multimodule.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Sample", description = "Sample API")
@RestController
@RequestMapping("/api/samples")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @Operation(summary = "Get all samples")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SampleResponse>>> findAll() {
        List<SampleResponse> samples = sampleService.findAll();
        return ResponseEntity.ok(ApiResponse.success(samples));
    }

    @Operation(summary = "Get sample by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SampleResponse>> findById(@PathVariable Long id) {
        SampleResponse sample = sampleService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(sample));
    }

    @Operation(summary = "Create sample")
    @PostMapping
    public ResponseEntity<ApiResponse<SampleResponse>> create(@Valid @RequestBody SampleRequest request) {
        SampleResponse sample = sampleService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(sample, "Sample created successfully"));
    }

    @Operation(summary = "Update sample")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SampleResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody SampleRequest request) {
        SampleResponse sample = sampleService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(sample, "Sample updated successfully"));
    }

    @Operation(summary = "Delete sample")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        sampleService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Sample deleted successfully"));
    }
}
