package uniandes.isis2304.vacuandes.interfazApp.dtos;

/**
 * DTO for creating a new condition
 */
public class DTOCondicion {
    public String descripcionCondicion;
    public Long identificadorCondicion; // Optional - if null, will be auto-generated
    
    public DTOCondicion() {}
    
    public DTOCondicion(String descripcionCondicion) {
        this.descripcionCondicion = descripcionCondicion;
    }
    
    public DTOCondicion(Long identificadorCondicion, String descripcionCondicion) {
        this.identificadorCondicion = identificadorCondicion;
        this.descripcionCondicion = descripcionCondicion;
    }
    
    @Override
    public String toString() {
        return "DTOCondicion [identificadorCondicion=" + identificadorCondicion + 
               ", descripcionCondicion=" + descripcionCondicion + "]";
    }
} 