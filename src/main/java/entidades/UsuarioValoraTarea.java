package entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_valora_tarea")
public class UsuarioValoraTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Clave primaria simple, generada automáticamente
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Tarea_idTarea", nullable = false)
    private Tarea tareaIdtarea;

    @Column(name = "Valoracion")
    private Integer valoracion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Proyecto_idProyecto", nullable = false)
    private Proyecto proyectoIdproyecto;

    @Column(name = "Valorada")
    private Byte valorada;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(Usuario usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

    public Tarea getTareaIdtarea() {
        return tareaIdtarea;
    }

    public void setTareaIdtarea(Tarea tareaIdtarea) {
        this.tareaIdtarea = tareaIdtarea;
    }

    public Integer getValoracion() {
        return valoracion;
    }

    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }

    public Proyecto getProyectoIdproyecto() {
        return proyectoIdproyecto;
    }

    public void setProyectoIdproyecto(Proyecto proyectoIdproyecto) {
        this.proyectoIdproyecto = proyectoIdproyecto;
    }

    public Byte getValorada() {
        return valorada;
    }

    public void setValorada(Byte valorada) {
        this.valorada = valorada;
    }
}