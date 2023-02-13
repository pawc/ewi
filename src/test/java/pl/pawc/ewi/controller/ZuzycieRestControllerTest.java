package pl.pawc.ewi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class ZuzycieRestControllerTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertNotNull(webApplicationContext.getBean("zuzycieRestController"));
    }

    @Test
    void calcTest() throws Exception {

        mockMvc.perform(get("/calc")
                    .param("before", "1")
                    .param("norma", "2")
                    .param("normaVal", "3"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(result -> {
                String response = result.getResponse().getContentAsString();
                assertEquals("[-5.0,6.0]", response);
            });

    }
    
}