package pl.pawc.ewi.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
public class Jednostka {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.AUTO)
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