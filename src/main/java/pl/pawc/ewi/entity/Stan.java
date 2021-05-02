package pl.pawc.ewi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Stan {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column
    private int miesiac;

    @Column
    private int rok;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="norma_id", nullable=false)
    private Norma norma;

    @Column(columnDefinition = "double default 0")
    private double wartosc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Norma getNorma() {
        return norma;
    }

    public void setNorma(Norma norma) {
        this.norma = norma;
    }

    public double getWartosc() {
        return wartosc;
    }

    public void setWartosc(double wartosc) {
        this.wartosc = wartosc;
    }

    public int getMiesiac() {
        return miesiac;
    }

    public void setMiesiac(int miesiac) {
        this.miesiac = miesiac;
    }

    public int getRok() {
        return rok;
    }

    public void setRok(int rok) {
        this.rok = rok;
    }

    @Override
    public String toString() {
        return "Stan{" +
                "id=" + id +
                ", miesiac=" + miesiac +
                ", rok=" + rok +
                ", norma=" + norma.getId() +
                ", wartosc=" + wartosc +
                '}';
    }

}