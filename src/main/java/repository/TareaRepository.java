package repository;

import entidades.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**public interface TareaRepository extends CrudRepository<Tarea, Integer> {
    @Query("SELECT t.id, t.nombre AS nombreTarea, "
            + "SUM(uvt.valoracion * pu.pesoCliente) AS sumatoriaValoracionesPonderadas "
            + "FROM Proyecto p "
            + "JOIN p.tareas t "
            + "JOIN t.usuarioValoraTareas uvt "
            + "JOIN p.proyectoHasUsuarios pu ON pu.usuarioIdusuario.id = uvt.usuarioIdusuario.id "
            + "JOIN Usuario u ON pu.usuarioIdusuario.id = u.id "
            + "WHERE p.id = :idProyecto "
            + "AND u.rol = :rolCliente "
            + "GROUP BY t.id, t.nombre "
            + "HAVING COUNT(CASE WHEN uvt.valorada = 0 THEN 1 END) = 0")
    List<Object[]> obtenerTareasConValoracionPonderada(
            @Param("idProyecto") Long idProyecto,
            @Param("rolCliente") String rolCliente
    );
}**/

public interface TareaRepository extends JpaRepository<Tarea, Integer> {
    List<Tarea> findByNombre(String nombre);
}

