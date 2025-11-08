package ru.leralee.hibernate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.leralee.hibernate.entity.learning_resources.LearningResource;
import ru.leralee.hibernate.entity.person.Teacher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author valeriali
 * @project orm-project
 */
@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course extends AbstractEntity {

    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "credits")
    private Integer credits;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "course_keywords",
            joinColumns = @JoinColumn(name = "course_id")
    )
    @Column(name = "keyword")
    private Set<String> keywords = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Teacher author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featured_resource_id")
    private LearningResource featuredResource;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    private List<CourseModule> modules = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_tags",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

/**
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CourseTag> courseTags = new HashSet<>();
 **/

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();
}
