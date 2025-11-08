package ru.leralee.hibernate.entity.person;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.leralee.hibernate.entity.AbstractEntity;

/**
 * @author valeriali
 * @project orm-project
 */
@Getter
@Setter
@Entity
@Table(name = "user_profiles")
public class UserProfile extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "contact_links", length = 1000)
    private String contactLinks;
}
