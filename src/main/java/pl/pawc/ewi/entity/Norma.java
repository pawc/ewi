package pl.pawc.ewi.entity;

import javax.persistence.*;

@Entity
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJednostka() {
        return jednostka;
    }

    public void setJednostka(String jednostka) {
        this.jednostka = jednostka;
    }

    public double getWartosc() {
        return wartosc;
    }

    public void setWartosc(double wartosc) {
        this.wartosc = wartosc;
    }

    public Maszyna getMaszyna() {
        return maszyna;
    }

    public void setMaszyna(Maszyna maszyna) {
        this.maszyna = maszyna;
    }

    public Double getSuma() {
        return suma;
    }

    public void setSuma(Double suma) {
        this.suma = suma;
    }

    @Override
    public String toString() {
        return "Norma{" +
                "id=" + id +
                ", jednostka='" + jednostka + '\'' +
                ", wartosc=" + wartosc +
                ", maszyna=" + maszyna +
                ", suma=" + suma +
                '}';
    }

}