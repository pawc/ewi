package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Kategoria;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.model.Raport;
import pl.pawc.ewi.model.RaportKilometry;
import pl.pawc.ewi.model.RaportRoczny;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.KilometryRepository;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.StanRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Component
@RequiredArgsConstructor
public class RaportService {

    private final KilometryRepository kilometryRepository;
    private final MaszynaService maszynaService;
    private final KategoriaService kategoriaService;
    private final ZuzycieService zuzycieService;
    private final StanRepository stanRepository;
    private final DokumentRepository dokumentRepository;
    private final NormaRepository normaRepository;
    private final ZuzycieRepository zuzycieRepository;

    public List<RaportKilometry> getKilometryRaport(int rok, int miesiac){

        List<Maszyna> allActive = maszynaService.findAllActive();
        List<RaportKilometry> result = new ArrayList<>();

        allActive.forEach(m -> {
            RaportKilometry raportKilometry = new RaportKilometry();
            raportKilometry.setMaszynaid(m.getId());
            raportKilometry.setMaszynanazwa(m.getNazwa());
            Kilometry oneByMaszynaAndRokAndMiesiac = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(m, rok, miesiac);
            if (oneByMaszynaAndRokAndMiesiac == null) raportKilometry.setStanpoczatkowy(BigDecimal.ZERO);
            else raportKilometry.setStanpoczatkowy(oneByMaszynaAndRokAndMiesiac.getWartosc());
            result.add(raportKilometry);
        });

        return result;

    }

    public List<RaportRoczny> getRaportRoczny(int year){
        List<RaportRoczny> result = getMaszynaNormaUngrouped(year);
        Map<String, List<RaportRoczny>> collect = result.stream().collect(groupingBy(RaportRoczny::getKategoria_jednostka));
        List<RaportRoczny> groupBy = new ArrayList<>();

        collect.forEach((s, lista) -> {
            BigDecimal sum = lista.stream().map(RaportRoczny::getSuma).reduce(BigDecimal.ZERO, BigDecimal::add);
            if(BigDecimal.ZERO.equals(sum)) return;

            RaportRoczny raportRoczny = new RaportRoczny();
            raportRoczny.setKategoria_jednostka(s);
            raportRoczny.setKategoria(lista.get(0).getKategoria());
            raportRoczny.setJednostka(lista.get(0).getJednostka());
            BigDecimal waga = lista.get(0).getWaga();
            raportRoczny.setWaga(waga);
            BigDecimal suma = sum.setScale(2, RoundingMode.HALF_UP);
            raportRoczny.setSuma(suma);
            raportRoczny.setSumaRazyWaga(suma.multiply(waga));

            groupBy.add(raportRoczny);

        });

        return groupBy;
    }

    private List<RaportRoczny> getMaszynaNormaUngrouped(int year) {
        List<RaportRoczny> result = new ArrayList<>();

        for (Kategoria k : kategoriaService.findAll()) {
            for (Maszyna m : k.getMaszyny()) {
                normaRepository.findByMaszyna(m).forEach(n -> getRaportRocznyNormaByMaszyna(year, result, k, n));
            }
        }
        return result;
    }

    private void getRaportRocznyNormaByMaszyna(int year, List<RaportRoczny> result, Kategoria k, Norma n) {
        RaportRoczny raportRoczny = new RaportRoczny();

        String jednostka = n.getJednostkaObj() == null ? n.getJednostka() : n.getJednostkaObj().getNazwa();
        BigDecimal waga = n.getJednostkaObj() == null ? BigDecimal.ONE : n.getJednostkaObj().getWaga();

        String kategoriaJednostka = new StringBuilder(k.getNazwa()).append("-").append(jednostka).toString();
        raportRoczny.setKategoria_jednostka(kategoriaJednostka.toUpperCase());

        raportRoczny.setKategoria(k.getNazwa());
        raportRoczny.setJednostka(jednostka);
        raportRoczny.setWaga(waga);

        BigDecimal sumaYear = zuzycieService.getSumaYear(n.getId(), year);
        raportRoczny.setSuma(sumaYear);

        result.add(raportRoczny);
    }

    public List<Raport> getRaport(int year, int month, boolean isQuarterly) {
        Calendar cal = Calendar.getInstance();

        if(isQuarterly) month = getFirstMonthOfQuarter(month);
        cal.set(year, month-1, 1);

        Date firstDayOfMonth = cal.getTime();
        cal.add(Calendar.MONTH, isQuarterly ? 3 : 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDayOfMonth = cal.getTime();

        List<Dokument> dokumentsByDataBetween = dokumentRepository.findByDataBetween(firstDayOfMonth, lastDayOfMonth);

        Iterable<Norma> normy = normaRepository.findAll();

        List<Raport> results = new ArrayList<>();

        for (Norma norma : normy) {

            List<Dokument> dokumentList  = dokumentsByDataBetween.stream().filter(d -> d.getMaszyna().equals(norma.getMaszyna())).toList();
            if(dokumentList.isEmpty()) continue;

            int scale = norma.isCzyZaokr1setna() ? 2 : 1;

            BigDecimal sumaWartosc = BigDecimal.ZERO;
            BigDecimal sumaOgrzewanie = BigDecimal.ZERO;
            BigDecimal sumaTankowanie = BigDecimal.ZERO;
            BigDecimal sumaKilometry = BigDecimal.ZERO;
            BigDecimal sumaKilometryPrzyczepa = BigDecimal.ZERO;
            BigDecimal sumaGodzin = BigDecimal.ZERO;

            for (Dokument d : dokumentList) {
                Zuzycie zuzycie = zuzycieRepository.findByDokument(d).stream().filter(z -> z.getNorma().equals(norma)).findFirst().orElse(null);
                if(zuzycie == null) continue;
                sumaWartosc = sumaWartosc.add(zuzycie.getWartosc().multiply(norma.getWartosc()).setScale(scale, RoundingMode.HALF_UP));
                sumaOgrzewanie = sumaOgrzewanie.add(zuzycie.getOgrzewanie());
                sumaTankowanie = sumaTankowanie.add(zuzycie.getZatankowano());
                sumaKilometry = sumaKilometry.add(d.getKilometry());
                sumaKilometryPrzyczepa = sumaKilometryPrzyczepa.add(d.getKilometryPrzyczepa());
                sumaGodzin = sumaGodzin.add(zuzycie.getWartosc()).setScale(scale, RoundingMode.HALF_UP);
            }

            Stan stan = stanRepository.findOneByNormaAndRokAndMiesiac(norma, year, month);
            BigDecimal stanPoprz = stan == null ? BigDecimal.ZERO : stan.getWartosc();

            BigDecimal endState = stanPoprz.subtract(sumaWartosc).subtract(sumaOgrzewanie).add(sumaTankowanie).setScale(scale, RoundingMode.HALF_UP);

            Kilometry kilometry = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(norma.getMaszyna(), year, month);
            BigDecimal stanKilometry = kilometry != null ? kilometry.getWartosc() : BigDecimal.ZERO;
            BigDecimal endStateKilometry = stanKilometry.add(sumaKilometry);

            Raport raport = new Raport();
            raport.setMaszynaidnormaid(norma.getMaszyna().getId()+"-"+norma.getId());
            raport.setMaszyna(norma.getMaszyna().getNazwa() + "(" + norma.getMaszyna().getId() + ")");
            raport.setMaszynaid(norma.getMaszyna().getId());
            raport.setStankilometry(stanKilometry);
            raport.setKilometry(sumaKilometry);
            raport.setEndStateKilometry(endStateKilometry);
            raport.setKilometryprzyczepa(sumaKilometryPrzyczepa);
            raport.setJednostka(norma.getJednostkaObj() == null ? norma.getJednostka() : norma.getJednostkaObj().getNazwa());
            raport.setSuma(sumaWartosc);
            raport.setSumagodzin(sumaGodzin);
            raport.setZatankowano(sumaTankowanie);
            raport.setOgrzewanie(sumaOgrzewanie);
            raport.setNormaId(norma.getId());
            raport.setStanPoprz(stanPoprz);
            raport.setEndState(endState);
            results.add(raport);

        }

        return results;

    }

    private int getFirstMonthOfQuarter(int quarter){
        if(quarter == 1) return 1;
        if(quarter == 2) return 4;
        if(quarter == 3) return 6;
        else return 9;
    }

}