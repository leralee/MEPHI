package ru.leralee.hibernate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.leralee.hibernate.entity.person.Student;
import ru.leralee.hibernate.enums.SubmissionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Getter
@Setter
@Entity
@Table(name = "submissions")
public class Submission extends AbstractEntity {

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @Column(name = "content_url", length = 500)
    private String contentUrl;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubmissionStatus status;

    @Column(name = "attempt_no", nullable = false)
    private Integer attemptNo;

    @Column(name = "score")
    private Double score;

    @Column(name = "feedback", length = 2000)
    private String feedback;

    @Version
    @Column(name = "version")
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @OneToOne(mappedBy = "submission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Grade grade;

    @OneToMany(mappedBy = "submission", fetch = FetchType.LAZY)
    private List<SubmissionAttachment> attachments = new ArrayList<>();
}
