package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leralee.hibernate.dto.request.ModuleRequest;
import ru.leralee.hibernate.dto.response.ModuleResponse;
import ru.leralee.hibernate.entity.Course;
import ru.leralee.hibernate.entity.CourseModule;
import ru.leralee.hibernate.exception.NotFoundException;
import ru.leralee.hibernate.mapper.ModuleMapper;
import ru.leralee.hibernate.repository.CourseModuleRepository;
import ru.leralee.hibernate.repository.CourseRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class ModuleService {

    private final CourseModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final ModuleMapper moduleMapper;

    @Transactional
    public ModuleResponse create(ModuleRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + request.getCourseId()));

        CourseModule module = moduleMapper.toEntity(request);
        module.setCourse(course);

        CourseModule saved = moduleRepository.save(module);
        return moduleMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ModuleResponse findById(UUID id) {
        return moduleMapper.toResponse(getModule(id));
    }

    @Transactional(readOnly = true)
    public List<ModuleResponse> findByCourseId(UUID courseId) {
        return moduleMapper.toResponseList(moduleRepository.findByCourseIdOrderByOrderIndexAsc(courseId));
    }

    @Transactional
    public ModuleResponse update(UUID id, ModuleRequest request) {
        CourseModule module = getModule(id);
        moduleMapper.updateEntityFromRequest(request, module);
        if (request.getCourseId() != null && !request.getCourseId().equals(module.getCourse().getId())) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new NotFoundException("Course not found with id: " + request.getCourseId()));
            module.setCourse(course);
        }
        return moduleMapper.toResponse(moduleRepository.save(module));
    }

    @Transactional
    public void delete(UUID id) {
        moduleRepository.deleteById(id);
    }

    private CourseModule getModule(UUID id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Module not found with id: " + id));
    }
}
