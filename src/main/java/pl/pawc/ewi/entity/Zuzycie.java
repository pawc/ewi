package pl.pawc.ewi.entity;

import javax.persistence.*;

@Entity
public class Zuzycie {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "wartosc")
    private double wartosc;

    @ManyToOne(fetch = FetchType.LAZY)
    private Dokument dokument;

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
                ", dokument=" + dokument +
                '}';
    }

}
