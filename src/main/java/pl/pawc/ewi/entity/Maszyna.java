package pl.pawc.ewi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.List;
import java.util.Set;

@Entity
public class Maszyna {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "opis")
    private String opis;

    @Transient
    @Column(name = "suma_kilometry")
    private double sumaKilometry;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Kategoria> kategorie;

    @Transient
    private List<Norma> normy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public List<Norma> getNormy() {
        return normy;
    }

    public void setNormy(List<Norma> normy) {
        this.normy = normy;
    }

    public double getSumaKilometry() {
        return sumaKilometry;
    }

    public void setSumaKilometry(double sumaKilometry) {
        this.sumaKilometry = sumaKilometry;
    }

    public Set<Kategoria> getKategorie() {
        return kategorie;
    }

    public void setKategorie(Set<Kategoria> kategorie) {
        this.kategorie = kategorie;
    }

    @Override
    public String toString() {
        return "Maszyna{" +
                "id='" + id + '\'' +
                ", nazwa='" + nazwa + '\'' +
                ", opis='" + opis + '\'' +
                ", sumaKilometry=" + sumaKilometry +
                ", normy=" + normy +
                '}';
    }

}