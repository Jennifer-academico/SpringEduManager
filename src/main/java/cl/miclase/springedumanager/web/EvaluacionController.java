package cl.miclase.springedumanager.web;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import cl.miclase.springedumanager.domain.*;
import cl.miclase.springedumanager.dto.EvaluacionForm;
import cl.miclase.springedumanager.service.*;

/**
 * Controlador MVC para la gestión de evaluaciones y sus notas.
 *
 * <p>Presenta, para cada curso visible según el rol del usuario, una
 * planilla con los estudiantes como filas y las evaluaciones como
 * columnas. El guardado de notas es único por curso: se procesan todas
 * las celdas de la planilla en un solo envío, asignando 1.0 a las celdas
 * dejadas en blanco. Solo ADMIN y DOCENTE pueden crear evaluaciones y
 * registrar notas.</p>
 */
@Controller
@RequestMapping("/evaluaciones")
public class EvaluacionController {

    private final EvaluacionService service;
    private final NotaService notaService;
    private final CursoService cursoService;
    private final PersonaService personaService;

    public EvaluacionController(
            EvaluacionService service,
            NotaService notaService,
            CursoService cursoService,
            PersonaService personaService) {
        this.service = service;
        this.notaService = notaService;
        this.cursoService = cursoService;
        this.personaService = personaService;
    }

    /**
     * Construye la planilla de evaluaciones y notas para los cursos
     * visibles según el rol de la persona autenticada.
     *
     * @param model modelo de la vista
     * @param principal persona autenticada actual
     * @return el nombre de la vista {@code evaluaciones}
     */
    @GetMapping
    public String listar(Model model, Principal principal) {
        Persona actual = personaService.buscarPorRut(principal.getName()).orElse(null);

        List<Curso> cursos;
        if (actual != null && actual.getRol() == Rol.DOCENTE) {
            cursos = cursoService.listarPorDocente(actual.getId());
        } else if (actual != null && actual.getRol() == Rol.ESTUDIANTE) {
            cursos = cursoService.listarPorEstudiante(actual.getId());
        } else {
            cursos = cursoService.listar();
        }

        Map<Long, List<Evaluacion>> evaluacionesPorCurso = new LinkedHashMap<>();
        Map<String, Double> notas = new HashMap<>();
        Map<String, Double> promedios = new HashMap<>();

        for (Curso curso : cursos) {
            List<Evaluacion> evals = service.listarPorCurso(curso.getId());
            evaluacionesPorCurso.put(curso.getId(), evals);

            for (Persona est : curso.getEstudiantes()) {
                double suma = 0;
                int cuenta = 0;
                for (Evaluacion ev : evals) {
                    Nota nota = notaService.buscar(ev.getId(), est.getId()).orElse(null);
                    if (nota != null) {
                        notas.put(ev.getId() + "_" + est.getId(), nota.getValor());
                        suma += nota.getValor();
                        cuenta++;
                    }
                }
                if (cuenta > 0) {
                    promedios.put(curso.getId() + "_" + est.getId(), suma / cuenta);
                }
            }
        }

        model.addAttribute("cursos", cursos);
        model.addAttribute("evaluacionesPorCurso", evaluacionesPorCurso);
        model.addAttribute("notas", notas);
        model.addAttribute("promedios", promedios);
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new EvaluacionForm());
        }
        return "evaluaciones";
    }

    /**
     * Crea una nueva evaluación para un curso. Solo ADMIN o DOCENTE.
     *
     * @param form datos del formulario (nombre y curso)
     * @param errors resultado de las validaciones
     * @return redirección al listado de evaluaciones
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public String crear(@Valid @ModelAttribute("form") EvaluacionForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            return "redirect:/evaluaciones";
        }
        service.crear(form.getNombre(), form.getCursoId());
        return "redirect:/evaluaciones";
    }

    /**
     * Guarda de una sola vez todas las notas de la planilla de un curso.
     * Los parámetros de la petición tienen la forma
     * {@code nota_<idEvaluacion>_<idEstudiante>}; las celdas vacías se
     * registran con nota mínima (1.0).
     *
     * @param cursoId id del curso cuya planilla se está guardando
     * @param params parámetros crudos de la petición, con las notas
     *        ingresadas en cada celda de la planilla
     * @return redirección al listado de evaluaciones
     */
    @PostMapping("/curso/{cursoId}/notas")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public String guardarNotas(@PathVariable Long cursoId, @RequestParam Map<String, String> params) {
        Curso curso = cursoService.obtener(cursoId);
        List<Evaluacion> evals = service.listarPorCurso(cursoId);

        for (Evaluacion ev : evals) {
            for (Persona est : curso.getEstudiantes()) {
                String key = "nota_" + ev.getId() + "_" + est.getId();
                String raw = params.get(key);
                double valor;
                if (raw == null || raw.isBlank()) {
                    valor = 1.0;
                } else {
                    valor = Double.parseDouble(raw.replace(",", "."));
                }
                notaService.guardarOActualizar(ev.getId(), est.getId(), valor);
            }
        }
        return "redirect:/evaluaciones";
    }
}