package org.ssglobal.revalida.codes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "app_user")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class AppUser {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Integer userId;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, columnDefinition = "text")
    private String password;

    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean isValidated;

    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean isActive;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(mappedBy = "user")
    private Set<Posts> posts;

    @OneToMany(mappedBy = "user")
    private Set<Likes> likedPosts;

    @OneToMany(mappedBy = "user")
    private Set<Comments> commentedPosts;

    @OneToMany(mappedBy = "follower")
    private Set<Follows> follower;

    @OneToMany(mappedBy = "following")
    private Set<Follows> following;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;
}
