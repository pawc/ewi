package pl.pawc.ewi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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