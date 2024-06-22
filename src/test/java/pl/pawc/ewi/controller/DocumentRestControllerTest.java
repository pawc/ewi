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
import pl.pawc.ewi.entity.Document;
import pl.pawc.ewi.entity.FuelConsumption;
import pl.pawc.ewi.entity.FuelConsumptionStandard;
import pl.pawc.ewi.entity.Machine;
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
class DocumentRestControllerTest {

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
        assertNotNull(webApplicationContext.getBean("documentRestController"));
    }

    @Test
    @Order(1)
    void documentGetTest() throws Exception {

        mockMvc.perform(get("/document")
                        .param("number", "1/04/2022/C1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    Document document = objectMapper.readValue(result.getResponse().getContentAsString(), Document.class);
                    assertNotNull(document);
                    assertEquals("1/04/2022/C1", document.getNumber());
                    assertNotNull(document.getMachine());
                    assertEquals("C1", document.getMachine().getId());
                    assertEquals(new BigDecimal("10.0"), document.getKilometers());
                    assertEquals(new BigDecimal("10.0"), document.getKilometersTrailer());
                });

        mockMvc.perform(get("/document")
                        .param("number", "99/04/2022/C1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    Document document = objectMapper.readValue(result.getResponse().getContentAsString(), Document.class);
                    assertNotNull(document);
                    assertNull(document.getNumber());
                    assertNull(document.getMachine());
                    assertNull(document.getKilometers());
                    assertNull(document.getKilometersTrailer());
                });

    }

    @Test
    @Order(2)
    void documentsGetTest() throws Exception {

        mockMvc.perform(get("/documents")
                        .param("year", "2022")
                        .param("month", "4")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    List<Document> documenty = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                    assertEquals(17, documenty.size());
                });

    }

    @Test
    @Order(3)
    void documentPostTest() throws Exception {

        Machine machine = new Machine();
        machine.setId("C1");
        FuelConsumption fuelConsumption = new FuelConsumption();
        fuelConsumption.setValue(BigDecimal.ZERO);
        fuelConsumption.setHeating(BigDecimal.ZERO);
        fuelConsumption.setRefueled(BigDecimal.ZERO);
        FuelConsumptionStandard fuelConsumptionStandard = new FuelConsumptionStandard();
        fuelConsumptionStandard.setId(1);
        fuelConsumption.setFuelConsumptionStandard(fuelConsumptionStandard);

        Document document = new Document();
        document.setNumber("80/09/2022/C1");
        document.setKilometers(BigDecimal.ZERO);
        document.setKilometersTrailer(BigDecimal.ZERO);
        document.setDate(new Date(System.currentTimeMillis()));
        document.setMachine(machine);
        document.setFuelConsumption(List.of(fuelConsumption));

        requestJson = ow.writeValueAsString(document);

        mockMvc.perform(post("/document")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

    }
    @Test
    @Order(4)
    void documentPutTest() throws Exception {

        Machine machine = new Machine();
        machine.setId("C1");
        FuelConsumption fuelConsumption = new FuelConsumption();
        fuelConsumption.setId(123);
        fuelConsumption.setValue(BigDecimal.valueOf(13.34));
        fuelConsumption.setHeating(BigDecimal.valueOf(57.78));
        fuelConsumption.setRefueled(BigDecimal.valueOf(99.98));
        FuelConsumptionStandard fuelConsumptionStandard = new FuelConsumptionStandard();
        fuelConsumptionStandard.setId(1);
        fuelConsumption.setFuelConsumptionStandard(fuelConsumptionStandard);

        Document document = new Document();
        document.setKilometers(BigDecimal.valueOf(10.0));
        document.setKilometersTrailer(BigDecimal.valueOf(10.0));
        document.setDate(new Date(System.currentTimeMillis()));
        document.setMachine(machine);
        document.setFuelConsumption(Collections.EMPTY_LIST);

        document.setNumber("81/09/2022/C1");
        requestJson = ow.writeValueAsString(document);

        mockMvc.perform(put("/document")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        document.setNumber("80/09/2022/C1");
        requestJson = ow.writeValueAsString(document);

        mockMvc.perform(put("/document")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

    }

    @Test
    @Order(5)
    void documentDeleteTest() throws Exception {

        mockMvc.perform(delete("/document")
                        .param("number", "80/09/2022/C1"))
                .andExpect(status().isOk());

    }

}