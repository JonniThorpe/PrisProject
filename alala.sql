SELECT
    T.idTarea,
    T.Nombre AS Nombre_Tarea,
    SUM(UVT.Valoracion * PU.PesoCliente) AS Sumatoria_Valoraciones_Ponderadas
FROM
    Proyecto P
JOIN
    Proyecto_has_Tarea PT ON P.idProyecto = PT.Proyecto_idProyecto
JOIN
    Tarea T ON PT.Tarea_idTarea = T.idTarea
JOIN
    Usuario_Valora_Tarea UVT ON T.idTarea = UVT.Tarea_idTarea
JOIN
    Proyecto_has_Usuario PU ON P.idProyecto = PU.Proyecto_idProyecto
    AND UVT.Usuario_idUsuario = PU.Usuario_idUsuario
JOIN
    Usuario U ON PU.Usuario_idUsuario = U.idUsuario
WHERE
    P.idProyecto = 1
    AND U.Rol = 'Cliente'
GROUP BY
    T.idTarea, T.Nombre
HAVING
    COUNT(CASE WHEN UVT.Valorada = 0 THEN 1 END) = 0;





SELECT
    T.idTarea,
    T.Nombre AS Nombre_Tarea,
    SUM(UVT.Valoracion * PU.PesoCliente) AS Sumatoria_Valoraciones_Ponderadas
FROM
    Proyecto P
JOIN
    Tarea T ON P.idProyecto = T.Proyecto_idProyecto
JOIN
    Usuario_Valora_Tarea UVT ON T.idTarea = UVT.Tarea_idTarea
JOIN
    Proyecto_has_Usuario PU ON P.idProyecto = PU.Proyecto_idProyecto
    AND UVT.Usuario_idUsuario = PU.Usuario_idUsuario
JOIN
    Usuario U ON PU.Usuario_idUsuario = U.idUsuario
WHERE
    P.idProyecto = 1
    AND U.Rol = 'Cliente'
GROUP BY
    T.idTarea, T.Nombre
HAVING
    COUNT(CASE WHEN UVT.Valorada = 0 THEN 1 END) = 0;
