package cl.miclase.springedumanager.web;

import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import cl.miclase.springedumanager.domain.Curso;
import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.domain.Practica;
import cl.miclase.springedumanager.domain.Rol;
import cl.miclase.springedumanager.dto.PracticaForm;
import cl.miclase.springedumanager.service.CursoService;
import cl.miclase.springedumanager.service.PersonaService;
import cl.miclase.springedumanager.service.PracticaService;

/**
 * Controlador MVC para la gestión de prácticas.
 *
 * <p>El listado se filtra según el rol de la persona autenticada, igual
 * que en cursos: un administrador ve todas las prácticas, un docente solo
 * las de los cursos que dicta, y un estudiante solo las de los cursos en
 * los que está inscrito. Solo ADMIN y DOCENTE pueden crear prácticas.</p>
 */
@Controller
@RequestMapping("/practicas")
public class PracticaController {

    private final PracticaService service;
    private final CursoService cursoService;
    private final PersonaService personaService;

    public PracticaController(PracticaService service, CursoService cursoService, PersonaService personaService) {
        this.service = service;
        this.cursoService = cursoService;
        this.personaService = personaService;
    }

    /**
     * Lista las prácticas visibles para la persona autenticada, según su
     * rol, junto con los cursos disponibles para el formulario de creación.
     *
     * @param model modelo de la vista
     * @param principal persona autenticada actual
     * @return el nombre de la vista {@code practicas}
     */
    @GetMapping
    public String listar(Model model, Principal principal) {
        Persona actual = personaService.buscarPorRut(principal.getName()).orElse(null);

        List<Practica> practicas;
        List<Curso> cursos;

        if (actual != null && actual.getRol() == Rol.DOCENTE) {
            practicas = service.listarPorDocente(actual.getId());
            cursos = cursoService.listarPorDocente(actual.getId());
        } else if (actual != null && actual.getRol() == Rol.ESTUDIANTE) {
            practicas = service.listarPorEstudiante(actual.getId());
            cursos = cursoService.listarPorEstudiante(actual.getId());
        } else {
            practicas = service.listar();
            cursos = cursoService.listar();
        }

        model.addAttribute("practicas", practicas);
        model.addAttribute("cursos", cursos);
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new PracticaForm());
        }
        return "practicas";
    }

    /**
     * Crea una nueva práctica. Solo accesible para ADMIN o DOCENTE.
     *
     * @param form datos del formulario de creación
     * @param errors resultado de las validaciones
     * @return redirección al listado de prácticas
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public String crear(@Valid @ModelAttribute("form") PracticaForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            return "redirect:/practicas";
        }
        service.crear(form.getTema(), form.getInstrucciones(), form.getFechaEntrega(), form.getCursoId());
        return "redirect:/practicas";
    }
}