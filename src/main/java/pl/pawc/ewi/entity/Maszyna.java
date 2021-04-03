package pl.pawc.ewi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Entity
public class Maszyna {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "opis")
    private String opis;

    @Transient
    private List<Norma> normy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @Override
    public String toString() {
        return "Maszyna{" +
                "id=" + id +
                ", nazwa='" + nazwa + '\'' +
                ", opis='" + opis + '\'' +
                ", normy=" + normy +
                '}';
    }

}