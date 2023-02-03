package pl.pawc.ewi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.pawc.ewi.entity.Dokument;

import javax.servlet.ServletContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class DokumentRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertNotNull(webApplicationContext.getBean("dokumentRestController"));
    }

    @Test
    void dokumentGet() throws Exception {

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
    void dokumentyGet() throws Exception {

        mockMvc.perform(get("/dokumentyGet")
                .param("rok", "2022")
                .param("miesiac", "4")
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(result -> {
                System.out.println(result);
                List<Dokument> dokumenty = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                assertEquals(17, dokumenty.size());
            });

    }

}