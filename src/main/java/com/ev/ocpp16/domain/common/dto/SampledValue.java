package com.ev.ocpp16.domain.common.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SampledValue {
    private String value; // 1..1
    private ReadingContext context;
    private ValueFormat format;
    private Measurand measurand;
    private Phase phase;
    private Location location;
    private UnitOfMeasure unit;
}
