package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
public class RaportRoczny {

    @Id
    private String kategoria_jednostka;
    private String kategoria;
    private String jednostka;
    private BigDecimal waga;
    private BigDecimal suma;
    private BigDecimal sumaRazyWaga;

}