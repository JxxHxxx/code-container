package com.example.demo.authority.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(indexes = @Index(name = "idx_authority_serviceId_authorityId", columnList = "service_id, authority_id"))
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_no")
    private Long no;
    @Column(name = "service_id")

    private String serviceId;
    @Column(name = "authority_id")
    private Long id;
    @Column(name = "authority_name")

    private String authorityName;
    @Column(name = "authority_type")
    @Enumerated
    private AuthorityType authorityType;
}
