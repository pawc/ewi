package pl.pawc.ewi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Kategoria {

    @Id
    @Column(name = "nazwa")
    private String nazwa;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "maszyna_kategorie")
    private Set<Maszyna> maszyny;

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Set<Maszyna> getMaszyny() {
        return maszyny;
    }

    public void setMaszyny(Set<Maszyna> maszyny) {
        this.maszyny = maszyny;
    }

}