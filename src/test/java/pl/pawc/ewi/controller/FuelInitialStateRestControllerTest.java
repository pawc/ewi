package pl.pawc.ewi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.pawc.ewi.entity.FuelInitialState;
import pl.pawc.ewi.entity.FuelConsumptionStandard;
import pl.pawc.ewi.model.FuelInitialStateReport;

import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class FuelInitialStateRestControllerTest {

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
        assertNotNull(webApplicationContext.getBean("fuelInitialStateRestController"));
    }

    @Test
    @Transactional
    void stanPostPutTest() throws Exception {
        FuelConsumptionStandard fuelConsumptionStandard = new FuelConsumptionStandard();
        fuelConsumptionStandard.setId(1);
        assertNotNull(fuelConsumptionStandard.toString());
        assertNotNull(fuelConsumptionStandard.hashCode());

        FuelInitialState fuelInitialState = new FuelInitialState();
        fuelInitialState.setId(102);
        fuelInitialState.setFuelConsumptionStandard(fuelConsumptionStandard);
        fuelInitialState.setValue(BigDecimal.valueOf(0));
        fuelInitialState.setYear(2023);
        fuelInitialState.setMonth(2);

        requestJson = ow.writeValueAsString(fuelInitialState);

        mockMvc.perform(put("/fuelInitialState")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

        List<FuelInitialState> stany = List.of(fuelInitialState);
        requestJson = ow.writeValueAsString(stany);

        mockMvc.perform(post("/fuelInitialStates")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

    }

    @Test
    void stanyGetTest() throws Exception {

        mockMvc.perform(get("/fuelInitialStateReport")
                .param("year", "2022")
                .param("month", "4"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(result -> {
                List<FuelInitialStateReport> stany = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                assertEquals(4, stany.size());
            });

    }

}