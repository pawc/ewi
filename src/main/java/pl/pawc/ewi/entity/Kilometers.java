package pl.pawc.ewi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Kilometers {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "month_val")
    private int month;

    @Column(name = "year_val")
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    private Machine machine;

    @Column(name = "val", columnDefinition = "double default 0")
    private BigDecimal value;

    @Override
    public String toString() {
        return "Kilometers{" +
                "id=" + id +
                ", month=" + month +
                ", year=" + year +
                ", machine=" + machine.getId() +
                ", value=" + value +
                '}';
    }

}