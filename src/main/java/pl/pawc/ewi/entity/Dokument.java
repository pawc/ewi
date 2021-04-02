package pl.pawc.ewi.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Dokument {

    @Id
    private String numer;

    @Column(name = "data")
    private Date data;

    @Column(name = "ilosc")
    private double ilosc;

    @ManyToOne(fetch = FetchType.LAZY)
    private Maszyna maszyna;

    public String getNumer() {
        return numer;
    }

    public void setNumer(String numer) {
        this.numer = numer;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getIlosc() {
        return ilosc;
    }

    public void setIlosc(double ilosc) {
        this.ilosc = ilosc;
    }

    public Maszyna getMaszyna() {
        return maszyna;
    }

    public void setMaszyna(Maszyna maszyna) {
        this.maszyna = maszyna;
    }

    @Override
    public String toString() {
        return "Dokument{" +
                "numer='" + numer + '\'' +
                ", data=" + data +
                ", ilosc=" + ilosc +
                ", maszyna=" + maszyna +
                '}';
    }

}