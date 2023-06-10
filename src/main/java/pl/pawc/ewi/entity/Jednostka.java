package pl.pawc.ewi.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
public class Jednostka {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "waga")
    private BigDecimal waga;

    public Jednostka(){
    }

    public Jednostka(String nazwa) {
        this.nazwa = nazwa;
    }

}