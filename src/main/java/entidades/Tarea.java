package entidades;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tarea")
public class Tarea {
    @Id
    @Column(name = "id_tarea", nullable = false)
    private Integer id;

    @Column(name = "Nombre", length = 45)
    private String nombre;

    @Column(name = "Descripcion", length = 45)
    private String descripcion;

    @Column(name = "Satisfaccion")
    private Double satisfaccion;

    @Column(name = "Esfuerzo", nullable = false)
    private Integer esfuerzo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proyecto_id_proyecto", nullable = false)
    private Proyecto proyectoIdproyecto;

    @OneToMany(mappedBy = "tareaIdtarea")
    private Set<UsuarioValoraTarea> usuarioValoraTareas = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Proyecto getProyectoIdproyecto() {
        return proyectoIdproyecto;
    }

    public void setProyectoIdproyecto(Proyecto proyectoIdproyecto) {
        this.proyectoIdproyecto = proyectoIdproyecto;
    }

    public Set<UsuarioValoraTarea> getUsuarioValoraTareas() {
        return usuarioValoraTareas;
    }

    public void setUsuarioValoraTareas(Set<UsuarioValoraTarea> usuarioValoraTareas) {
        this.usuarioValoraTareas = usuarioValoraTareas;
    }

}