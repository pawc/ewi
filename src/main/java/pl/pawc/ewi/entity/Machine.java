package pl.pawc.ewi.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Machine {

    @Id
    @Column
    private String id;

    @Column
    private String name;

    @Column
    private String description;

    @Column(columnDefinition = "boolean default true")
    private boolean isActive;

    @Transient
    private BigDecimal sumOfKilometers;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "machines", cascade = CascadeType.ALL)
    private Set<Category> categories;

    @Transient
    private List<FuelConsumptionStandard> fuelConsumptionStandards;

    public boolean isCarriedOver(){

        if(categories == null || categories.isEmpty()) return false;
        boolean result = false;
        for(Category category : categories){
            result |= category.isCarriedOver();
        }
        return result;
    }

    @Override
    public String toString() {
        return "Maszyna{" +
                "id='" + id + '\'' +
                ", nazwa='" + name + '\'' +
                ", opis='" + description + '\'' +
                ", aktywna=" + isActive +
                ", sumaKilometry=" + sumOfKilometers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machine machine = (Machine) o;
        return Objects.equals(id, machine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}