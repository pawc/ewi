package pl.pawc.ewi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Zuzycie {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "wartosc")
    private double wartosc;

    @ManyToOne(fetch = FetchType.LAZY)
    private Norma norma;

    @ManyToOne(fetch = FetchType.LAZY)
    private Dokument dokument;

    @Column(columnDefinition = "double default 0")
    private double zatankowano;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getWartosc() {
        return wartosc;
    }

    public void setWartosc(double wartosc) {
        this.wartosc = wartosc;
    }

    public Norma getNorma() {
        return norma;
    }

    public void setNorma(Norma norma) {
        this.norma = norma;
    }

    public double getZatankowano() {
        return zatankowano;
    }

    public void setZatankowano(double zatankowano) {
        this.zatankowano = zatankowano;
    }

    public Dokument getDokument() {
        return dokument;
    }

    public void setDokument(Dokument dokument) {
        this.dokument = dokument;
    }

    @Override
    public String toString() {
        return "Zuzycie{" +
                "id=" + id +
                ", wartosc=" + wartosc +
                ", norma=" + norma +
                ", dokument=" + dokument +
                ", zatankowano=" + zatankowano +
                '}';
    }

}