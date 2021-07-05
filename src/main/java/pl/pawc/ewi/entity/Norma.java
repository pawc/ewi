package pl.pawc.ewi.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Maszyna maszyna;

    @Transient
    private Double suma;

    @Column(name = "czy_ogrzewanie")
    private boolean czyOgrzewanie;

    @Transient
    @Column(name = "suma_ogrzewania")
    private double sumaOgrzewania;

}