package cl.miclase.springedumanager.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de las páginas de entrada de la aplicación.
 *
 * <p>Expone la página de bienvenida pública ({@code /}) y la página de
 * inicio del usuario ya autenticado ({@code /inicio}), que muestra el
 * menú de navegación hacia las demás secciones del sistema.</p>
 */
@Controller
public class HomeController {

    /**
     * Página de bienvenida pública, con acceso al login y a la activación
     * de cuenta.
     *
     * @return el nombre de la vista {@code index}
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /**
     * Página de inicio tras un login exitoso, con el menú de navegación.
     *
     * @return el nombre de la vista {@code inicio}
     */
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }
}