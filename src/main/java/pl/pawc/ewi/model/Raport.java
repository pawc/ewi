package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
public class Raport {

    @Id
    private String maszynaidnormaid;
    private String maszyna;
    private String maszynaid;
    private double stankilometry;
    private double kilometry;
    private double kilometryprzyczepa;
    private String jednostka;
    private BigDecimal suma;
    private double zatankowano;
    private double ogrzewanie;
    private long normaId;
    private double stanPoprz;
    private double sumagodzin;

}