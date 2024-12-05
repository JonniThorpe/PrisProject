package repository;

import entidades.Dependencia;
import entidades.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DependenciaRepository extends JpaRepository<Dependencia, Integer> {

    // Obtener todas las dependencias de una tarea específica
    @Query("SELECT d FROM Dependencia d WHERE d.idTarea = :tarea")
    List<Dependencia> findByTarea(Tarea tarea);

    // Obtener todas las dependencias que apuntan a una tarea específica
    @Query("SELECT d FROM Dependencia d WHERE d.idTareaDependencia = :tarea")
    List<Dependencia> findDependenciasQueDependenDe(Tarea tarea);
}
