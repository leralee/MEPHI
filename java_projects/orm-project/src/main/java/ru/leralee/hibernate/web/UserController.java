package ru.leralee.hibernate.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.leralee.hibernate.dto.request.StudentRequest;
import ru.leralee.hibernate.dto.request.TeacherRequest;
import ru.leralee.hibernate.dto.response.StudentResponse;
import ru.leralee.hibernate.dto.response.TeacherResponse;
import ru.leralee.hibernate.dto.response.UserResponse;
import ru.leralee.hibernate.enums.StudentStatus;
import ru.leralee.hibernate.service.UserService;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/students")
    public StudentResponse createStudent(@Valid @RequestBody StudentRequest request) {
        return userService.createStudent(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/teachers")
    public TeacherResponse createTeacher(@Valid @RequestBody TeacherRequest request) {
        return userService.createTeacher(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/students/{id}")
    public StudentResponse getStudent(@PathVariable UUID id) {
        return userService.findStudentById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/teachers/{id}")
    public TeacherResponse getTeacher(@PathVariable UUID id) {
        return userService.findTeacherById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.findAllUsers();
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/students")
    public List<StudentResponse> getStudents(@RequestParam(required = false) StudentStatus status) {
        if (status != null) {
            return userService.findStudentsByStatus(status);
        }
        return userService.findAllStudents();
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/teachers")
    public List<TeacherResponse> getTeachers() {
        return userService.findAllTeachers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/students/{id}")
    public StudentResponse updateStudent(@PathVariable UUID id, @Valid @RequestBody StudentRequest request) {
        return userService.updateStudent(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/teachers/{id}")
    public TeacherResponse updateTeacher(@PathVariable UUID id, @Valid @RequestBody TeacherRequest request) {
        return userService.updateTeacher(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable UUID id) {
        userService.deleteStudent(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/teachers/{id}")
    public void deleteTeacher(@PathVariable UUID id) {
        userService.deleteTeacher(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}
