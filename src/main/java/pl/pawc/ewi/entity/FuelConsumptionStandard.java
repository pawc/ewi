package pl.pawc.ewi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
public class FuelConsumptionStandard {

    @Id
    @Column
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column
    private String unit;

    @ManyToOne
    private Unit unitObj;

    @Column(name = "val")
    private BigDecimal value;

    // is rounded to 0.01
    @Column(columnDefinition = "boolean default false")
    private boolean isRounded;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Machine machine;

    @Transient
    private BigDecimal sum;

    @Transient
    private BigDecimal sumBefore;

    @Transient
    private BigDecimal initialState;

    @Column
    private boolean isUsedForHeating;

    @Transient
    @Column
    private BigDecimal sumHeating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FuelConsumptionStandard fuelConsumptionStandard = (FuelConsumptionStandard) o;

        return id == fuelConsumptionStandard.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

}