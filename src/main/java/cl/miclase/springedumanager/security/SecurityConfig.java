package cl.miclase.springedumanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import cl.miclase.springedumanager.repository.PersonaRepository;

/**
 * Configuración central de Spring Security para SpringEduManager.
 *
 * <p>Autenticación: se usa el <b>RUT</b> de la {@link cl.miclase.springedumanager.domain.Persona}
 * como nombre de usuario (no el email), y solo pueden iniciar sesión las
 * personas que ya activaron su cuenta (tienen {@code passwordHash} no nulo).</p>
 *
 * <p>Autorización: se combina configuración a nivel de URL (este archivo)
 * con anotaciones {@code @PreAuthorize} a nivel de método en los
 * controladores, habilitadas mediante {@link EnableMethodSecurity}. Las
 * reglas generales son:</p>
 * <ul>
 *   <li>Rutas públicas: inicio, login, activación de cuenta y consola H2.</li>
 *   <li>Consultas (GET) de cursos, prácticas y evaluaciones: cualquier
 *       persona autenticada, sin importar su rol.</li>
 *   <li>Creación de cursos (POST): solo ADMIN.</li>
 *   <li>Creación de prácticas y evaluaciones (POST): ADMIN o DOCENTE.</li>
 *   <li>Gestión de personas: solo ADMIN.</li>
 * </ul>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Codificador de contraseñas usado tanto para activar cuentas como
     * para validar el login.
     *
     * @return una instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Servicio que Spring Security usa para cargar los datos de
     * autenticación de una persona a partir de su RUT.
     *
     * <p>Solo se consideran válidas las personas que ya tienen una
     * contraseña asignada (cuenta activa); si no la tienen, se rechaza
     * el intento de login como si el usuario no existiera.</p>
     *
     * @param repository repositorio para buscar la persona por RUT
     * @return el {@link UserDetailsService} usado por el flujo de login
     */
    @Bean
    UserDetailsService userDetailsService(PersonaRepository repository) {
        return rut -> repository.findByRut(rut)
            .filter(persona -> persona.getPasswordHash() != null)
            .map(persona -> User.withUsername(persona.getRut())
                .password(persona.getPasswordHash())
                .roles(persona.getRol().name())
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o cuenta no activada."));
    }

    /**
     * Define la cadena de filtros de seguridad: qué rutas son públicas,
     * cuáles requieren autenticación, cuáles requieren un rol específico,
     * y cómo se comportan el login y el logout.
     *
     * @param http configurador de seguridad HTTP provisto por Spring
     * @return la cadena de filtros configurada
     * @throws Exception si ocurre un error al construir la configuración
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/activar-cuenta", "/activar-cuenta/**", "/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/cursos/**", "/practicas/**", "/evaluaciones/**", "/inicio").authenticated()
                .requestMatchers(HttpMethod.POST, "/cursos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/practicas/**", "/evaluaciones/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers("/personas/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("username")
                .defaultSuccessUrl("/inicio", true)
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/**"))
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}