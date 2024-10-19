package entidades;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "idUsuario", nullable = false)
    private Integer id;

    @Column(name = "Nombre", length = 45)
    private String nombre;

    @Column(name = "Contraseña", length = 45)
    private String password;

    @Column(name = "Rol", length = 45)
    private String rol;

    @OneToMany(mappedBy = "usuarioIdusuario")
    private Set<Proyecto> proyectos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usuarioIdusuario")
    private Set<ProyectoHasUsuario> proyectoHasUsuarios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usuarioIdusuario")
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Set<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(Set<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public Set<ProyectoHasUsuario> getProyectoHasUsuarios() {
        return proyectoHasUsuarios;
    }

    public void setProyectoHasUsuarios(Set<ProyectoHasUsuario> proyectoHasUsuarios) {
        this.proyectoHasUsuarios = proyectoHasUsuarios;
    }

    public Set<UsuarioValoraTarea> getUsuarioValoraTareas() {
        return usuarioValoraTareas;
    }

    public void setUsuarioValoraTareas(Set<UsuarioValoraTarea> usuarioValoraTareas) {
        this.usuarioValoraTareas = usuarioValoraTareas;
    }

}