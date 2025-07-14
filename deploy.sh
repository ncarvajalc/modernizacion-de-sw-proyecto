#!/bin/bash

# Vacuandes Deployment Script
# This script builds the Java application and deploys it to AWS Elastic Beanstalk

set -e

# Configuration
PROJECT_NAME="vacuandes"
ENVIRONMENT="dev"
ZIP_FILE="vacuandes-deployment.zip"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if required tools are installed
check_requirements() {
    print_status "Checking requirements..."
    
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed. Please install Maven first."
        exit 1
    fi
    
    if ! command -v aws &> /dev/null; then
        print_error "AWS CLI is not installed. Please install AWS CLI first."
        exit 1
    fi
    
    if ! command -v terraform &> /dev/null; then
        print_error "Terraform is not installed. Please install Terraform first."
        exit 1
    fi
    
    print_status "All requirements satisfied."
}

# Build the Java application
build_application() {
    print_status "Building Java application..."
    
    # Clean and package
    mvn clean package -DskipTests
    
    # Check if fat JAR was created
    if [ ! -f "target/vacuandes-1.0.0-jar-with-dependencies.jar" ]; then
        print_error "Build failed! Fat JAR not created."
        exit 1
    fi
    
    print_status "Application built successfully."
}

# Create deployment package
create_deployment_package() {
    print_status "Creating deployment package..."
    
    # Remove existing package
    rm -f $ZIP_FILE
    
    # Create deployment package
    zip -r $ZIP_FILE \
        Dockerfile \
        src/ \
        target/vacuandes-1.0.0-jar-with-dependencies.jar \
        lib/ \
        pom.xml \
        -x "*.log" "*.DS_Store" "target/maven-archiver/*" "target/test-classes/*"
    
    print_status "Deployment package created: $ZIP_FILE"
}

# Deploy infrastructure
deploy_infrastructure() {
    print_status "Deploying infrastructure..."
    
    cd terraform
    
    # Check if terraform.tfvars exists
    if [ ! -f "terraform.tfvars" ]; then
        print_warning "terraform.tfvars not found. Copying from example..."
        cp terraform.tfvars.example terraform.tfvars
        print_warning "Please edit terraform.tfvars with your specific values before continuing."
        read -p "Press Enter to continue after editing terraform.tfvars..."
    fi
    
    # Initialize and apply
    terraform init
    terraform plan
    read -p "Do you want to apply this infrastructure? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        terraform apply
    else
        print_warning "Infrastructure deployment cancelled."
        cd ..
        return 1
    fi
    
    cd ..
    print_status "Infrastructure deployed successfully."
}

# Get Elastic Beanstalk application name
get_eb_application_name() {
    cd terraform
    APPLICATION_NAME=$(terraform output -raw elastic_beanstalk_application_name 2>/dev/null || echo "")
    ENVIRONMENT_NAME=$(terraform output -raw elastic_beanstalk_environment_name 2>/dev/null || echo "")
    cd ..
    
    if [ -z "$APPLICATION_NAME" ] || [ -z "$ENVIRONMENT_NAME" ]; then
        print_error "Could not retrieve Elastic Beanstalk application details from Terraform output."
        exit 1
    fi
    
    print_status "Using Elastic Beanstalk Application: $APPLICATION_NAME"
    print_status "Using Elastic Beanstalk Environment: $ENVIRONMENT_NAME"
}

# Deploy to Elastic Beanstalk
deploy_to_eb() {
    print_status "Deploying to Elastic Beanstalk..."
    
    # Create application version
    VERSION_LABEL="v$(date +%Y%m%d-%H%M%S)"
    
    # Upload to S3 and create application version
    aws elasticbeanstalk create-application-version \
        --application-name "$APPLICATION_NAME" \
        --version-label "$VERSION_LABEL" \
        --source-bundle S3Bucket="elasticbeanstalk-$(aws sts get-caller-identity --query Account --output text)-$(aws configure get region)",S3Key="$ZIP_FILE"
    
    # Upload the ZIP file to S3
    aws s3 cp "$ZIP_FILE" "s3://elasticbeanstalk-$(aws sts get-caller-identity --query Account --output text)-$(aws configure get region)/"
    
    # Create application version
    aws elasticbeanstalk create-application-version \
        --application-name "$APPLICATION_NAME" \
        --version-label "$VERSION_LABEL" \
        --source-bundle S3Bucket="elasticbeanstalk-$(aws sts get-caller-identity --query Account --output text)-$(aws configure get region)",S3Key="$ZIP_FILE"
    
    # Deploy to environment
    aws elasticbeanstalk update-environment \
        --environment-name "$ENVIRONMENT_NAME" \
        --version-label "$VERSION_LABEL"
    
    print_status "Deployment initiated. Version: $VERSION_LABEL"
    print_status "Monitor deployment status in AWS Console or with:"
    print_status "aws elasticbeanstalk describe-environments --environment-names $ENVIRONMENT_NAME"
}

# Main deployment function
main() {
    print_status "Starting Vacuandes deployment..."
    
    check_requirements
    
    case "${1:-all}" in
        "build")
            build_application
            create_deployment_package
            ;;
        "infrastructure")
            deploy_infrastructure
            ;;
        "deploy")
            get_eb_application_name
            deploy_to_eb
            ;;
        "all")
            build_application
            create_deployment_package
            deploy_infrastructure
            get_eb_application_name
            deploy_to_eb
            ;;
        *)
            echo "Usage: $0 [build|infrastructure|deploy|all]"
            echo "  build         - Build application and create deployment package"
            echo "  infrastructure - Deploy infrastructure only"
            echo "  deploy        - Deploy application to existing infrastructure"
            echo "  all           - Build, deploy infrastructure, and deploy application (default)"
            exit 1
            ;;
    esac
    
    print_status "Deployment completed successfully!"
}

# Run main function
main "$@" 