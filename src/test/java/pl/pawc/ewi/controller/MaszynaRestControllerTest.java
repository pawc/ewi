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
import pl.pawc.ewi.entity.Maszyna;

import jakarta.servlet.ServletContext;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class MaszynaRestControllerTest {

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
        assertNotNull(webApplicationContext.getBean("maszynaRestController"));
    }

    @Test
    void maszynaGetTest() throws Exception {

        mockMvc.perform(get("/maszyna").param("id", "C1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    Maszyna maszyna = objectMapper.readValue(result.getResponse().getContentAsString(), Maszyna.class);
                    assertNotNull(maszyna);
                    assertFalse(maszyna.getKategorie().isEmpty());
                    assertEquals(2, maszyna.getNormy().size());
                });

    }

    @Test
    void maszynaPost() throws Exception {

        Maszyna maszyna = new Maszyna();
        maszyna.setId("C99");
        maszyna.setNazwa("Test nazwa");
        maszyna.setOpis("Test opis");
        maszyna.setNormy(Collections.emptyList());

        requestJson = ow.writeValueAsString(maszyna);

        mockMvc.perform(post("/maszyna")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

        mockMvc.perform(post("/maszyna")
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        maszyna.setId("C1");
        requestJson = ow.writeValueAsString(maszyna);
        mockMvc.perform(post("/maszyna")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void maszynaPut() throws Exception {

        Maszyna maszyna = new Maszyna();
        maszyna.setId("C1");
        maszyna.setOpis("test opis");
        maszyna.setNormy(Collections.emptyList());

        requestJson = ow.writeValueAsString(maszyna);

        mockMvc.perform(put("/maszyna")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

        mockMvc.perform(put("/maszyna")
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        maszyna.setId("C98");
        requestJson = ow.writeValueAsString(maszyna);
        mockMvc.perform(put("/maszyna")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

    }

}