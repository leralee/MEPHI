package ru.leralee.hibernate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.leralee.hibernate.entity.person.Student;

@Getter
@Setter
@Entity
@Table(name = "course_reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"course_id", "student_id"})
})
/**
 * @author valeriali
 * @project orm-project
 */
public class CourseReview extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", length = 2000)
    private String comment;
}
