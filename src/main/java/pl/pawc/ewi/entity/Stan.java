package pl.pawc.ewi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class Stan {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column
    private int miesiac;

    @Column
    private int rok;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="norma_id", nullable=false)
    private Norma norma;

    @Column(columnDefinition = "double default 0")
    private double wartosc;

    @Override
    public String toString() {
        return "Stan{" +
                "id=" + id +
                ", miesiac=" + miesiac +
                ", rok=" + rok +
                ", norma=" + norma.getId() +
                ", wartosc=" + wartosc +
                '}';
    }

}