package cl.miclase.springedumanager.domain;

import java.time.LocalDate;
import jakarta.persistence.*;

/**
 * Representa una práctica o guía de ejercicios asignada a un {@link Curso}.
 *
 * <p>Cada práctica tiene un tema, instrucciones para el estudiante y una
 * fecha límite de entrega. Todas las prácticas pertenecen a un único curso;
 * los estudiantes inscritos en ese curso son quienes deben resolverla.</p>
 */
@Entity
@Table(name = "practica")
public class Practica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String tema;

    @Column(length = 2000)
    private String instrucciones;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @ManyToOne(optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    protected Practica() {}

    /**
     * Crea una nueva práctica asociada a un curso.
     *
     * @param tema tema o título breve de la práctica
     * @param instrucciones detalle de lo que debe resolver el estudiante
     * @param fechaEntrega fecha límite para entregar la práctica
     * @param curso curso al que pertenece esta práctica
     */
    public Practica(String tema, String instrucciones, LocalDate fechaEntrega, Curso curso) {
        this.tema = tema;
        this.instrucciones = instrucciones;
        this.fechaEntrega = fechaEntrega;
        this.curso = curso;
    }

    public Long getId() { return id; }
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
}