package pl.pawc.ewi.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Kilometry {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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