package entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "proyecto_has_usuario")
public class ProyectoHasUsuario {
    @EmbeddedId
    private ProyectoHasUsuarioId id;

    @MapsId("proyectoIdproyecto")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proyecto_id_proyecto", nullable = false)
    private Proyecto proyectoIdproyecto;

    @MapsId("usuarioIdusuario")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id_usuario", nullable = false)
    private Usuario usuarioIdusuario;

    @Column(name = "peso_cliente")
    private Integer pesoCliente;

    public ProyectoHasUsuarioId getId() {
        return id;
    }

    public void setId(ProyectoHasUsuarioId id) {
        this.id = id;
    }

    public Proyecto getProyectoIdproyecto() {
        return proyectoIdproyecto;
    }

    public void setProyectoIdproyecto(Proyecto proyectoIdproyecto) {
        this.proyectoIdproyecto = proyectoIdproyecto;
    }

    public Usuario getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(Usuario usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

    public Integer getPesoCliente() {
        return pesoCliente;
    }

    public void setPesoCliente(Integer pesoCliente) {
        this.pesoCliente = pesoCliente;
    }

}