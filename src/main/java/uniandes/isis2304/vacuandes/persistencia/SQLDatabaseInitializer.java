package uniandes.isis2304.vacuandes.persistencia;

import org.apache.log4j.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Database initializer that works with DataNucleus/JDO
 * Executes SQL scripts through the PersistenceManager to seed the database
 * 
 * @author Vacuandes Team
 */
public class SQLDatabaseInitializer {
    
    private static final Logger log = Logger.getLogger(SQLDatabaseInitializer.class.getName());
    
    /**
     * Initializes the database if tables don't exist
     * @param pm PersistenceManager to use for database operations
     */
    public static void initializeDatabase(PersistenceManager pm) {
        log.info("Starting database initialization check...");
        
        try {
            if (!tablesExist(pm)) {
                log.info("Tables not found. Creating schema and seeding data...");
                createTablesAndSeedData(pm);
                log.info("Database initialization completed successfully");
            } else {
                log.info("Tables already exist. Skipping database initialization.");
            }
        } catch (Exception e) {
            log.error("Error during database initialization: " + e.getMessage(), e);
        }
    }
    
    /**
     * Checks if core tables exist
     */
    private static boolean tablesExist(PersistenceManager pm) {
        try {
            // Try to query a core table
            pm.newQuery("SELECT COUNT(*) FROM OFICINAREGIONALEPS").execute();
            return true;
        } catch (Exception e) {
            log.debug("Tables don't exist: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Creates tables and seeds data
     */
    private static void createTablesAndSeedData(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        
        try {
            tx.begin();
            
            // Execute schema creation
            log.info("Creating database schema...");
            executeSQL(pm, "schema-simple.sql");
            
            log.info("Adding constraints...");
            executeSQL(pm, "constraints-simple.sql");
            
            log.info("Seeding data...");
            executeSQL(pm, "data-simple.sql");
            
            tx.commit();
            log.info("Database initialization completed successfully");
            
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.error("Failed to initialize database: " + e.getMessage(), e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Executes SQL commands from a resource file
     */
    private static void executeSQL(PersistenceManager pm, String resourcePath) {
        try {
            InputStream is = SQLDatabaseInitializer.class.getClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                log.warn("SQL resource not found: " + resourcePath);
                return;
            }
            
            List<String> statements = parseSQL(is);
            
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    try {
                        log.debug("Executing SQL: " + statement.substring(0, Math.min(50, statement.length())) + "...");
                        pm.newQuery("javax.jdo.query.SQL", statement).execute();
                    } catch (Exception e) {
                        // Some statements might fail if objects already exist, which is OK
                        log.debug("SQL statement failed (might be expected): " + e.getMessage());
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to execute SQL from " + resourcePath + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Parses SQL file into individual statements
     */
    private static List<String> parseSQL(InputStream is) throws Exception {
        List<String> statements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Skip comments and empty lines
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }
                
                currentStatement.append(line).append(" ");
                
                // Check for statement terminator
                if (line.endsWith(";")) {
                    String statement = currentStatement.toString().trim();
                    if (!statement.isEmpty()) {
                        // Remove the semicolon as JDO doesn't like it
                        statement = statement.substring(0, statement.length() - 1);
                        statements.add(statement);
                    }
                    currentStatement = new StringBuilder();
                }
            }
            
            // Add any remaining statement
            String statement = currentStatement.toString().trim();
            if (!statement.isEmpty()) {
                statements.add(statement);
            }
        }
        
        return statements;
    }
} 