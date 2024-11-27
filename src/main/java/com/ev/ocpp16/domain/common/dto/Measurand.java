package com.ev.ocpp16.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Measurand {
    @JsonProperty("Current.Export")
    CURRENT_EXPORT("Current.Export"), // EV에서 즉시 흐르는 전류량

    @JsonProperty("Current.Import")
    CURRENT_IMPORT("Current.Import"), // EV로 즉시 흐르는 전류량

    @JsonProperty("Current.Offered")
    CURRENT_OFFERED("Current.Offered"), // EV에 제공된 최대 전류량

    @JsonProperty("Energy.Active.Export.Register")
    ENERGY_ACTIVE_EXPORT_REGISTER("Energy.Active.Export.Register"), // EV에 의해 수출된 에너지 (Wh 또는 kWh)

    @JsonProperty("Energy.Active.Import.Register")
    ENERGY_ACTIVE_IMPORT_REGISTER("Energy.Active.Import.Register"), // EV로 수입된 에너지 (Wh 또는 kWh)

    @JsonProperty("Energy.Reactive.Export.Register")
    ENERGY_REACTIVE_EXPORT_REGISTER("Energy.Reactive.Export.Register"), // EV에 의해 수출된 무효 에너지 (varh 또는 kvarh)

    @JsonProperty("Energy.Reactive.Import.Register")
    ENERGY_REACTIVE_IMPORT_REGISTER("Energy.Reactive.Import.Register"), // EV로 수입된 무효 에너지 (varh 또는 kvarh)

    @JsonProperty("Energy.Active.Export.Interval")
    ENERGY_ACTIVE_EXPORT_INTERVAL("Energy.Active.Export.Interval"), // EV에 의해 수출된 에너지 (Wh 또는 kWh)

    @JsonProperty("Energy.Active.Import.Interval")
    ENERGY_ACTIVE_IMPORT_INTERVAL("Energy.Active.Import.Interval"), // EV로 수입된 에너지 (Wh 또는 kWh)

    @JsonProperty("Energy.Reactive.Export.Interval")
    ENERGY_REACTIVE_EXPORT_INTERVAL("Energy.Reactive.Export.Interval"), // EV에 의해 수출된 무효 에너지 (varh 또는 kvarh)

    @JsonProperty("Energy.Reactive.Import.Interval")
    ENERGY_REACTIVE_IMPORT_INTERVAL("Energy.Reactive.Import.Interval"), // EV로 수입된 무효 에너지 (varh 또는 kvarh)

    @JsonProperty("Frequency")
    FREQUENCY("Frequency"), // 전력선 주파수의 즉시 읽기 값

    @JsonProperty("Power.Active.Export")
    POWER_ACTIVE_EXPORT("Power.Active.Export"), // EV에 의해 수출된 즉각적인 활성 전력 (W 또는 kW)

    @JsonProperty("Power.Active.Import")
    POWER_ACTIVE_IMPORT("Power.Active.Import"), // EV로 수입된 즉각적인 활성 전력 (W 또는 kW)

    @JsonProperty("Power.Factor")
    POWER_FACTOR("Power.Factor"), // 총 에너지 흐름의 즉각적인 전력 계수

    @JsonProperty("Power.Offered")
    POWER_OFFERED("Power.Offered"), // EV에 제공된 최대 전력

    @JsonProperty("Power.Reactive.Export")
    POWER_REACTIVE_EXPORT("Power.Reactive.Export"), // EV에 의해 수출된 즉각적인 무효 전력 (var 또는 kvar)

    @JsonProperty("Power.Reactive.Import")
    POWER_REACTIVE_IMPORT("Power.Reactive.Import"), // EV로 수입된 즉각적인 무효 전력 (var 또는 kvar)

    @JsonProperty("RPM")
    RPM("RPM"), // RPM 단위의 팬 속도

    @JsonProperty("SoC")
    SOC("SoC"), // 충전 중인 차량의 충전 상태를 백분율로 표시

    @JsonProperty("Temperature")
    TEMPERATURE("Temperature"), // 충전소 내부의 온도 측정치

    @JsonProperty("Voltage")
    VOLTAGE("Voltage"); // 즉각적인 AC RMS 공급 전압

    private String value;
}
