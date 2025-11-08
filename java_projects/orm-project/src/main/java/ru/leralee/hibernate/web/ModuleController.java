package ru.leralee.hibernate.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.leralee.hibernate.dto.request.ModuleRequest;
import ru.leralee.hibernate.dto.response.ModuleResponse;
import ru.leralee.hibernate.service.ModuleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/modules")
@Validated
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping
    public ModuleResponse create(@Valid @RequestBody ModuleRequest request) {
        return moduleService.create(request);
    }

    @GetMapping("/course/{courseId}")
    public List<ModuleResponse> getByCourse(@PathVariable UUID courseId) {
        return moduleService.findByCourseId(courseId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}")
    public ModuleResponse update(@PathVariable UUID id, @Valid @RequestBody ModuleRequest request) {
        return moduleService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        moduleService.delete(id);
    }
}
