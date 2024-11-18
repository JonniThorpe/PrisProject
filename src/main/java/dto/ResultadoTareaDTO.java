package dto;

public class ResultadoTareaDTO {
    private Long idTarea;
    private String nombreTarea;
    private Double valoracionPonderada;

    // Constructor
    public ResultadoTareaDTO(Long idTarea, String nombreTarea, Double valoracionPonderada) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
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

    public Double getValoracionPonderada() {
        return valoracionPonderada;
    }

    public void setValoracionPonderada(Double valoracionPonderada) {
        this.valoracionPonderada = valoracionPonderada;
    }
}
