package pl.pawc.ewi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","maszyny"})
@Getter
@Setter
public class Category {

    @Id
    private String name;

    @Column(columnDefinition = "boolean default true")
    private boolean isCarriedOver;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "machine_category",
            joinColumns = { @JoinColumn(name = "category_name") },
            inverseJoinColumns = { @JoinColumn(name = "machine_id") })
    private Set<Machine> machines;

}