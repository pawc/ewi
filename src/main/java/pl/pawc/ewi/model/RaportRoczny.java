package pl.pawc.ewi.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RaportRoczny {

    @Id
    private String kategoria_jednostka;
    private String kategoria;
    private String jednostka;
    private double suma;

    public String getKategoria_jednostka() {
        return kategoria_jednostka;
    }

    public void setKategoria_jednostka(String kategoria_jednostka) {
        this.kategoria_jednostka = kategoria_jednostka;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public String getJednostka() {
        return jednostka;
    }

    public void setJednostka(String jednostka) {
        this.jednostka = jednostka;
    }

    public double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {
        this.suma = suma;
    }

}