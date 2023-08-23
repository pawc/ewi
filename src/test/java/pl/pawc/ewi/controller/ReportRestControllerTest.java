package pl.pawc.ewi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ReportRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertNotNull(webApplicationContext.getBean("reportRestController"));
    }

    @Test
    void reportTest() throws Exception {

        mockMvc.perform(get("/report")
                .param("year", "2022")
                .param("month", "4"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/report"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void reportAnnualTest() throws Exception {

        mockMvc.perform(get("/reportAnnual")
                        .param("year", "2022"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/reportAnnual"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void reportMachineKilometersTest() throws Exception {

        mockMvc.perform(get("/getReportMachineKilometers")
                        .param("start", "2022-01-01")
                        .param("end", "2022-12-31")
                        .param("machineId", "C1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/getReportMachineKilometers")
                        .param("start", "2022-01-01")
                        .param("end", "test")
                        .param("machineId", "C1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/getReportMachineKilometers"))
                .andExpect(status().isBadRequest());

    }

}