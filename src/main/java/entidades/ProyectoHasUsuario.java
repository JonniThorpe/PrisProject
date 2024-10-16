package entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "proyecto_has_usuario")
public class ProyectoHasUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Clave primaria simple, generada automáticamente
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Proyecto_idProyecto", nullable = false)
    private Proyecto proyectoIdproyecto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @Column(name = "PesoCliente")
    private Integer pesoCliente;

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
