package pl.pawc.ewi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@Getter
@Setter
@ToString
public class Norma {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "jednostka")
    private String jednostka;

    @Column(name = "wartosc")
    private double wartosc;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Maszyna maszyna;

    @Transient
    private Double suma;

    @Transient
    private Double sumaBefore;

    @Transient
    private Double stan;

    @Column(name = "czy_ogrzewanie")
    private boolean czyOgrzewanie;

    @Transient
    @Column(name = "suma_ogrzewania")
    private double sumaOgrzewania;

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

    /*  @Column(name = "czy_zaokraglane", columnDefinition="bit default 1")
    private boolean czyZaokraglane = true;*/

}