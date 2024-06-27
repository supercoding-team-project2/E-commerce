package com.github.supercodingproject2mall.auth.entity;


import com.github.supercodingproject2mall.auth.enums.Gender;
import com.github.supercodingproject2mall.auth.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder(toBuilder = true)
@DynamicInsert
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "phone_num", nullable = false, length = 20)
    private String phoneNum;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "profile_picture_url", length = 100, columnDefinition = "VARCHAR(100) DEFAULT ''")
    private String profilePictureUrl;

    @Column(name = "about_me", length = 100, columnDefinition = "VARCHAR(100) DEFAULT ''")
    private String aboutMe;

    @Column(name = "shopping_pay", columnDefinition = "INTEGER DEFAULT 0")
    private Integer shoppingPay;

    @Column(columnDefinition = "ENUM('ACTIVE', 'DELETED') DEFAULT 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
