#!/bin/bash

# Elastic Beanstalk deployment script for Vacuandes

echo "🚀 Building Vacuandes application for Elastic Beanstalk deployment..."

# Check if JAR already exists at root level
if [ -f "vacuandes-1.0.0.jar" ]; then
    echo "📋 Using existing Spring Boot JAR file at root level..."
else
    # Clean and build the application
    echo "📦 Building application..."
    mvn clean package -DskipTests

    # Check if build was successful
    if [ $? -ne 0 ]; then
        echo "❌ Build failed! Please fix build errors and try again."
        exit 1
    fi

    # Copy the Spring Boot JAR to root level (required by Elastic Beanstalk)
    echo "📋 Copying Spring Boot JAR to root level..."
    cp target/vacuandes-1.0.0.jar ./
fi

# Check if JAR file exists
if [ ! -f "vacuandes-1.0.0.jar" ]; then
    echo "❌ Spring Boot JAR file not found! Build may have failed."
    exit 1
fi

# Create deployment package if it doesn't exist
if [ -f "vacuandes-deployment.zip" ]; then
    echo "📋 Using existing deployment package..."
else
    echo "📦 Creating deployment package..."
    zip -r vacuandes-deployment.zip vacuandes-1.0.0.jar Procfile
fi

echo "✅ Deployment package created: vacuandes-deployment.zip"
echo "📁 Contents:"
unzip -l vacuandes-deployment.zip

# Upload to S3 and create application version if Terraform outputs are available
if [ -f "terraform/terraform.tfstate" ]; then
    echo "🔍 Getting deployment info from Terraform outputs..."
    BUCKET_NAME=$(cd terraform && terraform output -raw elastic_beanstalk_app_versions_bucket 2>/dev/null)
    APP_NAME=$(cd terraform && terraform output -raw elastic_beanstalk_application_name 2>/dev/null)
    ENV_NAME=$(cd terraform && terraform output -raw elastic_beanstalk_environment_name 2>/dev/null)
    
    if [ ! -z "$BUCKET_NAME" ] && [ ! -z "$APP_NAME" ]; then
        echo "☁️  Uploading to S3 bucket: $BUCKET_NAME"
        aws s3 cp vacuandes-deployment.zip s3://$BUCKET_NAME/vacuandes-deployment.zip
        
        if [ $? -eq 0 ]; then
            echo "✅ Successfully uploaded to S3!"
            
            # Create application version
            VERSION_NAME="v$(date +%Y%m%d-%H%M%S)"
            echo "📦 Creating application version: $VERSION_NAME"
            
            aws elasticbeanstalk create-application-version \
                --application-name "$APP_NAME" \
                --version-label "$VERSION_NAME" \
                --description "Deployed on $(date)" \
                --source-bundle S3Bucket="$BUCKET_NAME",S3Key="vacuandes-deployment.zip"
            
            if [ $? -eq 0 ]; then
                echo "✅ Application version created successfully!"
                
                # Update environment to use new version
                if [ ! -z "$ENV_NAME" ]; then
                    echo "🔄 Updating environment to use new version..."
                    aws elasticbeanstalk update-environment \
                        --environment-name "$ENV_NAME" \
                        --version-label "$VERSION_NAME"
                    
                    if [ $? -eq 0 ]; then
                        echo "✅ Environment update initiated!"
                        echo "🌐 Monitor deployment progress in AWS Console"
                    else
                        echo "⚠️  Environment update failed, but version was created"
                    fi
                fi
            else
                echo "❌ Failed to create application version"
            fi
        else
            echo "❌ Failed to upload to S3. You may need to upload manually."
        fi
    else
        echo "⚠️  Could not get required info from Terraform outputs."
        echo "   Run: cd terraform && terraform apply first"
    fi
else
    echo "⚠️  Terraform state not found. Deploy infrastructure first:"
    echo "   cd terraform && terraform apply"
fi

echo ""
echo "🚀 Ready for Elastic Beanstalk deployment!"
echo "📋 Next steps:"
echo "   1. If not done yet: cd terraform && terraform apply"
echo "   2. Application will be automatically deployed to Elastic Beanstalk" 