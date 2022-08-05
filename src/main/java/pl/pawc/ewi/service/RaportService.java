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
import pl.pawc.ewi.repository.ZuzycieRepository;

import java.math.BigDecimal;
import java.math.MathContext;
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
    private final KategoriaRepository kategoriaRepository;
    private final ZuzycieService zuzycieService;
    private final StanRepository stanRepository;
    private final DokumentRepository dokumentRepository;
    private final NormaRepository normaRepository;
    private final ZuzycieRepository zuzycieRepository;
    private final MathContext m1 = new MathContext(2);
    private final MathContext m2 = new MathContext(3);

    public List<RaportKilometry> getKilometryRaport(int rok, int miesiac){

        List<Maszyna> allActive = maszynaService.findAllActive();
        List<RaportKilometry> result = new ArrayList<>();

        allActive.forEach(m -> {
            RaportKilometry raportKilometry = new RaportKilometry();
            raportKilometry.setMaszynaid(m.getId());
            raportKilometry.setMaszynanazwa(m.getNazwa());
            Kilometry oneByMaszynaAndRokAndMiesiac = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(m, rok, miesiac);
            if (oneByMaszynaAndRokAndMiesiac == null){
                raportKilometry.setStanpoczatkowy(BigDecimal.ZERO);
            }
            else{
                raportKilometry.setStanpoczatkowy(oneByMaszynaAndRokAndMiesiac.getWartosc());
            }
            result.add(raportKilometry);
        });

        return result;

    }

    public List<RaportRoczny> getRaportRoczny(int year){
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
                    raportRoczny.setKategoria_jednostka(kategoria_jednostka.toUpperCase());

                    raportRoczny.setKategoria(k.getNazwa());
                    raportRoczny.setJednostka(n.getJednostka());

                    BigDecimal sumaYear = zuzycieService.getSumaYear(n.getId(), year);
                    raportRoczny.setSuma(sumaYear);

                    result.add(raportRoczny);

                });
            });
        });

        Map<String, List<RaportRoczny>> collect = result.stream().collect(groupingBy(RaportRoczny::getKategoria_jednostka));

        List<RaportRoczny> groupBy = new ArrayList<>();
        collect.forEach((s, lista) -> {
            BigDecimal sum = lista.stream().map(RaportRoczny::getSuma).reduce(BigDecimal.ZERO, BigDecimal::add);
            if(BigDecimal.ZERO.equals(sum)) return;

            RaportRoczny raportRoczny = new RaportRoczny();

            raportRoczny.setKategoria_jednostka(s);
            raportRoczny.setKategoria(lista.get(0).getKategoria());
            raportRoczny.setJednostka(lista.get(0).getJednostka());
            raportRoczny.setSuma(sum.setScale(2, RoundingMode.HALF_UP));

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
            zuzycieRepository.findByDokument(d).forEach(z -> {

                BigDecimal suma = z.getWartosc().multiply(z.getNorma().getWartosc()).setScale(1, RoundingMode.HALF_UP);

                Raport raport = new Raport();
                raport.setMaszynaidnormaid(d.getMaszyna().getId()+"-"+z.getNorma().getId());
                raport.setMaszyna(d.getMaszyna().getNazwa() + "(" + d.getMaszyna().getId() + ")");
                raport.setMaszynaid(d.getMaszyna().getId());
                Kilometry kilometry = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(d.getMaszyna(), year, month);
                raport.setStankilometry(kilometry != null ? kilometry.getWartosc() : BigDecimal.ZERO);
                raport.setKilometry(d.getKilometry());
                raport.setKilometryprzyczepa(d.getKilometryPrzyczepa());
                raport.setJednostka(z.getNorma().getJednostka());
                raport.setSuma(suma);
                raport.setSumagodzin(z.getWartosc().setScale(1, RoundingMode.HALF_UP));
                raport.setZatankowano(z.getZatankowano().setScale(1, RoundingMode.HALF_UP));
                raport.setOgrzewanie(z.getOgrzewanie().setScale(1, RoundingMode.HALF_UP));
                raport.setNormaId(z.getNorma().getId());
                Stan stan = stanRepository.findOneByNormaAndRokAndMiesiac(z.getNorma(), year, month);
                BigDecimal stanPoprz = stan == null ? BigDecimal.ZERO : stan.getWartosc();
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

            BigDecimal zatankowano = list.stream().map(r -> r.getZatankowano().setScale(1, RoundingMode.HALF_UP)).reduce(BigDecimal.ZERO, BigDecimal::add);
            raport.setZatankowano(zatankowano);

            BigDecimal ogrzewanie = list.stream().map(r -> r.getOgrzewanie().setScale(1, RoundingMode.HALF_UP)).reduce(BigDecimal.ZERO, BigDecimal::add);
            raport.setOgrzewanie(ogrzewanie);

            BigDecimal sumagodzin = list.stream().map(r -> r.getSumagodzin().setScale(1, RoundingMode.HALF_UP)).reduce(BigDecimal.ZERO, BigDecimal::add);
            raport.setSumagodzin(sumagodzin.setScale(1, RoundingMode.HALF_UP));

            BigDecimal suma = new BigDecimal("0");
            for (Raport r : list) {
                suma = suma.add(r.getSuma());
            }
            raport.setSuma(suma);

            BigDecimal kilometry = list.stream().map(Raport::getKilometry).reduce(BigDecimal.ZERO, BigDecimal::add);
            raport.setKilometry(kilometry.setScale(2, RoundingMode.HALF_UP));

            BigDecimal kilometryPrzyczepa = list.stream().map(Raport::getKilometryprzyczepa).reduce(BigDecimal.ZERO, BigDecimal::add);
            raport.setKilometryprzyczepa(kilometryPrzyczepa.setScale(2, RoundingMode.HALF_UP));

            grouped.add(raport);
        }

        return grouped;
    }

}