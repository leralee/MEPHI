package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leralee.hibernate.dto.request.AssignmentRequest;
import ru.leralee.hibernate.dto.response.AssignmentResponse;
import ru.leralee.hibernate.entity.Assignment;
import ru.leralee.hibernate.entity.Lesson;
import ru.leralee.hibernate.exception.NotFoundException;
import ru.leralee.hibernate.mapper.AssignmentMapper;
import ru.leralee.hibernate.repository.AssignmentRepository;
import ru.leralee.hibernate.repository.LessonRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;
    private final AssignmentMapper assignmentMapper;

    @Transactional
    public AssignmentResponse create(AssignmentRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new NotFoundException("Lesson not found with id: " + request.getLessonId()));

        Assignment assignment = assignmentMapper.toEntity(request);
        assignment.setLesson(lesson);

        Assignment saved = assignmentRepository.save(assignment);
        return assignmentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AssignmentResponse findById(UUID id) {
        return assignmentMapper.toResponse(getAssignment(id));
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> findByLesson(UUID lessonId) {
        return assignmentMapper.toResponseList(assignmentRepository.findByLessonId(lessonId));
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> findByCourse(UUID courseId) {
        return assignmentMapper.toResponseList(assignmentRepository.findByLesson_Module_Course_Id(courseId));
    }

    @Transactional
    public AssignmentResponse update(UUID id, AssignmentRequest request) {
        Assignment assignment = getAssignment(id);
        assignmentMapper.updateEntityFromRequest(request, assignment);
        if (request.getLessonId() != null && !request.getLessonId().equals(assignment.getLesson().getId())) {
            Lesson lesson = lessonRepository.findById(request.getLessonId())
                    .orElseThrow(() -> new NotFoundException("Lesson not found with id: " + request.getLessonId()));
            assignment.setLesson(lesson);
        }
        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Transactional
    public void delete(UUID id) {
        assignmentRepository.deleteById(id);
    }

    private Assignment getAssignment(UUID id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment not found with id: " + id));
    }
}
