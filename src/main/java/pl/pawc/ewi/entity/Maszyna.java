package pl.pawc.ewi.entity;

import pl.pawc.ewi.model.Paliwo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Maszyna {

    @Id
    private Integer id;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "paliwo")
    private Paliwo paliwo;

    @Column(name = "opis")
    private String opis;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Paliwo getPaliwo() {
        return paliwo;
    }

    public void setPaliwo(Paliwo paliwo) {
        this.paliwo = paliwo;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @Override
    public String toString() {
        return "Maszyna{" +
                "id=" + id +
                ", nazwa='" + nazwa + '\'' +
                ", paliwo=" + paliwo +
                ", opis='" + opis + '\'' +
                '}';
    }

}