package ru.leralee.hibernate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.leralee.hibernate.entity.learning_resources.LearningResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Getter
@Setter
@Entity
@Table(name = "lessons")
public class Lesson extends AbstractEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "syllabus", columnDefinition = "TEXT")
    private String syllabus;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "lesson_resources",
            joinColumns = @JoinColumn(name = "lesson_id")
    )
    @OrderColumn(name = "resource_order")
    @Column(name = "link", length = 500)
    private List<String> resources = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private CourseModule module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_resource_id")
    private LearningResource primaryResource;
}
