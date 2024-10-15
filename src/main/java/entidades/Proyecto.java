package entidades;

import jakarta.persistence.*;

import java.math.BigDecimal;
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

    @Column(name = "Resultado", precision = 2)
    private BigDecimal resultado;

    @Column(name = "Fecha_Creacion")
    private Instant fechaCreacion;

    @Column(name = "PesoMaximoTareas")
    private Integer pesoMaximoTareas;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @ManyToMany
    @JoinTable(name = "proyecto_has_tarea",
            inverseJoinColumns = @JoinColumn(name = "Tarea_idTarea"))
    private Set<Tarea> tareas = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "proyecto_has_usuario",
            inverseJoinColumns = @JoinColumn(name = "Usuario_idUsuario"))
    private Set<Usuario> usuarios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "proyectoIdproyecto")
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

    public BigDecimal getResultado() {
        return resultado;
    }

    public void setResultado(BigDecimal resultado) {
        this.resultado = resultado;
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

    public Set<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        this.tareas = tareas;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Set<UsuarioValoraTarea> getUsuarioValoraTareas() {
        return usuarioValoraTareas;
    }

    public void setUsuarioValoraTareas(Set<UsuarioValoraTarea> usuarioValoraTareas) {
        this.usuarioValoraTareas = usuarioValoraTareas;
    }

}