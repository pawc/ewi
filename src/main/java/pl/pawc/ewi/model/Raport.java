package pl.pawc.ewi.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Raport {

    private String maszyna;
    private String maszynaid;
    private double stankilometry;
    private double kilometry;
    private String jednostka;
    private double suma;
    private double zatankowano;
    private double ogrzewanie;
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

    public String getMaszynaid() {
        return maszynaid;
    }

    public void setMaszynaid(String maszynaid) {
        this.maszynaid = maszynaid;
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

    public double getKilometry() {
        return kilometry;
    }

    public void setKilometry(double kilometry) {
        this.kilometry = kilometry;
    }

    public double getStankilometry() {
        return stankilometry;
    }

    public void setStankilometry(double stankilometry) {
        this.stankilometry = stankilometry;
    }

    public double getOgrzewanie() {
        return ogrzewanie;
    }

    public void setOgrzewanie(double ogrzewanie) {
        this.ogrzewanie = ogrzewanie;
    }

    @Override
    public String toString() {
        return "Raport{" +
                "maszyna='" + maszyna + '\'' +
                ", maszynaid='" + maszynaid + '\'' +
                ", stankilometry=" + stankilometry +
                ", kilometry=" + kilometry +
                ", jednostka='" + jednostka + '\'' +
                ", suma=" + suma +
                ", zatankowano=" + zatankowano +
                ", ogrzewanie=" + ogrzewanie +
                ", normaId=" + normaId +
                ", stanPoprz=" + stanPoprz +
                ", Id='" + Id + '\'' +
                '}';
    }

}