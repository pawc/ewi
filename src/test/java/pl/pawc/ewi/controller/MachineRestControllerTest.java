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
import pl.pawc.ewi.entity.Machine;

import jakarta.servlet.ServletContext;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class MachineRestControllerTest {

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
        assertNotNull(webApplicationContext.getBean("machineRestController"));
    }

    @Test
    void machineGetTest() throws Exception {

        mockMvc.perform(get("/machine").param("id", "C1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> {
                    Machine machine = objectMapper.readValue(result.getResponse().getContentAsString(), Machine.class);
                    assertNotNull(machine);
                    assertFalse(machine.getCategories().isEmpty());
                    assertEquals(2, machine.getFuelConsumptionStandards().size());
                });

    }

    @Test
    void machinePost() throws Exception {

        Machine machine = new Machine();
        machine.setId("C99");
        machine.setName("Test nazwa");
        machine.setDescription("Test opis");
        machine.setFuelConsumptionStandards(Collections.emptyList());

        requestJson = ow.writeValueAsString(machine);

        mockMvc.perform(post("/machine")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

        mockMvc.perform(post("/machine")
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        machine.setId("C1");
        requestJson = ow.writeValueAsString(machine);
        mockMvc.perform(post("/machine")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void machinePut() throws Exception {

        Machine machine = new Machine();
        machine.setId("C1");
        machine.setDescription("test opis");
        machine.setFuelConsumptionStandards(Collections.emptyList());

        requestJson = ow.writeValueAsString(machine);

        mockMvc.perform(put("/machine")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());

        mockMvc.perform(put("/machine")
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        machine.setId("C98");
        requestJson = ow.writeValueAsString(machine);
        mockMvc.perform(put("/machine")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

    }

}