package entidades;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "proyecto")
public class Proyecto {
    @Id
    @Column(name = "idProyecto", nullable = false)
    private Integer id;

    @Column(name = "Nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "Fecha_Creacion")
    private Instant fechaCreacion;

    @Column(name = "PesoMaximoTareas")
    private Integer pesoMaximoTareas;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @OneToMany(mappedBy = "proyectoIdproyecto")
    private Set<ProyectoHasUsuario> proyectoHasUsuarios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "proyectoIdproyecto")
    private Set<Tarea> tareas = new LinkedHashSet<>();

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

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getPesoMaximoTareas() {
        return pesoMaximoTareas;
    }

    public void setPesoMaximoTareas(Integer pesoMaximoTareas) {
        this.pesoMaximoTareas = pesoMaximoTareas;
    }

    public Usuario getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(Usuario usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

    public Set<ProyectoHasUsuario> getProyectoHasUsuarios() {
        return proyectoHasUsuarios;
    }

    public void setProyectoHasUsuarios(Set<ProyectoHasUsuario> proyectoHasUsuarios) {
        this.proyectoHasUsuarios = proyectoHasUsuarios;
    }

    public Set<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        this.tareas = tareas;
    }

}