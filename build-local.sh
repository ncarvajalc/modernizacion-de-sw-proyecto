#!/bin/bash

# Local build script for testing with local JARs

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if lib folder exists
if [ ! -d "lib" ]; then
    print_error "lib folder not found. Please ensure all JAR dependencies are in the lib folder."
    exit 1
fi

# Check if required JARs exist
required_jars=(
    "lib/ojdbc8.jar"
    "lib/datanucleus-core-5.1.10.jar"
    "lib/datanucleus-api-jdo-5.1.7.jar"
    "lib/datanucleus-rdbms-5.1.10.jar"
    "lib/javax.jdo-3.2.0-m8.jar"
    "lib/transaction-api-1.1.jar"
    "lib/gson-2.8.5.jar"
    "lib/log4j-1.2.17.jar"
    "lib/junit-4.12.jar"
    "lib/hamcrest-core-1.3.jar"
)

missing_jars=()
for jar in "${required_jars[@]}"; do
    if [ ! -f "$jar" ]; then
        missing_jars+=("$jar")
    fi
done

if [ ${#missing_jars[@]} -gt 0 ]; then
    print_error "Missing required JAR files:"
    for jar in "${missing_jars[@]}"; do
        echo "  - $jar"
    done
    exit 1
fi

print_status "All required JARs found."

# Clean previous build
print_status "Cleaning previous build..."
mvn clean

# Compile the application
print_status "Compiling application..."
mvn compile

# Package the application
print_status "Packaging application..."
mvn package -DskipTests

# Check if JAR was created
if [ -f "target/vacuandes-1.0.0-jar-with-dependencies.jar" ]; then
    print_status "Build successful! JAR created: target/vacuandes-1.0.0-jar-with-dependencies.jar"
else
    print_error "Build failed! JAR not created."
    exit 1
fi

# Test the application locally (optional)
if [ "$1" = "test" ]; then
    print_status "Testing application locally..."
    java -jar target/vacuandes-1.0.0-jar-with-dependencies.jar \
        -Djava.awt.headless=true \
        -Dspring.profiles.active=test &
    
    APP_PID=$!
    
    # Wait for application to start
    sleep 10
    
    # Test health endpoint
    if curl -f http://localhost:8080/api/v1/health > /dev/null 2>&1; then
        print_status "Health check passed!"
    else
        print_warning "Health check failed, but application might still be starting..."
    fi
    
    # Stop the application
    kill $APP_PID
    wait $APP_PID 2>/dev/null
    print_status "Application stopped."
fi

print_status "Build completed successfully!"
print_status "You can now run: java -jar target/vacuandes-1.0.0-jar-with-dependencies.jar" 