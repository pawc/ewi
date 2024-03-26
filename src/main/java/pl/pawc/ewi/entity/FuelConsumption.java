package pl.pawc.ewi.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
public class FuelConsumption {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "val")
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.EAGER)
    private FuelConsumptionStandard fuelConsumptionStandard;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Document document;

    @Column
    private BigDecimal refueled;

    @Column
    private BigDecimal heating;

}