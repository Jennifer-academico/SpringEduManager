package cl.miclase.springedumanager.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void notFound_devuelveStatus404ConMensaje() {
        ProblemDetail problem = handler.notFound(new IllegalArgumentException("No existe"));

        assertEquals(HttpStatus.NOT_FOUND.value(), problem.getStatus());
        assertEquals("No existe", problem.getDetail());
        assertEquals("Recurso no encontrado", problem.getTitle());
    }
}