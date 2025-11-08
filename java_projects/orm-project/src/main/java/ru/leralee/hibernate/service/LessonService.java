package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leralee.hibernate.dto.request.LessonRequest;
import ru.leralee.hibernate.dto.response.LessonResponse;
import ru.leralee.hibernate.entity.Course;
import ru.leralee.hibernate.entity.CourseModule;
import ru.leralee.hibernate.entity.Lesson;
import ru.leralee.hibernate.exception.AppException;
import ru.leralee.hibernate.exception.NotFoundException;
import ru.leralee.hibernate.mapper.LessonMapper;
import ru.leralee.hibernate.repository.CourseModuleRepository;
import ru.leralee.hibernate.repository.CourseRepository;
import ru.leralee.hibernate.repository.LessonRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final CourseModuleRepository moduleRepository;
    private final LessonMapper lessonMapper;

    @Transactional
    public LessonResponse create(LessonRequest request) {
        if (request.getCourseId() == null || request.getModuleId() == null) {
            throw new AppException("CourseId and moduleId are required to create a lesson", 400);
        }
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + request.getCourseId()));
        CourseModule module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new NotFoundException("Module not found with id: " + request.getModuleId()));

        if (!module.getCourse().getId().equals(course.getId())) {
            throw new AppException("Module does not belong to course", 400);
        }

        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setModule(module);

        module.getLessons().add(lesson);

        Lesson saved = lessonRepository.save(lesson);
        return lessonMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public LessonResponse findById(UUID id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson not found with id: " + id));
        return lessonMapper.toResponse(lesson);
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> findAll() {
        List<Lesson> lessons = lessonRepository.findAll();
        return lessonMapper.toResponseList(lessons);
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> findByCourseId(UUID courseId) {
        List<Lesson> lessons = lessonRepository.findByModule_Course_Id(courseId);
        return lessonMapper.toResponseList(lessons);
    }

    @Transactional
    public LessonResponse update(UUID id, LessonRequest request) {
        Lesson lesson = getLesson(id);
        lessonMapper.updateEntityFromRequest(request, lesson);

        if (request.getModuleId() != null) {
            CourseModule module = moduleRepository.findById(request.getModuleId())
                    .orElseThrow(() -> new NotFoundException("Module not found with id: " + request.getModuleId()));
            lesson.setModule(module);
        }

        Lesson updated = lessonRepository.save(lesson);
        return lessonMapper.toResponse(updated);
    }

    @Transactional
    public void addResource(UUID lessonId, String resourceUrl) {
        Lesson lesson = getLesson(lessonId);
        lesson.getResources().add(resourceUrl);
    }

    @Transactional
    public void addResourceAtPosition(UUID lessonId, String resourceUrl, int position) {
        Lesson lesson = getLesson(lessonId);
        if (position < 0 || position > lesson.getResources().size()) {
            lesson.getResources().add(resourceUrl);
        } else {
            lesson.getResources().add(position, resourceUrl);
        }
    }

    @Transactional
    public void removeResourceAtPosition(UUID lessonId, int position) {
        Lesson lesson = getLesson(lessonId);
        if (position >= 0 && position < lesson.getResources().size()) {
            lesson.getResources().remove(position);
        }
    }

    @Transactional
    public void moveResource(UUID lessonId, int fromPosition, int toPosition) {
        Lesson lesson = getLesson(lessonId);
        List<String> resources = lesson.getResources();

        if (fromPosition >= 0 && fromPosition < resources.size() &&
            toPosition >= 0 && toPosition < resources.size()) {
            String resource = resources.remove(fromPosition);
            resources.add(toPosition, resource);
        }
    }

    @Transactional
    public void updateSyllabus(UUID lessonId, String syllabus) {
        Lesson lesson = getLesson(lessonId);
        lesson.setSyllabus(syllabus);
    }

    @Transactional
    public void delete(UUID id) {
        lessonRepository.deleteById(id);
    }

    private Lesson getLesson(UUID lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson not found with id: " + lessonId));
    }
}
