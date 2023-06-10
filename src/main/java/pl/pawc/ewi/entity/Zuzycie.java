package pl.pawc.ewi.entity;

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
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
public class Zuzycie {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "wartosc")
    private BigDecimal wartosc;

    @ManyToOne(fetch = FetchType.EAGER)
    private Norma norma;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Dokument dokument;

    @Column(columnDefinition = "double default 0")
    private BigDecimal zatankowano;

    @Column(columnDefinition = "double default 0")
    private BigDecimal ogrzewanie;

}