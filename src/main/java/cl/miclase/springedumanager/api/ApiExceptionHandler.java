package cl.miclase.springedumanager.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador centralizado de errores para todos los controladores REST
 * del paquete {@code api}.
 *
 * <p>Traduce las excepciones de negocio a respuestas HTTP con formato
 * {@link ProblemDetail} (RFC 7807), en vez de dejar que Spring devuelva
 * una página de error HTML por defecto. No intercepta errores de
 * autorización: esos los maneja Spring Security respondiendo 401/403.</p>
 */
@RestControllerAdvice(basePackages = "cl.miclase.springedumanager.api")
public class ApiExceptionHandler {

    /**
     * Traduce una {@link IllegalArgumentException} (por ejemplo, un
     * recurso no encontrado) a una respuesta 404.
     *
     * @param ex excepción capturada
     * @return detalle del error en formato {@link ProblemDetail}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail notFound(IllegalArgumentException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Recurso no encontrado");
        return problem;
    }

    /**
     * Traduce un error de validación de {@code @Valid} a una respuesta 400.
     *
     * @param ex excepción capturada
     * @return detalle del error en formato {@link ProblemDetail}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Datos inválidos.");
        problem.setTitle("Error de validación");
        return problem;
    }
}