package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Report {

    private String machineIdFuelConsumptionStandardId;
    private String machine;
    private String machineId;
    private BigDecimal kilometersInitialState;
    private BigDecimal kilometers;
    private BigDecimal kilometersTrailer;
    private String unit;
    private BigDecimal sum;
    private BigDecimal refueled;
    private BigDecimal heating;
    private long fuelConsumptionStandardId;
    private BigDecimal initialState;
    private BigDecimal sumHours;
    private BigDecimal endState;
    private BigDecimal endStateKilometers;

}