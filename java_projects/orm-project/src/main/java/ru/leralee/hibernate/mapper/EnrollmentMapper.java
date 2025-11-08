package ru.leralee.hibernate.mapper;

import org.mapstruct.*;
import ru.leralee.hibernate.dto.nested.CourseInfo;
import ru.leralee.hibernate.dto.nested.StudentInfo;
import ru.leralee.hibernate.dto.request.EnrollmentRequest;
import ru.leralee.hibernate.dto.response.EnrollmentResponse;
import ru.leralee.hibernate.entity.Course;
import ru.leralee.hibernate.entity.Enrollment;
import ru.leralee.hibernate.entity.person.Student;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnrollmentMapper {

    @Mapping(target = "student", source = "studentId", qualifiedByName = "studentIdToStudent")
    @Mapping(target = "course", source = "courseId", qualifiedByName = "courseIdToCourse")
    Enrollment toEntity(EnrollmentRequest request);

    @Mapping(target = "student", source = "student", qualifiedByName = "studentToStudentInfo")
    @Mapping(target = "course", source = "course", qualifiedByName = "courseToCourseInfo")
    EnrollmentResponse toResponse(Enrollment enrollment);

    List<EnrollmentResponse> toResponseList(List<Enrollment> enrollments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    void updateEntityFromRequest(EnrollmentRequest request, @MappingTarget Enrollment enrollment);

    @Named("studentToStudentInfo")
    default StudentInfo studentToStudentInfo(Student student) {
        if (student == null) {
            return null;
        }
        StudentInfo info = new StudentInfo();
        info.setId(student.getId());
        info.setFirstName(student.getFirstName());
        info.setLastName(student.getLastName());
        info.setStudentNo(student.getStudentNo());
        return info;
    }

    @Named("courseToCourseInfo")
    default CourseInfo courseToCourseInfo(Course course) {
        if (course == null) {
            return null;
        }
        CourseInfo info = new CourseInfo();
        info.setId(course.getId());
        info.setCode(course.getCode());
        info.setTitle(course.getTitle());
        return info;
    }

    @Named("studentIdToStudent")
    default Student studentIdToStudent(UUID studentId) {
        if (studentId == null) {
            return null;
        }
        Student student = new Student();
        student.setId(studentId);
        return student;
    }

    @Named("courseIdToCourse")
    default Course courseIdToCourse(UUID courseId) {
        if (courseId == null) {
            return null;
        }
        Course course = new Course();
        course.setId(courseId);
        return course;
    }
}
