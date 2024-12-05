package dto;

import java.util.List;

public class ResultadoTareaDTO {
    private Long idTarea;
    private String nombreTarea;
    private Integer esfuerzo; // Añadir esta propiedad
    private Double valoracionPonderada;
    private List<Long> dependencias; // IDs de las tareas de las que depende esta tarea

    // Constructor
    public ResultadoTareaDTO(Long idTarea, String nombreTarea, Integer esfuerzo, Double valoracionPonderada) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
        this.esfuerzo = esfuerzo;
        this.valoracionPonderada = valoracionPonderada;
    }

    // Getters y Setters
    public Long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Long idTarea) {
        this.idTarea = idTarea;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public Integer getEsfuerzo() {
        return esfuerzo;
    }

    public void setEsfuerzo(Integer esfuerzo) {
        this.esfuerzo = esfuerzo;
    }

    public Double getValoracionPonderada() {
        return valoracionPonderada;
    }

    public void setValoracionPonderada(Double valoracionPonderada) {
        this.valoracionPonderada = valoracionPonderada;
    }

    // Calcular productividad
    public Double getProductividad() {
        return esfuerzo != null && esfuerzo > 0 ? valoracionPonderada / esfuerzo : 0.0;
    }
}
