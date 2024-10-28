package repository;

import entidades.Proyecto;
import entidades.ProyectoHasUsuario;
import entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProyectoHasUsuarioRepository extends JpaRepository<ProyectoHasUsuario, Integer> {
    List<ProyectoHasUsuario> findByUsuarioIdusuario(Usuario usuario);
    List<ProyectoHasUsuario> findByProyectoIdproyecto(Proyecto proyecto);
}
