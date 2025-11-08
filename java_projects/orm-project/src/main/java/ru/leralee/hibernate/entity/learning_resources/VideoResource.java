package ru.leralee.hibernate.entity.learning_resources;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

/**
 * @author valeriali
 * @project orm-project
 */
@Getter
@Entity
@DiscriminatorValue("VIDEO")
public class VideoResource extends LearningResource {

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "duration_sec")
    private Integer durationSec;

    @Column(name = "transcript_url", length = 500)
    private String transcriptUrl;
}
