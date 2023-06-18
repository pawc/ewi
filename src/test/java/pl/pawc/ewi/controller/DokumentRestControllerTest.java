package pl.pawc.ewi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Zuzycie;

import jakarta.servlet.ServletContext;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DokumentRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private ObjectWriter ow;
    private String requestJson;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        ServletContext servletContext = webApplicationContext.getServletContext();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = objectMapper.writer().withDefaultPrettyPrinter();
        assertNotNull(servletContext);
        assertNotNull(webApplicationContext.getBean("dokumentRestController"));
    }

    @Test
    @Order(1)
    void dokumentGetTest() throws Exception {

        mockMvc.perform(get("/dokument")
                        .param("numer", "1/04/2022/C1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    Dokument dokument = objectMapper.readValue(result.getResponse().getContentAsString(), Dokument.class);
                    assertNotNull(dokument);
                    assertEquals("1/04/2022/C1", dokument.getNumer());
                    assertNotNull(dokument.getMaszyna());
                    assertEquals("C1", dokument.getMaszyna().getId());
                    assertEquals(new BigDecimal("10.0"), dokument.getKilometry());
                    assertEquals(new BigDecimal("10.0"), dokument.getKilometryPrzyczepa());
                });

        mockMvc.perform(get("/dokument")
                        .param("numer", "99/04/2022/C1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    Dokument dokument = objectMapper.readValue(result.getResponse().getContentAsString(), Dokument.class);
                    assertNotNull(dokument);
                    assertNull(dokument.getNumer());
                    assertNull(dokument.getMaszyna());
                    assertNull(dokument.getMaszyna());
                    assertNull(dokument.getKilometry());
                    assertNull(dokument.getKilometryPrzyczepa());
                });

    }

    @Test
    @Order(2)
    void dokumentyGetTest() throws Exception {

        mockMvc.perform(get("/dokumentyGet")
                        .param("rok", "2022")
                        .param("miesiac", "4")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    List<Dokument> dokumenty = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                    assertEquals(17, dokumenty.size());
                });

    }

    @Test
    @Order(3)
    void dokumentPostTest() throws Exception {

        Maszyna maszyna = new Maszyna();
        maszyna.setId("C1");
        Zuzycie zuzycie = new Zuzycie();
        zuzycie.setWartosc(BigDecimal.ZERO);
        zuzycie.setOgrzewanie(BigDecimal.ZERO);
        zuzycie.setZatankowano(BigDecimal.ZERO);
        Norma norma = new Norma();
        norma.setId(1);
        zuzycie.setNorma(norma);

        Dokument dokument = new Dokument();
        dokument.setNumer("80/09/2022/C1");
        dokument.setKilometry(BigDecimal.ZERO);
        dokument.setKilometryPrzyczepa(BigDecimal.ZERO);
        dokument.setData(new Date(System.currentTimeMillis()));
        dokument.setMaszyna(maszyna);
        dokument.setZuzycie(List.of(zuzycie));

        requestJson = ow.writeValueAsString(dokument);

        mockMvc.perform(post("/dokument")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

    }
    @Test
    @Order(4)
    void dokumentPutTest() throws Exception {

        Maszyna maszyna = new Maszyna();
        maszyna.setId("C1");
        Zuzycie zuzycie = new Zuzycie();
        zuzycie.setId(123);
        zuzycie.setWartosc(BigDecimal.valueOf(13.34));
        zuzycie.setOgrzewanie(BigDecimal.valueOf(57.78));
        zuzycie.setZatankowano(BigDecimal.valueOf(99.98));
        Norma norma = new Norma();
        norma.setId(1);
        zuzycie.setNorma(norma);

        Dokument dokument = new Dokument();
        dokument.setKilometry(BigDecimal.valueOf(10.0));
        dokument.setKilometryPrzyczepa(BigDecimal.valueOf(10.0));
        dokument.setData(new Date(System.currentTimeMillis()));
        dokument.setMaszyna(maszyna);
        dokument.setZuzycie(Collections.EMPTY_LIST);

        dokument.setNumer("81/09/2022/C1");
        requestJson = ow.writeValueAsString(dokument);

        mockMvc.perform(put("/dokument")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        dokument.setNumer("80/09/2022/C1");
        requestJson = ow.writeValueAsString(dokument);

        mockMvc.perform(put("/dokument")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

    }

    @Test
    @Order(5)
    void dokumentDeleteTest() throws Exception {

/*        mockMvc.perform(delete("/dokument")
                        .param("numer", "80/04/2022/C1"))
                .andExpect(status().isBadRequest());*/

        mockMvc.perform(delete("/dokument")
                        .param("numer", "80/09/2022/C1"))
                .andExpect(status().isOk());

    }

}