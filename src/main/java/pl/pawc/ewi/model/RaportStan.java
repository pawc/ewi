package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class RaportStan {

    private String maszynanazwa;
    private String maszynaid;
    private String jednostka;
    private long normaid;
    private long stanid;
    private BigDecimal stanpoczatkowy;

}