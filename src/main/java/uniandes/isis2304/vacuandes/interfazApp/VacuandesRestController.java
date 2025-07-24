package uniandes.isis2304.vacuandes.interfazApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import uniandes.isis2304.vacuandes.negocio.Ciudadano;
import uniandes.isis2304.vacuandes.negocio.Condicion;
import uniandes.isis2304.vacuandes.negocio.Vacuandes;
import uniandes.isis2304.vacuandes.negocio.VOCiudadano;
import uniandes.isis2304.vacuandes.interfazApp.dtos.CiudadanoResponse;
import uniandes.isis2304.vacuandes.interfazApp.dtos.DTOCiudadano;
import uniandes.isis2304.vacuandes.interfazApp.dtos.DTOCondicion;
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

    /**
     * Endpoint to create a new condition
     * @param dto DTOCondicion with descripcionCondicion (required) and identificadorCondicion (optional)
     * @return ResponseEntity with the created condition
     */
    @PostMapping("/condiciones")
    public ResponseEntity<?> createCondicion(@RequestBody DTOCondicion dto) {
        try {
            System.out.println("=== DEBUG: Received request to create condicion ===");
            System.out.println("DTO: " + dto.toString());
            
            // Validate required fields
            if (dto.descripcionCondicion == null || dto.descripcionCondicion.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "error", "Bad Request",
                        "message", "descripcionCondicion is required and cannot be empty",
                        "timestamp", System.currentTimeMillis()
                    ));
            }
            
            // Generate ID if not provided
            long identificadorCondicion;
            if (dto.identificadorCondicion != null) {
                identificadorCondicion = dto.identificadorCondicion;
                System.out.println("Using provided ID: " + identificadorCondicion);
            } else {
                // Simple auto-generation: find max existing ID + 1
                try {
                    List<Condicion> existingCondiciones = vacuandes.darCondiciones();
                    identificadorCondicion = existingCondiciones.stream()
                        .mapToLong(Condicion::getIdentificadorCondicion)
                        .max()
                        .orElse(0L) + 1;
                    System.out.println("Auto-generated ID: " + identificadorCondicion);
                } catch (Exception e) {
                    System.out.println("Error getting existing conditions, using timestamp-based ID");
                    identificadorCondicion = System.currentTimeMillis() % 1000000; // Simple fallback
                }
            }
            
            System.out.println("=== DEBUG: Calling vacuandes.adicionarCondicion ===");
            Condicion condicion = vacuandes.adicionarCondicion(
                identificadorCondicion,
                dto.descripcionCondicion.trim()
            );
            
            System.out.println("=== DEBUG: adicionarCondicion completed ===");
            System.out.println("Returned condicion: " + (condicion != null ? condicion.toString() : "NULL"));
            
            if (condicion == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Failed to create condicion",
                        "message", "The condicion creation returned null - possible database constraint violation or duplicate ID",
                        "timestamp", System.currentTimeMillis()
                    ));
            }
            
            System.out.println("=== DEBUG: Returning successful response ===");
            return ResponseEntity.ok(Map.of(
                "identificadorCondicion", condicion.getIdentificadorCondicion(),
                "descripcionCondicion", condicion.getDescripcionCondicion(),
                "message", "Condicion created successfully"
            ));
            
        } catch (Exception e) {
            System.err.println("=== ERROR in createCondicion ===");
            System.err.println("Exception: " + e.getClass().getSimpleName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Failed to create condicion",
                    "message", e.getMessage() != null ? e.getMessage() : "Unknown error occurred",
                    "exception", e.getClass().getSimpleName(),
                    "timestamp", System.currentTimeMillis(),
                    "details", "Check server logs for more information"
                ));
        }
    }

    /**
     * Endpoint to get all conditions
     * @return ResponseEntity with list of all conditions
     */
    @GetMapping("/condiciones")
    public ResponseEntity<?> getCondiciones() {
        try {
            System.out.println("=== DEBUG: Fetching all conditions ===");
            List<Condicion> condiciones = vacuandes.darCondiciones();
            
            System.out.println("=== DEBUG: Found " + condiciones.size() + " conditions ===");
            return ResponseEntity.ok(condiciones);
            
        } catch (Exception e) {
            System.err.println("=== ERROR in getCondiciones ===");
            System.err.println("Exception: " + e.getClass().getSimpleName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Failed to fetch conditions",
                    "message", e.getMessage() != null ? e.getMessage() : "Unknown error occurred",
                    "exception", e.getClass().getSimpleName(),
                    "timestamp", System.currentTimeMillis()
                ));
        }
    }
    
} 