package entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProyectoHasUsuarioId implements Serializable {
    private static final long serialVersionUID = 9101389567335261405L;
    @Column(name = "Proyecto_idProyecto", nullable = false)
    private Integer proyectoIdproyecto;

    @Column(name = "Usuario_idUsuario", nullable = false)
    private Integer usuarioIdusuario;

    public Integer getProyectoIdproyecto() {
        return proyectoIdproyecto;
    }

    public void setProyectoIdproyecto(Integer proyectoIdproyecto) {
        this.proyectoIdproyecto = proyectoIdproyecto;
    }

    public Integer getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(Integer usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProyectoHasUsuarioId entity = (ProyectoHasUsuarioId) o;
        return Objects.equals(this.usuarioIdusuario, entity.usuarioIdusuario) &&
                Objects.equals(this.proyectoIdproyecto, entity.proyectoIdproyecto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioIdusuario, proyectoIdproyecto);
    }

}