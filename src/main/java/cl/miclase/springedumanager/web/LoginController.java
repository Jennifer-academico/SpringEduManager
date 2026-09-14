package cl.miclase.springedumanager.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador que expone la vista del formulario de login.
 *
 * <p>El procesamiento real de las credenciales (RUT y contraseña) no
 * ocurre aquí: lo maneja directamente Spring Security mediante la
 * configuración de {@code formLogin()} en {@link cl.miclase.springedumanager.security.SecurityConfig}.
 * Este controlador solo se encarga de mostrar el formulario.</p>
 */
@Controller
public class LoginController {

    /**
     * Muestra el formulario de inicio de sesión.
     *
     * @return el nombre de la vista {@code login}
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}