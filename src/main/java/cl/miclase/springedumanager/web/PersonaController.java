package cl.miclase.springedumanager.web;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import cl.miclase.springedumanager.dto.PersonaForm;
import cl.miclase.springedumanager.service.PersonaService;

/**
 * Controlador MVC para la gestión de personas (docentes y estudiantes)
 * por parte del administrador.
 *
 * <p>Todo este controlador está reservado al rol ADMIN: es donde se
 * precargan las personas que luego, por su cuenta, activarán su acceso
 * en {@link ActivarCuentaController}.</p>
 */
@Controller
@RequestMapping("/personas")
@PreAuthorize("hasRole('ADMIN')")
public class PersonaController {

    private final PersonaService service;

    public PersonaController(PersonaService service) {
        this.service = service;
    }

    /**
     * Lista todas las personas registradas en el sistema.
     *
     * @param model modelo de la vista
     * @return el nombre de la vista {@code personas}
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("personas", service.listar());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new PersonaForm());
        }
        return "personas";
    }

    /**
     * Precarga una nueva persona (docente o estudiante), sin cuenta
     * activa todavía.
     *
     * @param form datos del formulario de precarga
     * @param errors resultado de las validaciones
     * @param model modelo de la vista
     * @return redirección al listado de personas si fue exitoso, o la
     *         misma vista con los errores si el formulario es inválido
     */
    @PostMapping
    public String precargar(@Valid @ModelAttribute("form") PersonaForm form, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("personas", service.listar());
            return "personas";
        }
        service.precargar(form.getNombre(), form.getEmail(), form.getRut(), form.getRol());
        return "redirect:/personas";
    }
}