package uniandes.isis2304.vacuandes.interfazApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import uniandes.isis2304.vacuandes.negocio.Ciudadano;
import uniandes.isis2304.vacuandes.negocio.Vacuandes;
import uniandes.isis2304.vacuandes.negocio.VOCiudadano;
import uniandes.isis2304.vacuandes.interfazApp.dtos.CiudadanoResponse;
import uniandes.isis2304.vacuandes.interfazApp.dtos.DTOCiudadano;
import uniandes.isis2304.vacuandes.negocio.VOCita;
import uniandes.isis2304.vacuandes.negocio.VOPuntoVacunacion;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Vacuandes Web Services
 * 
 * This controller provides REST endpoints for the Vacuandes application
 * to be consumed by a future frontend application.
 * 
 * @author Vacuandes Team
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class VacuandesRestController {
    
    @Autowired
    private Vacuandes vacuandes;
    
    /**
     * Root health check endpoint for Elastic Beanstalk
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> rootHealth() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Vacuandes API",
            "version", "1.0.0",
            "message", "Vacuandes vaccination management system is running"
        ));
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Vacuandes API",
            "version", "1.0.0"
        ));
    }
     
    /**
     * Error handler for general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        e.printStackTrace(); // Print stack trace for debugging
        return ResponseEntity.internalServerError()
            .body(Map.of(
                "error", "Internal Server Error",
                "message", e.getMessage() != null ? e.getMessage() : "Unknown error occurred",
                "exception", e.getClass().getSimpleName(),
                "timestamp", System.currentTimeMillis()
            ));
    }

    /**
     * Endpoint to create a new ciudadano
     * @param dto
     * @return ResponseEntity with the created ciudadano
     */
    @PostMapping("/ciudadanos")
    public ResponseEntity<?> createCiudadano(@RequestBody DTOCiudadano dto) {
        try {
            System.out.println("=== DEBUG: Received request to create ciudadano ===");
            System.out.println("DTO: " + dto.toString());
            System.out.println("TipoDeIdentificacion: " + dto.tipoDeIdentificacion);
            System.out.println("IdentificacionCiudadano: " + dto.identificacionCiudadano);
            System.out.println("NombreCiudadano: " + dto.nombreCiudadano);
            System.out.println("FechaDeNacimiento: " + dto.fechaDeNacimiento);
            
            System.out.println("=== DEBUG: Calling vacuandes.adicionarCiudadano ===");
            Ciudadano ciudadano = vacuandes.adicionarCiudadano(
                dto.tipoDeIdentificacion,
                dto.identificacionCiudadano,
                dto.nombreCiudadano,
                dto.apellidoCiudadano,
                dto.esVacunable,
                dto.fechaDeNacimiento,
                dto.telefonoDeContacto,
                dto.estadoVacunacion,
                dto.etapa,
                dto.nombreOficina,
                dto.regionOficina,
                dto.genero,
                dto.rol,
                dto.ciudad,
                dto.localidad
            );
            
            System.out.println("=== DEBUG: adicionarCiudadano completed ===");
            System.out.println("Returned ciudadano: " + (ciudadano != null ? ciudadano.toString() : "NULL"));
            
            if (ciudadano == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Failed to create ciudadano",
                        "message", "The ciudadano creation returned null - possible database constraint violation",
                        "timestamp", System.currentTimeMillis()
                    ));
            }
            
            System.out.println("=== DEBUG: Creating response ===");
            CiudadanoResponse response = new CiudadanoResponse(ciudadano);
            System.out.println("=== DEBUG: Returning successful response ===");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("=== ERROR in createCiudadano ===");
            System.err.println("Exception: " + e.getClass().getSimpleName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Failed to create ciudadano",
                    "message", e.getMessage() != null ? e.getMessage() : "Unknown error occurred",
                    "exception", e.getClass().getSimpleName(),
                    "timestamp", System.currentTimeMillis(),
                    "details", "Check server logs for more information"
                ));
        }
    }
    
} 