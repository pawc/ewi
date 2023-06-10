package pl.pawc.ewi.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Maszyna {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "opis")
    private String opis;

    @Column(columnDefinition = "boolean default true")
    private boolean aktywna;

    @Transient
    @Column(name = "suma_kilometry")
    private BigDecimal sumaKilometry;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName="nazwa")
    private Set<Kategoria> kategorie;

    @Transient
    private List<Norma> normy;

    public boolean isPrzenoszonaNaKolejnyOkres(){

        if(kategorie == null || kategorie.isEmpty()) return false;
        boolean result = false;
        for(Kategoria kategoria : kategorie){
            result |= kategoria.isPrzenoszonaNaKolejnyOkres();
        }
        return result;
    }

    @Override
    public String toString() {
        return "Maszyna{" +
                "id='" + id + '\'' +
                ", nazwa='" + nazwa + '\'' +
                ", opis='" + opis + '\'' +
                ", aktywna=" + aktywna +
                ", sumaKilometry=" + sumaKilometry +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maszyna maszyna = (Maszyna) o;
        return Objects.equals(id, maszyna.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}