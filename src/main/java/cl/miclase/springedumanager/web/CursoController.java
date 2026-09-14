package cl.miclase.springedumanager.web;

import java.security.Principal;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.domain.Rol;
import cl.miclase.springedumanager.dto.CursoForm;
import cl.miclase.springedumanager.service.CursoService;
import cl.miclase.springedumanager.service.PersonaService;

/**
 * Controlador MVC para la gestión de cursos.
 *
 * <p>El listado se filtra según el rol de la persona autenticada: un
 * administrador ve todos los cursos, un docente solo los que dicta, y un
 * estudiante solo aquellos en los que está inscrito. La creación de
 * cursos y la inscripción de estudiantes están reservadas al
 * administrador.</p>
 */
@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService service;
    private final PersonaService personaService;

    public CursoController(CursoService service, PersonaService personaService) {
        this.service = service;
        this.personaService = personaService;
    }

    /**
     * Lista los cursos visibles para la persona autenticada, según su rol.
     *
     * @param model modelo de la vista
     * @param principal persona autenticada actual
     * @return el nombre de la vista {@code cursos}
     */
    @GetMapping
    public String listar(Model model, Principal principal) {
        Persona actual = personaService.buscarPorRut(principal.getName()).orElse(null);

        if (actual != null && actual.getRol() == Rol.DOCENTE) {
            model.addAttribute("cursos", service.listarPorDocente(actual.getId()));
        } else if (actual != null && actual.getRol() == Rol.ESTUDIANTE) {
            model.addAttribute("cursos", service.listarPorEstudiante(actual.getId()));
        } else {
            model.addAttribute("cursos", service.listar());
        }

        model.addAttribute("estudiantes", personaService.listarPorRol(Rol.ESTUDIANTE));
        model.addAttribute("docentes", personaService.listarPorRol(Rol.DOCENTE));
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new CursoForm());
        }
        return "cursos";
    }

    /**
     * Crea un nuevo curso. Solo accesible para el rol ADMIN.
     *
     * @param form datos del formulario de creación
     * @param errors resultado de las validaciones
     * @param model modelo de la vista
     * @return redirección al listado de cursos si fue exitoso, o la misma
     *         vista con los errores si el formulario es inválido
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String crear(@Valid @ModelAttribute("form") CursoForm form, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("cursos", service.listar());
            model.addAttribute("estudiantes", personaService.listarPorRol(Rol.ESTUDIANTE));
            model.addAttribute("docentes", personaService.listarPorRol(Rol.DOCENTE));
            return "cursos";
        }
        service.crear(form.getNombre(), form.getCodigo(), form.getDescripcion(), form.getDocenteId());
        return "redirect:/cursos";
    }

    /**
     * Inscribe a un estudiante en un curso. Solo accesible para el rol ADMIN.
     *
     * @param cursoId id del curso
     * @param estudianteId id del estudiante a inscribir
     * @return redirección al listado de cursos
     */
    @PostMapping("/{cursoId}/inscribir")
    @PreAuthorize("hasRole('ADMIN')")
    public String inscribir(@PathVariable Long cursoId, @RequestParam Long estudianteId) {
        service.inscribir(cursoId, estudianteId);
        return "redirect:/cursos";
    }
}