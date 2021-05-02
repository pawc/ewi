package pl.pawc.ewi.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Raport {

    private String maszyna;
    private String jednostka;
    private double suma;
    private double zatankowano;
    private long normaId;
    private double stanPoprz;

    @Id
    private String Id;

    public String getMaszyna() {
        return maszyna;
    }

    public void setMaszyna(String maszyna) {
        this.maszyna = maszyna;
    }

    public String getJednostka() {
        return jednostka;
    }

    public void setJednostka(String jednostka) {
        this.jednostka = jednostka;
    }

    public double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {
        this.suma = suma;
    }

    public double getZatankowano() {
        return zatankowano;
    }

    public void setZatankowano(double zatankowano) {
        this.zatankowano = zatankowano;
    }

    public double getStanPoprz() {
        return stanPoprz;
    }

    public void setStanPoprz(double stanPoprz) {
        this.stanPoprz = stanPoprz;
    }

    public long getNormaId() {
        return normaId;
    }

    public void setNormaId(long normaId) {
        this.normaId = normaId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "Raport{" +
                "maszyna='" + maszyna + '\'' +
                ", jednostka='" + jednostka + '\'' +
                ", suma=" + suma +
                ", zatankowano=" + zatankowano +
                ", normaId=" + normaId +
                ", stanPoprz=" + stanPoprz +
                ", Id='" + Id + '\'' +
                '}';
    }

}