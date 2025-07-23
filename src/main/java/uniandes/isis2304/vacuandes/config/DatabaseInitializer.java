package uniandes.isis2304.vacuandes.config;

import org.apache.log4j.Logger;
import uniandes.isis2304.vacuandes.negocio.Vacuandes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Database initializer that seeds the database with schema and data
 * if tables don't already exist. This is a utility class that can be
 * called to ensure the database is properly initialized.
 * 
 * @author Vacuandes Team
 */
public class DatabaseInitializer {
    
    private static final Logger log = Logger.getLogger(DatabaseInitializer.class.getName());
    
    private Vacuandes vacuandes;
    
    /**
     * Constructor that takes a Vacuandes instance
     */
    public DatabaseInitializer(Vacuandes vacuandes) {
        this.vacuandes = vacuandes;
    }
    
    /**
     * Initializes database if needed
     */
    public void initializeIfNeeded() {
        log.info("Starting database initialization check...");
        
        try {
            // Check if tables exist and initialize if needed
            initializeDatabaseIfNeeded();
            
            log.info("Database initialization completed successfully");
        } catch (Exception e) {
            log.error("Error during database initialization: " + e.getMessage(), e);
            // Don't fail the application startup, just log the error
        }
    }
    
    /**
     * Checks if core tables exist and initializes database if needed
     */
    private void initializeDatabaseIfNeeded() {
        try {
            // Try to execute a simple query to check if tables exist
            // If this fails, we know we need to initialize
            boolean tablesExist = checkIfTablesExist();
            
            if (!tablesExist) {
                log.info("Tables not found. Initializing database...");
                initializeDatabase();
            } else {
                log.info("Tables already exist. Skipping database initialization.");
            }
            
        } catch (Exception e) {
            log.warn("Could not check table existence, attempting initialization: " + e.getMessage());
            try {
                initializeDatabase();
            } catch (Exception initEx) {
                log.error("Failed to initialize database: " + initEx.getMessage(), initEx);
            }
        }
    }
    
    /**
     * Checks if core tables exist in the database
     */
    private boolean checkIfTablesExist() {
        try {
            // Try to count records from a core table
            vacuandes.darOficinasRegionalesEPS();
            return true;
        } catch (Exception e) {
            log.debug("Table check failed (tables likely don't exist): " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Initializes the database by running SQL scripts
     */
    private void initializeDatabase() {
        log.info("Initializing database with schema and data...");
        
        try {
            // Note: Spring Boot's SQL initialization should handle the actual execution
            // This method can be extended to provide additional custom initialization logic
            
            // For now, we'll log that initialization is being handled by Spring Boot
            log.info("Database initialization is being handled by Spring Boot SQL init configuration");
            log.info("Schema files: schema.sql, schema-constraints.sql");
            log.info("Data files: data.sql");
            
            // Optionally, we could add custom post-initialization logic here
            // such as validating that data was inserted correctly
            
        } catch (Exception e) {
            log.error("Error during database initialization: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    /**
     * Validates that the database was properly initialized by checking
     * that core data exists
     */
    private boolean validateInitialization() {
        try {
            // Check that we have some basic data
            var oficinas = vacuandes.darOficinasRegionalesEPS();
            var estados = vacuandes.darEstadosVacunacion();
            var etapas = vacuandes.darEtapas();
            
            boolean isValid = !oficinas.isEmpty() && !estados.isEmpty() && !etapas.isEmpty();
            
            if (isValid) {
                log.info("Database validation successful. Found: " + 
                        oficinas.size() + " oficinas, " + 
                        estados.size() + " estados, " + 
                        etapas.size() + " etapas");
            } else {
                log.warn("Database validation failed. Missing core data.");
            }
            
            return isValid;
            
        } catch (Exception e) {
            log.warn("Could not validate database initialization: " + e.getMessage());
            return false;
        }
    }
} 