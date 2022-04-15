package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
public class RaportRoczny {

    @Id
    private String kategoria_jednostka;
    private String kategoria;
    private String jednostka;
    private double suma;

}