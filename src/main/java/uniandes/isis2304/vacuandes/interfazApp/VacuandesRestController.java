package uniandes.isis2304.vacuandes.interfazApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniandes.isis2304.vacuandes.negocio.Vacuandes;
import uniandes.isis2304.vacuandes.negocio.VOCiudadano;
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
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity.internalServerError()
            .body(Map.of(
                "error", "Internal Server Error",
                "message", e.getMessage()
            ));
    }
} 