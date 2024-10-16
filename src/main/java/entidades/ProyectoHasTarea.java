package entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "proyecto_has_tarea")
public class ProyectoHasTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Clave primaria simple, generada automáticamente
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Proyecto_idProyecto", nullable = false)
    private Proyecto proyectoIdproyecto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Tarea_idTarea", nullable = false)
    private Tarea tareaIdtarea;

    @Column(name = "Satisfaccion")
    private Double satisfaccion;

    @Column(name = "Esfuerzo")
    private Integer esfuerzo;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proyecto getProyectoIdproyecto() {
        return proyectoIdproyecto;
    }

    public void setProyectoIdproyecto(Proyecto proyectoIdproyecto) {
        this.proyectoIdproyecto = proyectoIdproyecto;
    }

    public Tarea getTareaIdtarea() {
        return tareaIdtarea;
    }

    public void setTareaIdtarea(Tarea tareaIdtarea) {
        this.tareaIdtarea = tareaIdtarea;
    }

    public Double getSatisfaccion() {
        return satisfaccion;
    }

    public void setSatisfaccion(Double satisfaccion) {
        this.satisfaccion = satisfaccion;
    }

    public Integer getEsfuerzo() {
        return esfuerzo;
    }

    public void setEsfuerzo(Integer esfuerzo) {
        this.esfuerzo = esfuerzo;
    }
}
