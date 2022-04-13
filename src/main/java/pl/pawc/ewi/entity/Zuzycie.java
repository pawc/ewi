package pl.pawc.ewi.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@ToString
public class Zuzycie {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "wartosc")
    private double wartosc;

    @ManyToOne(fetch = FetchType.EAGER)
    private Norma norma;

    @ManyToOne(fetch = FetchType.EAGER)
    private Dokument dokument;

    @Column(columnDefinition = "double default 0")
    private double zatankowano;

    @Column(columnDefinition = "double default 0")
    private double ogrzewanie;

}