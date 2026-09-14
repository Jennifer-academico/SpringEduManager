package cl.miclase.springedumanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.miclase.springedumanager.domain.Curso;
import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.repository.CursoRepository;

/**
 * Servicio encargado de la lógica de negocio relacionada con {@link Curso}.
 *
 * <p>Permite crear cursos (asignándoles opcionalmente un docente),
 * inscribir estudiantes, y listar cursos filtrados según el rol de quien
 * consulta: un administrador ve todos, un docente solo los que dicta, y
 * un estudiante solo aquellos en los que está inscrito.</p>
 */
@Service
public class CursoService {

    private final CursoRepository repository;
    private final PersonaService personaService;

    public CursoService(CursoRepository repository, PersonaService personaService) {
        this.repository = repository;
        this.personaService = personaService;
    }

    /**
     * Lista todos los cursos del sistema, sin filtrar.
     *
     * @return todos los cursos
     */
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return repository.findAll();
    }

    /**
     * Lista los cursos a cargo de un docente específico.
     *
     * @param docenteId id del docente
     * @return cursos que dicta ese docente
     */
    @Transactional(readOnly = true)
    public List<Curso> listarPorDocente(Long docenteId) {
        return repository.findByDocenteId(docenteId);
    }

    /**
     * Lista los cursos en los que está inscrito un estudiante específico.
     *
     * @param estudianteId id del estudiante
     * @return cursos en los que participa ese estudiante
     */
    @Transactional(readOnly = true)
    public List<Curso> listarPorEstudiante(Long estudianteId) {
        return repository.findByEstudiantesId(estudianteId);
    }

    /**
     * Obtiene un curso por su id.
     *
     * @param id id del curso
     * @return el curso encontrado
     * @throws IllegalArgumentException si no existe un curso con ese id
     */
    @Transactional(readOnly = true)
    public Curso obtener(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));
    }

    /**
     * Crea un nuevo curso, asignándole opcionalmente un docente a cargo.
     *
     * @param nombre nombre del curso
     * @param codigo código identificador
     * @param descripcion descripción del contenido
     * @param docenteId id del docente a asignar, o {@code null} si no se
     *        asigna ninguno todavía
     * @return el curso recién creado
     */
    @Transactional
    public Curso crear(String nombre, String codigo, String descripcion, Long docenteId) {
        Curso curso = new Curso(nombre, codigo, descripcion);
        if (docenteId != null) {
            Persona docente = personaService.obtener(docenteId);
            curso.asignarDocente(docente);
        }
        return repository.save(curso);
    }

    /**
     * Inscribe a un estudiante en un curso.
     *
     * @param cursoId id del curso
     * @param estudianteId id del estudiante a inscribir
     */
    @Transactional
    public void inscribir(Long cursoId, Long estudianteId) {
        Curso curso = obtener(cursoId);
        Persona estudiante = personaService.obtener(estudianteId);
        curso.inscribir(estudiante);
    }

    /**
     * Elimina un curso del sistema.
     *
     * @param id id del curso a eliminar
     * @throws IllegalArgumentException si no existe un curso con ese id
     */
    @Transactional
    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }
}