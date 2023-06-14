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
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.model.RaportStan;

import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class StanRestControllerTest {

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
        assertNotNull(webApplicationContext.getBean("stanRestController"));
    }

    @Test
    @Transactional
    void stanPostPutTest() throws Exception {
        Norma norma = new Norma();
        norma.setId(1);
        assertNotNull(norma.toString());
        assertNotNull(norma.hashCode());

        Stan stan = new Stan();
        stan.setId(102);
        stan.setNorma(norma);
        stan.setWartosc(BigDecimal.valueOf(0));
        stan.setRok(2023);
        stan.setMiesiac(2);

        requestJson = ow.writeValueAsString(stan);

        mockMvc.perform(put("/stan")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

        List stany = Arrays.asList(stan);
        requestJson = ow.writeValueAsString(stany);

        mockMvc.perform(post("/stany")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

    }

    @Test
    void stanyGetTest() throws Exception {

        mockMvc.perform(get("/stanyGet")
                .param("rok", "2022")
                .param("miesiac", "4"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(result -> {
                List<RaportStan> stany = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                assertEquals(4, stany.size());
            });

    }

}