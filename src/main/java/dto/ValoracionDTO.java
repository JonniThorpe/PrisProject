package dto;

public class ValoracionDTO {
    private String nombreProyecto;
    private Integer idProyecto;
    private String nombreCliente;
    private String nombreTarea;
    private String valoracion;

    // Constructor
    public ValoracionDTO(String nombreProyecto, String nombreCliente, String nombreTarea, String valoracion, Integer idProyecto) {
        this.nombreProyecto = nombreProyecto;
        this.nombreCliente = nombreCliente;
        this.nombreTarea = nombreTarea;
        this.valoracion = valoracion;
        this.idProyecto = idProyecto;
    }

    // Getters
    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public String getValoracion() {
        return valoracion;
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    // Setters
    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }
}