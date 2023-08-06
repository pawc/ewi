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
import pl.pawc.ewi.entity.Unit;

import jakarta.servlet.ServletContext;
import pl.pawc.ewi.repository.UnitRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UnitRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    UnitRepository unitRepository;

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
        assertNotNull(webApplicationContext.getBean("unitRestController"));
    }

    @Test
    @Order(1)
    void jednostkaPostTest() throws Exception {

        Unit unit = new Unit("L/H");
        unit.setName("ET/H");
        unit.setWeightRatio(BigDecimal.valueOf(1.23));
        requestJson = ow.writeValueAsString(unit);

        mockMvc.perform(post("/jednostka")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
            .andExpect(status().isOk());

    }

    @Test
    @Order(2)
    void jednostkiGetTest() throws Exception {

        mockMvc.perform(get("/jednostkiGet"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(result -> {
                List<Unit> jednostki = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                assertEquals(1, jednostki.size());
            });

    }

}