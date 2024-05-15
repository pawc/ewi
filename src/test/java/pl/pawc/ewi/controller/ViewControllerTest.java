package pl.pawc.ewi.controller;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ViewControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void viewControllerTest() {

        List.of("home", "quarterlyReport", "annualReport", "machineKilometersReport", "documentsView",
                "machines", "unitsView", "initialStatesView", "kilometersView", "categories")
            .forEach(this::testView);
    }

    private void testView(String path) {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/" + path)
                    .with(csrf()))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail();
        }
    }

}