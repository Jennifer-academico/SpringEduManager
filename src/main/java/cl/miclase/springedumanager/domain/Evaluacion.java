package cl.miclase.springedumanager.domain;

import jakarta.persistence.*;

/**
 * Representa una evaluación definida para un {@link Curso} (por ejemplo,
 * "Prueba 1" o "Examen Final").
 *
 * <p>Una Evaluación es la <b>definición</b> de un instrumento de evaluación
 * compartido por todos los estudiantes del curso; no contiene notas. Las
 * calificaciones individuales de cada estudiante en esta evaluación se
 * registran por separado en {@link Nota}.</p>
 */
@Entity
@Table(name = "evaluacion")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    protected Evaluacion() {}

    /**
     * Crea una nueva evaluación para un curso.
     *
     * @param nombre nombre de la evaluación (por ejemplo, "Control 1")
     * @param curso curso al que pertenece esta evaluación
     */
    public Evaluacion(String nombre, Curso curso) {
        this.nombre = nombre;
        this.curso = curso;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
}