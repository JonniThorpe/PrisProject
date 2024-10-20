package entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsuarioValoraTareaId implements Serializable {
    private static final long serialVersionUID = -7415806676786053496L;
    @Column(name = "usuario_id_usuario", nullable = false)
    private Integer usuarioIdusuario;

    @Column(name = "tarea_id_tarea", nullable = false)
    private Integer tareaIdtarea;

    public Integer getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(Integer usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

    public Integer getTareaIdtarea() {
        return tareaIdtarea;
    }

    public void setTareaIdtarea(Integer tareaIdtarea) {
        this.tareaIdtarea = tareaIdtarea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UsuarioValoraTareaId entity = (UsuarioValoraTareaId) o;
        return Objects.equals(this.usuarioIdusuario, entity.usuarioIdusuario) &&
                Objects.equals(this.tareaIdtarea, entity.tareaIdtarea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioIdusuario, tareaIdtarea);
    }

}