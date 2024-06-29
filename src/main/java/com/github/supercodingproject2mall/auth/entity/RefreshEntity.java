package com.github.supercodingproject2mall.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 500)
    private String refresh;

    @Column(nullable = false, length = 40)
    private String expiration;
}
