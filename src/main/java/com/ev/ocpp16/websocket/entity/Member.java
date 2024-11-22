package com.ev.ocpp16.websocket.entity;

import com.ev.ocpp16.websocket.entity.enums.AccountStatus;
import com.ev.ocpp16.websocket.entity.enums.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "id_token", nullable = false, length = 36, unique = true)
    private String idToken;

    @Column(name = "username", nullable = false, length = 30)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles", nullable = false, columnDefinition = "enum('ADMIN','USER') default 'USER'")
    private Roles roles;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, columnDefinition = "enum('ACTIVE','LOCKED','DISABLED') default 'ACTIVE'")
    private AccountStatus accountStatus;

    public Member(String idToken, String username, String password, String email, Roles roles,
            AccountStatus accountStatus) {
        this.idToken = idToken;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.accountStatus = accountStatus;
    }
}
