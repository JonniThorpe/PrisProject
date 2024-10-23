package repository;

import entidades.Proyecto;
import entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    // Encuentra los proyectos por el usuario relacionado
    List<Proyecto> findByUsuarioIdusuario(Usuario usuario);


}
