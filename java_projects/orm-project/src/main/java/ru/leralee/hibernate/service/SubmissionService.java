package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leralee.hibernate.dto.request.SubmissionRequest;
import ru.leralee.hibernate.dto.response.SubmissionResponse;
import ru.leralee.hibernate.entity.Assignment;
import ru.leralee.hibernate.entity.Submission;
import ru.leralee.hibernate.entity.person.Student;
import ru.leralee.hibernate.enums.SubmissionStatus;
import ru.leralee.hibernate.exception.AppException;
import ru.leralee.hibernate.exception.NotFoundException;
import ru.leralee.hibernate.mapper.SubmissionMapper;
import ru.leralee.hibernate.repository.AssignmentRepository;
import ru.leralee.hibernate.repository.StudentRepository;
import ru.leralee.hibernate.repository.SubmissionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;
    private final SubmissionMapper submissionMapper;

    @Transactional
    public SubmissionResponse submit(SubmissionRequest request) {
        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new NotFoundException("Assignment not found with id: " + request.getAssignmentId()));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + request.getStudentId()));

        submissionRepository.findByAssignmentId(request.getAssignmentId()).stream()
                .filter(existing -> existing.getStudent().getId().equals(request.getStudentId()))
                .findAny()
                .ifPresent(existing -> {
                    throw new AppException("Student already submitted this assignment", 409);
                });

        Submission submission = submissionMapper.toEntity(request);
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setAttemptNo(request.getAttemptNo() != null ? request.getAttemptNo() : 1);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setStatus(request.getStatus() != null ? request.getStatus() : SubmissionStatus.PENDING);
        submission.setScore(request.getScore());
        submission.setFeedback(request.getFeedback());

        Submission saved = submissionRepository.save(submission);
        return submissionMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public SubmissionResponse findById(UUID id) {
        return submissionMapper.toResponse(getSubmission(id));
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> findByAssignment(UUID assignmentId) {
        return submissionMapper.toResponseList(submissionRepository.findByAssignmentId(assignmentId));
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> findByStudent(UUID studentId) {
        return submissionMapper.toResponseList(submissionRepository.findByStudentId(studentId));
    }

    @Transactional
    public SubmissionResponse grade(UUID submissionId, Double score, String feedback) {
        Submission submission = getSubmission(submissionId);
        submission.setScore(score);
        submission.setFeedback(feedback);
        submission.setStatus(SubmissionStatus.GRADED);
        return submissionMapper.toResponse(submission);
    }

    @Transactional
    public SubmissionResponse update(UUID id, SubmissionRequest request) {
        Submission submission = getSubmission(id);
        submissionMapper.updateEntityFromRequest(request, submission);
        if (request.getScore() != null) {
            submission.setScore(request.getScore());
        }
        if (request.getFeedback() != null) {
            submission.setFeedback(request.getFeedback());
        }
        return submissionMapper.toResponse(submissionRepository.save(submission));
    }

    private Submission getSubmission(UUID id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Submission not found with id: " + id));
    }
}
