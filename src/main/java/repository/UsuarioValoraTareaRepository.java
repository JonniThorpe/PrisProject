package repository;

import entidades.Usuario;
import entidades.UsuarioValoraTarea;
import entidades.UsuarioValoraTareaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioValoraTareaRepository extends JpaRepository<UsuarioValoraTarea, UsuarioValoraTareaId> {

}

