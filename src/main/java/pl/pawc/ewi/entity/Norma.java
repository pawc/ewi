package pl.pawc.ewi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
public class Norma {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "jednostka")
    private String jednostka;

    @ManyToOne
    private Jednostka jednostkaObj;

    @Column(name = "wartosc")
    private BigDecimal wartosc;

    @Column(name = "czyZaokr1setna", columnDefinition = "boolean default false")
    private boolean czyZaokr1setna;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Maszyna maszyna;

    @Transient
    private BigDecimal suma;

    @Transient
    private BigDecimal sumaBefore;

    @Transient
    private BigDecimal stan;

    @Column(name = "czy_ogrzewanie")
    private boolean czyOgrzewanie;

    @Transient
    @Column(name = "suma_ogrzewania")
    private BigDecimal sumaOgrzewania;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Norma norma = (Norma) o;

        return id == norma.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

}