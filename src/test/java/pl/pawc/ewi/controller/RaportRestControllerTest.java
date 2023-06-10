package pl.pawc.ewi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class RaportRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertNotNull(webApplicationContext.getBean("raportRestController"));
    }

    @Test
    void raportTest() throws Exception {

        mockMvc.perform(get("/raport")
                .param("rok", "2022")
                .param("miesiac", "4"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/raport"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void raportRocznyTest() throws Exception {

        mockMvc.perform(get("/raportRoczny")
                        .param("rok", "2022"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/raportRoczny"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void raportMaszynaKilometryTest() throws Exception {

        mockMvc.perform(get("/getRaportMaszynaKilometry")
                        .param("start", "2022-01-01")
                        .param("end", "2022-12-31")
                        .param("maszynaId", "C1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/getRaportMaszynaKilometry")
                        .param("start", "2022-01-01")
                        .param("end", "test")
                        .param("maszynaId", "C1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/getRaportMaszynaKilometry"))
                .andExpect(status().isBadRequest());

    }

}