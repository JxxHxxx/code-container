package com.example.demo.authority.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 사용자는 간접 키 참조
 */

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_authority_master")
public class UserAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_authority_no")
    private Long no;

    @Column(name = "user_id")

    private String userId;
    @ManyToOne
    private Authority authority;
    @Embedded
    private AuthorityDuration duration;
    @Column(name = "create_user_id")
    private String createdUserId;
}
