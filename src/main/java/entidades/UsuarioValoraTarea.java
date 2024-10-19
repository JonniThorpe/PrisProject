package entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_valora_tarea")
public class UsuarioValoraTarea {
    @EmbeddedId
    private UsuarioValoraTareaId id;

    @MapsId("usuarioIdusuario")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @MapsId("tareaIdtarea")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Tarea_idTarea", nullable = false)
    private Tarea tareaIdtarea;

    @Column(name = "Valoracion")
    private Integer valoracion;

    @Column(name = "Valorada")
    private Byte valorada;

    public UsuarioValoraTareaId getId() {
        return id;
    }

    public void setId(UsuarioValoraTareaId id) {
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

    public Byte getValorada() {
        return valorada;
    }

    public void setValorada(Byte valorada) {
        this.valorada = valorada;
    }

}