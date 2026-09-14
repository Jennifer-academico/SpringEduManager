package cl.miclase.springedumanager.domain;

import jakarta.persistence.*;

/**
 * Representa la calificación de un {@link Persona estudiante} en una
 * {@link Evaluacion} específica.
 *
 * <p>Cada combinación de evaluación y estudiante tiene, como máximo, una
 * Nota asociada: si el docente vuelve a ingresar una nota para el mismo
 * estudiante en la misma evaluación, el valor existente se actualiza en
 * lugar de crear un registro nuevo.</p>
 */
@Entity
@Table(name = "nota")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Persona estudiante;

    protected Nota() {}

    /**
     * Crea una nueva nota para un estudiante en una evaluación.
     *
     * @param valor calificación obtenida (escala 1.0 a 7.0)
     * @param evaluacion evaluación a la que corresponde esta nota
     * @param estudiante estudiante calificado
     */
    public Nota(Double valor, Evaluacion evaluacion, Persona estudiante) {
        this.valor = valor;
        this.evaluacion = evaluacion;
        this.estudiante = estudiante;
    }

    public Long getId() { return id; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public Evaluacion getEvaluacion() { return evaluacion; }
    public Persona getEstudiante() { return estudiante; }
}