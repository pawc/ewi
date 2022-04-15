package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RaportStan {

    private String maszynanazwa;
    private String maszynaid;
    private String jednostka;
    private double normaid;
    private double stanid;
    private double stanpoczatkowy;

}