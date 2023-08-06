package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class KilometersReport {

    private String machineId;
    private BigDecimal initialState;
    private String machineName;

}