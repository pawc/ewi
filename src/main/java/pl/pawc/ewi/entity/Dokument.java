package pl.pawc.ewi.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Dokument {

    @Id
    private String numer;

    @Column(name = "data")
    private Date data;

    @ManyToOne(fetch = FetchType.LAZY)
    private Maszyna maszyna;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zuzycie> zuzycie = new ArrayList<>();

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

    public Maszyna getMaszyna() {
        return maszyna;
    }

    public void setMaszyna(Maszyna maszyna) {
        this.maszyna = maszyna;
    }

    public List<Zuzycie> getZuzycie() {
        return zuzycie;
    }

    public void setZuzycie(List<Zuzycie> zuzycie) {
        this.zuzycie = zuzycie;
    }

    @Override
    public String toString() {
        return "Dokument{" +
                "numer='" + numer + '\'' +
                ", data=" + data +
                ", maszyna=" + maszyna +
                ", zuzycie=" + zuzycie +
                '}';
    }

}