package cl.miclase.springedumanager.domain;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

/**
 * Representa un curso del sistema (por ejemplo, "Java Básico").
 *
 * <p>Un curso tiene un {@link Persona docente} responsable (opcional) y un
 * conjunto de {@link Persona estudiantes} inscritos, mediante la tabla
 * intermedia {@code inscripcion}. A partir de un curso se derivan sus
 * {@link Practica prácticas} y {@link Evaluacion evaluaciones}.</p>
 */
@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String codigo;

    @Column(length = 1000)
    private String descripcion;

    @ManyToOne(optional = true)
    @JoinColumn(name = "docente_id")
    private Persona docente;

    @ManyToMany
    @JoinTable(
        name = "inscripcion",
        joinColumns = @JoinColumn(name = "curso_id"),
        inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    private Set<Persona> estudiantes = new HashSet<>();

    protected Curso() {}

    /**
     * Crea un curso nuevo, sin docente asignado ni estudiantes inscritos.
     *
     * @param nombre nombre del curso
     * @param codigo código identificador (por ejemplo, "JAVA101")
     * @param descripcion descripción del contenido del curso
     */
    public Curso(String nombre, String codigo, String descripcion) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    /**
     * Inscribe a un estudiante en este curso.
     *
     * @param estudiante la persona (con rol ESTUDIANTE) a inscribir
     */
    public void inscribir(Persona estudiante) {
        this.estudiantes.add(estudiante);
    }

    /**
     * Asigna o reemplaza el docente responsable de este curso.
     *
     * @param docente la persona (con rol DOCENTE) a cargo del curso
     */
    public void asignarDocente(Persona docente) {
        this.docente = docente;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Persona getDocente() { return docente; }
    public Set<Persona> getEstudiantes() { return estudiantes; }
}