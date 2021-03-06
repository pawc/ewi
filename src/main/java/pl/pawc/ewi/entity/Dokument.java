package pl.pawc.ewi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Dokument {

    @Id
    private String numer;

    @Column(name = "data")
    private Date data;

    @Column(columnDefinition = "double default 0")
    private double kilometry;

    @ManyToOne(fetch = FetchType.LAZY)
    private Maszyna maszyna;

    @Transient
    private List<Zuzycie> zuzycie = new ArrayList<>();

    @Transient
    private Double kilometryBefore;

    @Override
    public String toString() {
        return "Dokument{" +
                "numer='" + numer + '\'' +
                ", data=" + data +
                ", kilometry=" + kilometry +
                ", maszyna=" + maszyna.getId() +
                ", zuzycie=" + zuzycie +
                '}';
    }

}