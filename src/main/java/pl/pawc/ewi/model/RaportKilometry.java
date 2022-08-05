package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class RaportKilometry {

    private String maszynaid;
    private BigDecimal stanpoczatkowy;
    private String maszynanazwa;

}