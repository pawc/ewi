package pl.pawc.ewi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.builder.SpringApplicationBuilder;

@ExtendWith(MockitoExtension.class)
class ServletInitializerTest {

    @InjectMocks
    ServletInitializer servletInitializer;

    @Mock
    SpringApplicationBuilder springApplicationBuilder;

    @Test
    void configure() {
        servletInitializer.configure(springApplicationBuilder);
        Mockito.verify(springApplicationBuilder, Mockito.times(1)).sources(EwiApplication.class);

    }

}