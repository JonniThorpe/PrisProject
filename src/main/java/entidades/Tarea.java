package entidades;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tarea")
public class Tarea {
    @Id
    @Column(name = "idTarea", nullable = false)
    private Integer id;

    @Column(name = "Nombre", length = 45)
    private String nombre;

    @Column(name = "Descripcion", length = 45)
    private String descripcion;

    @Column(name = "Esfuerzo", length = 45)
    private String esfuerzo;

    @ManyToMany(mappedBy = "tareas")
    private Set<Proyecto> proyectos = new LinkedHashSet<>();

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

    public String getEsfuerzo() {
        return esfuerzo;
    }

    public void setEsfuerzo(String esfuerzo) {
        this.esfuerzo = esfuerzo;
    }

    public Set<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(Set<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public Set<UsuarioValoraTarea> getUsuarioValoraTareas() {
        return usuarioValoraTareas;
    }

    public void setUsuarioValoraTareas(Set<UsuarioValoraTarea> usuarioValoraTareas) {
        this.usuarioValoraTareas = usuarioValoraTareas;
    }

}