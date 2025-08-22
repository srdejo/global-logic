package io.github.srdejo.technical.test.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Phone {
    @Id
    @GeneratedValue
    private Long id;

    private Long number;
    @Column(name = "city_code")
    private Integer cityCode;
    @Column(name = "country_code")
    private String countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
