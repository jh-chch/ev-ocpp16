package com.ev.ocpp16.websocket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "charger_error")
@Getter
public class ChargerError extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charger_error_id")
    private Integer id;

    @Column(name = "error_code", nullable = false, unique = true, length = 30)
    private String errorCode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

}
