package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leralee.hibernate.dto.request.StudentRequest;
import ru.leralee.hibernate.dto.request.TeacherRequest;
import ru.leralee.hibernate.dto.response.StudentResponse;
import ru.leralee.hibernate.dto.response.TeacherResponse;
import ru.leralee.hibernate.dto.response.UserResponse;
import ru.leralee.hibernate.entity.person.Student;
import ru.leralee.hibernate.entity.person.Teacher;
import ru.leralee.hibernate.entity.person.User;
import ru.leralee.hibernate.enums.StudentStatus;
import ru.leralee.hibernate.enums.UserRole;
import ru.leralee.hibernate.exception.AppException;
import ru.leralee.hibernate.exception.NotFoundException;
import ru.leralee.hibernate.mapper.StudentMapper;
import ru.leralee.hibernate.mapper.TeacherMapper;
import ru.leralee.hibernate.mapper.UserMapper;
import ru.leralee.hibernate.repository.StudentRepository;
import ru.leralee.hibernate.repository.TeacherRepository;
import ru.leralee.hibernate.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;
    private final UserMapper userMapper;

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        studentRepository.findByStudentNo(request.getStudentNo()).ifPresent(s -> {
            throw new AppException("Student with studentNo '" + request.getStudentNo() + "' already exists", 409);
        });
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new AppException("User with email '" + request.getEmail() + "' already exists", 409);
        });

        Student student = studentMapper.toEntity(request);
        student.setRole(UserRole.STUDENT);

        Student saved = studentRepository.save(student);
        return studentMapper.toResponse(saved);
    }

    @Transactional
    public TeacherResponse createTeacher(TeacherRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new AppException("User with email '" + request.getEmail() + "' already exists", 409);
        });

        Teacher teacher = teacherMapper.toEntity(request);
        teacher.setRole(UserRole.TEACHER);

        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public StudentResponse findStudentById(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + id));
        return studentMapper.toResponse(student);
    }

    @Transactional(readOnly = true)
    public TeacherResponse findTeacherById(UUID id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found with id: " + id));
        return teacherMapper.toResponse(teacher);
    }

    @Transactional(readOnly = true)
    public UserResponse findUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers() {
        return userMapper.toResponseList(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findAllStudents() {
        return studentMapper.toResponseList(studentRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> findAllTeachers() {
        return teacherMapper.toResponseList(teacherRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findStudentsByStatus(StudentStatus status) {
        return studentMapper.toResponseList(studentRepository.findByStatus(status));
    }

    @Transactional
    public StudentResponse updateStudent(UUID id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + id));
        studentMapper.updateEntityFromRequest(request, student);

        Student updated = studentRepository.save(student);
        return studentMapper.toResponse(updated);
    }

    @Transactional
    public TeacherResponse updateTeacher(UUID id, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found with id: " + id));
        teacherMapper.updateEntityFromRequest(request, teacher);

        Teacher updated = teacherRepository.save(teacher);
        return teacherMapper.toResponse(updated);
    }

    @Transactional
    public void addEmail(UUID studentId, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));
        student.getEmails().add(email);
    }

    @Transactional
    public void removeEmail(UUID studentId, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));
        student.getEmails().remove(email);
    }

    @Transactional
    public void setEmails(UUID studentId, Set<String> emails) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));
        student.getEmails().clear();
        student.getEmails().addAll(emails);
    }

    @Transactional
    public void addPhone(UUID studentId, String phone) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));
        student.getPhones().add(phone);
    }

    @Transactional
    public void removePhone(UUID studentId, String phone) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));
        student.getPhones().remove(phone);
    }

    @Transactional
    public void setPhones(UUID studentId, Set<String> phones) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));
        student.getPhones().clear();
        student.getPhones().addAll(phones);
    }

    @Transactional
    public void changeStudentStatus(UUID studentId, StudentStatus status) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + studentId));
        student.setStatus(status);
    }

    @Transactional
    public void deleteStudent(UUID id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public void deleteTeacher(UUID id) {
        teacherRepository.deleteById(id);
    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
