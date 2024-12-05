package repository;

import entidades.Proyecto;
import entidades.Tarea;
import entidades.UsuarioValoraTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    List<Tarea> findByProyectoIdproyecto(Proyecto proyecto);

    // Buscar todas las tareas por el id del usuario propietario de los proyectos
    @Query("SELECT t FROM Tarea t WHERE t.proyectoIdproyecto.usuarioIdusuario.id = :usuarioId")
    List<Tarea> findAllByProyectoUsuarioId(@Param("usuarioId") Integer usuarioId);

    @Query(
            "SELECT t.id AS idTarea, " +
                    "       t.nombre AS nombreTarea, " +
                    "       t.esfuerzo AS esfuerzoTarea, " + // Agregar el esfuerzo
                    "       SUM(uvt.valoracion * pu.pesoCliente) AS sumatoriaValoracionesPonderadas " +
                    "FROM Proyecto p " +
                    "JOIN p.tareas t " +
                    "JOIN t.usuarioValoraTareas uvt " +
                    "JOIN p.proyectoHasUsuarios pu ON pu.usuarioIdusuario.id = uvt.usuarioIdusuario.id " +
                    "JOIN Usuario u ON pu.usuarioIdusuario.id = u.id " +
                    "WHERE p.id = :idProyecto " +
                    "  AND u.rol = :rolCliente " +
                    "GROUP BY t.id, t.nombre, t.esfuerzo " + // Incluir esfuerzo en el GROUP BY
                    "HAVING COUNT(CASE WHEN uvt.valorada = 0 THEN 1 END) = 0"
    )
    List<Object[]> obtenerTareasConValoracionPonderada(
            @Param("idProyecto") Long idProyecto,
            @Param("rolCliente") String rolCliente
    );
    @Query(
            "SELECT uvt " +
                    "FROM UsuarioValoraTarea uvt " +
                    "WHERE uvt.usuarioIdusuario.id = :idCliente " +
                    "  AND uvt.tareaIdtarea.id = :idTarea"
    )
    Optional<UsuarioValoraTarea> findValoracionByClienteAndTarea(
            @Param("idCliente") Long idCliente,
            @Param("idTarea") Long idTarea
    );

}

