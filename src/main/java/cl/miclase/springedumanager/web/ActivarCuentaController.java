package cl.miclase.springedumanager.web;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import cl.miclase.springedumanager.dto.ActivarCuentaForm;
import cl.miclase.springedumanager.service.PersonaService;

/**
 * Controlador público que permite a una persona ya precargada por el
 * administrador activar su propia cuenta, definiendo una contraseña.
 *
 * <p>La persona debe conocer su RUT (dato ya ingresado por el
 * administrador); si el RUT no existe o ya tiene una cuenta activa, se
 * muestra un mensaje de error en lugar de completar la activación.</p>
 */
@Controller
@RequestMapping("/activar-cuenta")
public class ActivarCuentaController {

    private final PersonaService service;

    public ActivarCuentaController(PersonaService service) {
        this.service = service;
    }

    /**
     * Muestra el formulario de activación de cuenta.
     *
     * @param model modelo de la vista
     * @return el nombre de la vista {@code activar-cuenta}
     */
    @GetMapping
    public String mostrar(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new ActivarCuentaForm());
        }
        return "activar-cuenta";
    }

    /**
     * Procesa el formulario de activación: valida los datos y, si el RUT
     * corresponde a una persona precargada sin cuenta activa, define su
     * contraseña.
     *
     * @param form datos del formulario (RUT y nueva contraseña)
     * @param errors resultado de las validaciones del formulario
     * @param model modelo de la vista
     * @return la misma vista {@code activar-cuenta}, con un mensaje de
     *         éxito o de error según corresponda
     */
    @PostMapping
    public String activar(@Valid @ModelAttribute("form") ActivarCuentaForm form, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "activar-cuenta";
        }
        try {
            service.activarCuenta(form.getRut(), form.getPassword());
            model.addAttribute("exito", "Cuenta activada. Ya puedes iniciar sesión.");
            model.addAttribute("form", new ActivarCuentaForm());
            return "activar-cuenta";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "activar-cuenta";
        }
    }
}