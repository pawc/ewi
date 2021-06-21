package pl.pawc.ewi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","maszyny"})
public class Kategoria {

    @Id
    @Column(name = "nazwa")
    private String nazwa;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "maszyna_kategorie",
            joinColumns = { @JoinColumn(name = "kategorie_nazwa") },
            inverseJoinColumns = { @JoinColumn(name = "maszyna_id") })
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