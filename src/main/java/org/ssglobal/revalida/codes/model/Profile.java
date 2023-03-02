package org.ssglobal.revalida.codes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.ssglobal.revalida.codes.enums.Gender;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Profile {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "profile_sequence",
            sequenceName = "profile_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "profile_sequence"
    )
    private Integer profileId;

    @Column(nullable = false, length = 50)
    private String firstname;

    @Column(length = 20)
    private String middlename;

    @Column(nullable = false, length = 50)
    private String lastname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private LocalDate birthdate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime registerDate;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @Column(length = 11)
    private String phone;

    @Column(length = 160)
    private String description;

    @Column(columnDefinition = "text")
    private String profilePic;

    @Column(columnDefinition = "text")
    private String coverPic;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private AppUser profile;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
