package pl.pawc.ewi.entity;

import pl.pawc.ewi.model.Paliwo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dokument> dokumenty = new ArrayList<>();

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

    public List<Dokument> getDokumenty() {
        return dokumenty;
    }

    public void setDokumenty(List<Dokument> dokumenty) {
        this.dokumenty = dokumenty;
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