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

    @OneToMany(mappedBy = "tareaIdtarea")
    private Set<ProyectoHasTarea> proyectoHasTareas = new LinkedHashSet<>();

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

    public Set<ProyectoHasTarea> getProyectoHasTareas() {
        return proyectoHasTareas;
    }

    public void setProyectoHasTareas(Set<ProyectoHasTarea> proyectoHasTareas) {
        this.proyectoHasTareas = proyectoHasTareas;
    }

    public Set<UsuarioValoraTarea> getUsuarioValoraTareas() {
        return usuarioValoraTareas;
    }

    public void setUsuarioValoraTareas(Set<UsuarioValoraTarea> usuarioValoraTareas) {
        this.usuarioValoraTareas = usuarioValoraTareas;
    }

}