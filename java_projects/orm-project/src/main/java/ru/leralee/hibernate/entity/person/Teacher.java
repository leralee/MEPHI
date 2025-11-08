package ru.leralee.hibernate.entity.person;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.leralee.hibernate.entity.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Getter
@Setter
@Entity
@Table(name = "teachers")
public class Teacher extends User {

    @Column(name = "academic_title", length = 100)
    private String academicTitle;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();
}
