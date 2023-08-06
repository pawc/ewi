package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class AnnualReport {

    private String category_unit;
    private String category;
    private String unit;
    private BigDecimal weight;
    private BigDecimal sum;
    private BigDecimal sumMultipliedByWeight;

}