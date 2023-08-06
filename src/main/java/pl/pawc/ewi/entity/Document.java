package pl.pawc.ewi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(indexes = {@Index(columnList = "date", name = "data_index")})
public class Document {

    @Id
    private String number;

    @Column(name = "date")
    private Date date;

    @Column(columnDefinition = "double default 0")
    private BigDecimal kilometers;

    @Column(columnDefinition = "double default 0")
    private BigDecimal kilometersTrailer;

    @ManyToOne(fetch = FetchType.EAGER)
    private Machine machine;

    @Transient
    private List<FuelConsumption> fuelConsumption;

    @Transient
    private BigDecimal kilometersBefore;

    @Override
    public String toString() {
        return "Dokument{" +
                "number='" + number + '\'' +
                ", date=" + date +
                ", kilometers=" + kilometers +
                ", kilometersTrailer=" + kilometersTrailer +
                ", machine=" + machine.getId() +
                '}';
    }

}