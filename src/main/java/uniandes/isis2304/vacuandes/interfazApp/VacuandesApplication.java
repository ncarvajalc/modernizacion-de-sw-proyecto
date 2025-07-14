package uniandes.isis2304.vacuandes.interfazApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot Application class for Vacuandes
 * 
 * This class enables the REST API functionality while maintaining
 * compatibility with the existing Swing application.
 * 
 * @author Vacuandes Team
 */
@SpringBootApplication
@ComponentScan(basePackages = "uniandes.isis2304.vacuandes")
public class VacuandesApplication {
    
    public static void main(String[] args) {
        // Always run as Spring Boot web application for deployment
        // Set headless mode to prevent GUI creation
        System.setProperty("java.awt.headless", "true");
        
        // Run as Spring Boot web application
        SpringApplication.run(VacuandesApplication.class, args);
    }
} 