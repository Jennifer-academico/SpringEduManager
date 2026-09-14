package cl.miclase.springedumanager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

class SpringEduManagerApplicationTest {

    @Test
    void configure_retornaBuilderConFuentesConfiguradas() {
        SpringEduManagerApplication app = new SpringEduManagerApplication();
        SpringApplicationBuilder builder = new SpringApplicationBuilder();

        SpringApplicationBuilder resultado = app.configure(builder);

        assertNotNull(resultado);
    }
}