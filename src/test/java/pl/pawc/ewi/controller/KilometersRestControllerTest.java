package pl.pawc.ewi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.pawc.ewi.entity.Kilometers;
import pl.pawc.ewi.entity.Machine;
import pl.pawc.ewi.model.KilometersReport;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class KilometersRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private ObjectWriter ow;

    private String requestJson;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        ServletContext servletContext = webApplicationContext.getServletContext();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = objectMapper.writer().withDefaultPrettyPrinter();
        assertNotNull(servletContext);
        assertNotNull(webApplicationContext.getBean("kilometersRestController"));
    }

    @Test
    void stanyGetTest() throws Exception {

        mockMvc.perform(get("/kilometryGet")
                        .param("rok", "2022")
                        .param("miesiac", "4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    List<KilometersReport> kilometry = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                    assertNotNull(kilometry);
                    assertFalse(kilometry.isEmpty());
                    assertEquals(2, kilometry.size());
                });

    }

    @Test
    @Transactional
    void kilometryListTest() throws Exception {
        Kilometers kilometers = new Kilometers();
        kilometers.setYear(2023);
        kilometers.setMonth(2);
        Machine machine = new Machine();
        machine.setId("C1");
        kilometers.setMachine(machine);
        kilometers.setValue(BigDecimal.valueOf(99.99));

        requestJson = ow.writeValueAsString(List.of(kilometers));

        mockMvc.perform(post("/kilometryList")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

        kilometers.setYear(2022);
        kilometers.setMonth(4);
        kilometers.setValue(BigDecimal.valueOf(120));
        requestJson = ow.writeValueAsString(List.of(kilometers));

        mockMvc.perform(post("/kilometryList")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

    }

    @Test
    @Transactional
    void kilometryTest() throws Exception {
        Kilometers kilometers = new Kilometers();
        kilometers.setYear(2023);
        kilometers.setMonth(2);
        Machine machine = new Machine();
        machine.setId("C1");
        kilometers.setMachine(machine);
        kilometers.setValue(BigDecimal.valueOf(99.99));

        requestJson = ow.writeValueAsString(kilometers);

        mockMvc.perform(post("/kilometry")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

        kilometers.setYear(2022);
        kilometers.setMonth(4);
        kilometers.setValue(BigDecimal.valueOf(120));
        requestJson = ow.writeValueAsString(kilometers);

        mockMvc.perform(post("/kilometry")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

    }

}