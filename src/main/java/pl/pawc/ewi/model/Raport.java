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
    private BigDecimal stankilometry;
    private BigDecimal kilometry;
    private BigDecimal kilometryprzyczepa;
    private String jednostka;
    private BigDecimal suma;
    private BigDecimal zatankowano;
    private BigDecimal ogrzewanie;
    private long normaId;
    private BigDecimal stanPoprz;
    private BigDecimal sumagodzin;
    private BigDecimal endState;
    private BigDecimal endStateKilometry;

}