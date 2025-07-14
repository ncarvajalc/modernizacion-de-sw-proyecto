package uniandes.isis2304.vacuandes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uniandes.isis2304.vacuandes.negocio.Vacuandes;

/**
 * Spring Configuration class for Vacuandes
 * 
 * This class provides Spring beans for dependency injection
 * in the REST controllers and configures database connection
 * using environment variables.
 * 
 * @author Vacuandes Team
 */
@Configuration
public class VacuandesConfig {
    
    @Value("${DB_HOST:localhost}")
    private String dbHost;
    
    @Value("${DB_PORT:1521}")
    private String dbPort;
    
    @Value("${DB_NAME:FREEPDB1}")
    private String dbName;
    
    @Value("${DB_USERNAME:ISIS2304B05202110}")
    private String dbUsername;
    
    @Value("${DB_PASSWORD:MXJgcEkeOLBb}")
    private String dbPassword;
    
    /**
     * Creates a Vacuandes bean for dependency injection
     * Sets up database connection properties from environment variables
     */
    @Bean
    public Vacuandes vacuandes() {
        // Set system properties for JDO to use
        // For Oracle RDS, use service name format (not SID format)
        // AWS RDS Oracle uses service names by default
        
        // Handle the case where DB_HOST might already include the port
        String connectionUrl;
        if (dbHost.contains(":")) {
            // DB_HOST already contains port (typical for RDS endpoints)
            connectionUrl = String.format("jdbc:oracle:thin:@//%s/%s", dbHost, dbName);
        } else {
            // DB_HOST is just the hostname, need to add port
            connectionUrl = String.format("jdbc:oracle:thin:@//%s:%s/%s", dbHost, dbPort, dbName);
        }
        
        System.setProperty("db.connection.url", connectionUrl);
        System.setProperty("db.connection.username", dbUsername);
        System.setProperty("db.connection.password", dbPassword);
        
        System.out.println("Database Configuration:");
        System.out.println("URL: " + connectionUrl);
        System.out.println("Username: " + dbUsername);
        System.out.println("Password: " + (dbPassword != null ? "*****" : "null"));
        System.out.println("Database Name: " + dbName);
        System.out.println("Host: " + dbHost);
        System.out.println("Port: " + dbPort);
        
        return new Vacuandes();
    }
} 