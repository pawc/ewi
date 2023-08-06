package pl.pawc.ewi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.pawc.ewi.entity.Document;
import pl.pawc.ewi.entity.Machine;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class ReportMachineKilometers {

    private Machine machine;
    private Date dataStart;
    private Date dataEnd;
    private List<Document> documents;
    private BigDecimal sumKilometers;
    private BigDecimal sumKilometersTrailer;

}