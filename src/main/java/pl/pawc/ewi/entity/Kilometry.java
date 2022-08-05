package pl.pawc.ewi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Kilometry {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column
    private int miesiac;

    @Column
    private int rok;

    @ManyToOne(fetch = FetchType.LAZY)
    private Maszyna maszyna;

    @Column(columnDefinition = "double default 0")
    private BigDecimal wartosc;

    @Override
    public String toString() {
        return "Kilometry{" +
                "id=" + id +
                ", miesiac=" + miesiac +
                ", rok=" + rok +
                ", maszyna=" + maszyna.getId() +
                ", wartosc=" + wartosc +
                '}';
    }

}