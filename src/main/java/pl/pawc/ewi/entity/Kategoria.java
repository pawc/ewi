package pl.pawc.ewi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class Kategoria {

    @Id
    @Column(name = "nazwa")
    private String nazwa;

    @Column(columnDefinition = "boolean default true")
    private boolean przenoszonaNaKolejnyOkres;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "maszyna_kategorie",
            joinColumns = { @JoinColumn(name = "kategorie_nazwa") },
            inverseJoinColumns = { @JoinColumn(name = "maszyna_id") })
    private Set<Maszyna> maszyny;

}