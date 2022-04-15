package pl.pawc.ewi.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Kategoria;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.model.RaportKilometry;
import pl.pawc.ewi.model.RaportRoczny;
import pl.pawc.ewi.repository.KategoriaRepository;
import pl.pawc.ewi.repository.KilometryRepository;
import pl.pawc.ewi.repository.NormaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Component
@RequiredArgsConstructor
public class RaportService {

    private final KilometryRepository kilometryRepository;
    private final MaszynaService maszynaService;
    private final KategoriaRepository kategoriaRepository;
    private final ZuzycieService zuzycieService;
    private final NormaRepository normaRepository;

    List<RaportKilometry> getKilometryRaport(int rok, int miesiac){

        List<Maszyna> allActive = maszynaService.findAllActive();
        List<RaportKilometry> result = new ArrayList<>();

        allActive.forEach(m -> {
            RaportKilometry raportKilometry = new RaportKilometry();
            raportKilometry.setMaszynaid(m.getId());
            raportKilometry.setMaszynanazwa(m.getNazwa());
            Kilometry oneByMaszynaAndRokAndMiesiac = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(m, rok, miesiac);
            if (oneByMaszynaAndRokAndMiesiac == null){
                raportKilometry.setStanpoczatkowy(0);
            }
            else{
                raportKilometry.setStanpoczatkowy(oneByMaszynaAndRokAndMiesiac.getWartosc());
            }
            result.add(raportKilometry);
        });

        return result;

    }

    List<RaportRoczny> getRaportRoczny(int year){
        List<RaportRoczny> result = new ArrayList<>();

        Iterable<Kategoria> kategorieI = kategoriaRepository.findAll();
        List<Kategoria> kategorie = Lists.newArrayList(kategorieI);

        kategorie.forEach(k -> {
            k.getMaszyny().forEach(m -> {

                List<Norma> normyByMaszyna = normaRepository.findByMaszyna(m);

                normyByMaszyna.forEach(n -> {

                    RaportRoczny raportRoczny = new RaportRoczny();

                    String kategoria_jednostka =
                            new StringBuilder(k.getNazwa()).append("-").append(n.getJednostka()).toString();
                    raportRoczny.setKategoria_jednostka(kategoria_jednostka);

                    raportRoczny.setKategoria(k.getNazwa());
                    raportRoczny.setJednostka(n.getJednostka());

                    Double sumaYear = zuzycieService.getSumaYear(n.getId(), year);
                    raportRoczny.setSuma(sumaYear);

                    result.add(raportRoczny);

                });
            });
        });

        Map<String, List<RaportRoczny>> collect = result.stream().collect(groupingBy(RaportRoczny::getKategoria_jednostka));

        List<RaportRoczny> groupBy = new ArrayList<>();
        collect.forEach((s, lista) -> {
            double sum = lista.stream().mapToDouble(RaportRoczny::getSuma).sum();
            if(sum == 0) return;

            RaportRoczny raportRoczny = new RaportRoczny();

            raportRoczny.setKategoria_jednostka(s);
            raportRoczny.setKategoria(lista.get(0).getKategoria());
            raportRoczny.setJednostka(lista.get(0).getJednostka());
            raportRoczny.setSuma(myRound(sum, true));

            groupBy.add(raportRoczny);

        });

        return groupBy;
    }

    private double myRound(double d, boolean precisionMode){
        double r = (double) Math.round(d*100)/100;
        if(precisionMode){
            return r;
        }
        else{
            return (double) Math.round(r*10)/10;
        }
    }

}