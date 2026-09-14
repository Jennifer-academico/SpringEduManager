package cl.miclase.springedumanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Punto de entrada de SpringEduManager.
 *
 * <p>Proyecto desarrollado para el Módulo 6: Desarrollo de aplicaciones
 * JEE con Spring Framework. Integra Spring MVC, Spring Data JPA, Spring
 * Security y una API REST sobre un dominio de gestión académica con tres
 * roles: administrador, docente y estudiante.</p>
 *
 * <p>Extiende {@link SpringBootServletInitializer} para poder ejecutarse
 * con el Tomcat embebido durante el desarrollo (botón Play del IDE) y,
 * si se necesita, empaquetarse como WAR para un servidor Tomcat 10.1+
 * externo.</p>
 */
@SpringBootApplication
public class SpringEduManagerApplication extends SpringBootServletInitializer {

    /**
     * Arranca la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringEduManagerApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringEduManagerApplication.class);
    }
}