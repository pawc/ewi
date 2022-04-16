package pl.pawc.ewi.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Kategoria;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.model.Raport;
import pl.pawc.ewi.model.RaportKilometry;
import pl.pawc.ewi.model.RaportRoczny;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.KategoriaRepository;
import pl.pawc.ewi.repository.KilometryRepository;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.StanRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Component
@RequiredArgsConstructor
public class RaportService {

    private final KilometryRepository kilometryRepository;
    private final MaszynaService maszynaService;
    private final KategoriaRepository kategoriaRepository;
    private final ZuzycieService zuzycieService;
    private final StanRepository stanRepository;
    private final DokumentRepository dokumentRepository;
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

    public List<Raport> getRaport(int year, int month){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, 1);
        Date firstDayOfMonth = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDayOfMonth = cal.getTime();

        List<Dokument> dokumentsByDataBetween = dokumentRepository.findByDataBetween(firstDayOfMonth, lastDayOfMonth);

        List<Raport> results = new ArrayList<>();

        dokumentsByDataBetween.forEach(d -> {
            d.getZuzycie().forEach(z -> {
                Raport raport = new Raport();
                raport.setMaszynaidnormaid(d.getMaszyna().getId()+"-"+z.getNorma().getId());
                raport.setMaszyna(d.getMaszyna().getNazwa() + "(" + d.getMaszyna().getId() + ")");
                raport.setMaszynaid(d.getMaszyna().getId());
                Kilometry kilometry = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(d.getMaszyna(), year, month);
                raport.setStankilometry(kilometry != null ? kilometry.getWartosc() : 0);
                raport.setKilometry(d.getKilometry());
                raport.setKilometryprzyczepa(d.getKilometryPrzyczepa());
                raport.setJednostka(z.getNorma().getJednostka());
                raport.setSuma(myRound(z.getWartosc() * z.getNorma().getWartosc(), false));
                raport.setSumagodzin(myRound(z.getWartosc(), false));
                raport.setZatankowano(myRound(z.getZatankowano(), false));
                raport.setOgrzewanie(myRound(z.getOgrzewanie(), false));
                raport.setNormaId(z.getNorma().getId());
                Stan stan = stanRepository.findOneByNormaAndRokAndMiesiac(z.getNorma(), year, month);
                double stanPoprz = stan == null ? 0 : stan.getWartosc();
                raport.setStanPoprz(stanPoprz);
                results.add(raport);
            });
        });

        Map<String, List<Raport>> collect = results.stream().collect(groupingBy(Raport::getMaszynaidnormaid));

        List<Raport> grouped = new ArrayList<>();

        for(String maszynaIdNormaId : collect.keySet()){

            List<Raport> list = collect.get(maszynaIdNormaId);
            Raport raportMain = list.get(0);

            Raport raport = new Raport();

            raport.setMaszynaidnormaid(maszynaIdNormaId);
            raport.setMaszyna(raportMain.getMaszyna());
            raport.setMaszynaid(raportMain.getMaszynaid());
            raport.setNormaId(raportMain.getNormaId());
            raport.setJednostka(raportMain.getJednostka());
            raport.setStankilometry(raportMain.getStankilometry());
            raport.setStanPoprz(raportMain.getStanPoprz());

            double zatankowano = list.stream().mapToDouble(r -> myRound(r.getZatankowano(), false)).sum();
            raport.setZatankowano(zatankowano);

            double ogrzewanie = list.stream().mapToDouble(r -> myRound(r.getOgrzewanie(), false)).sum();
            raport.setOgrzewanie(ogrzewanie);

            double sumagodzin = list.stream().mapToDouble(r -> myRound(r.getSumagodzin(), false)).sum();
            raport.setSumagodzin(sumagodzin);

            double suma = list.stream().mapToDouble(r -> myRound(r.getSuma(), false)).sum();
            raport.setSuma(suma);

            double kilometry = list.stream().mapToDouble(Raport::getKilometry).sum();
            raport.setKilometry(myRound(kilometry, true));

            double kilometryPrzyczepa = list.stream().mapToDouble(Raport::getKilometryprzyczepa).sum();
            raport.setKilometryprzyczepa(myRound(kilometryPrzyczepa, true));

            grouped.add(raport);
        }

        return grouped;
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