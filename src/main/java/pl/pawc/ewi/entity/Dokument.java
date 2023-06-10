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
@Table(indexes = {@Index(columnList = "data", name = "data_index")})
public class Dokument {

    @Id
    private String numer;

    @Column(name = "data")
    private Date data;

    @Column(columnDefinition = "double default 0")
    private BigDecimal kilometry;

    @Column(columnDefinition = "double default 0")
    private BigDecimal kilometryPrzyczepa;

    @ManyToOne(fetch = FetchType.EAGER)
    private Maszyna maszyna;

    @Transient
    private List<Zuzycie> zuzycie;

    @Transient
    private BigDecimal kilometryBefore;

    @Override
    public String toString() {
        return "Dokument{" +
                "numer='" + numer + '\'' +
                ", data=" + data +
                ", kilometry=" + kilometry +
                ", kilometryPrzyczepa=" + kilometryPrzyczepa +
                ", maszyna=" + maszyna.getId() +
                '}';
    }

}