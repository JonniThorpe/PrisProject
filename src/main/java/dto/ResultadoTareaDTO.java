package dto;

public class ResultadoTareaDTO {
    private Long idTarea;
    private String nombreTarea;
    private Integer esfuerzo; // Añadir esta propiedad
    private Double valoracionPonderada;

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
        return esfuerzo; // Getter para esfuerzo
    }

    public void setEsfuerzo(Integer esfuerzo) {
        this.esfuerzo = esfuerzo; // Setter para esfuerzo
    }

    public Double getValoracionPonderada() {
        return valoracionPonderada;
    }

    public void setValoracionPonderada(Double valoracionPonderada) {
        this.valoracionPonderada = valoracionPonderada;
    }
}
