package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class FuelInitialStateReport {

    private String machineName;
    private String machineId;
    private String unit;
    private long fuelConsumptionStandardId;
    private long fuelInitialStateId;
    private BigDecimal fuelInitialState;

}