package pl.pawc.ewi.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
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
    @JoinColumn(referencedColumnName="nazwa")
    private Set<Kategoria> kategorie;

    @Transient
    private List<Norma> normy;

}