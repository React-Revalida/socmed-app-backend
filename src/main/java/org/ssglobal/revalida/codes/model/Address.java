package org.ssglobal.revalida.codes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Address {
    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "address_sequence",
            sequenceName = "address_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "address_sequence"
    )
    private Integer addressId;

    @Column(length = 20)
    private String houseNo;

    @Column(nullable = false, length = 20)
    private String street;

    @Column(nullable = false, length = 25)
    private String subdivision;

    @Column(nullable = false, length = 25)
    private String barangay;

    @Column(nullable = false, length = 25)
    private String city;

    @Column(nullable = false, length = 20)
    private String province;

    @Column(nullable = false)
    private Integer zip;

    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    private Profile address;
}
