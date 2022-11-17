package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Maszyna;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class RaportMaszynaKilometry {

    private Maszyna maszyna;
    private Date dataStart;
    private Date dataEnd;
    private List<Dokument> dokumenty;
    private BigDecimal sumaKilometry;
    private BigDecimal sumaKilometryPrzyczepa;

}