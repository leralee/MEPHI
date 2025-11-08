package ru.leralee.hibernate.entity.learning_resources;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author valeriali
 * @project orm-project
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("ARTICLE")
public class ArticleResource extends LearningResource {

    @Column(name = "content_url", length = 500)
    private String contentUrl;

    @Column(name = "reading_time_min")
    private Integer readingTimeMin;
}
