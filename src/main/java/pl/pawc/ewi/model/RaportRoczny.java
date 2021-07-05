package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class RaportRoczny {

    @Id
    private String kategoria_jednostka;
    private String kategoria;
    private String jednostka;
    private double suma;

}