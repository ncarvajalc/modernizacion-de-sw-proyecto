#!/bin/bash

# Complete deployment workflow for Vacuandes application

echo "🚀 Starting complete deployment workflow for Vacuandes..."

# Step 1: Build and package application
echo ""
echo "==============================================="
echo "Step 1: Building and packaging application..."
echo "==============================================="
./deploy-eb.sh

if [ $? -ne 0 ]; then
    echo "❌ Application build failed!"
    exit 1
fi

# Step 2: Deploy infrastructure
echo ""
echo "==============================================="
echo "Step 2: Deploying infrastructure..."
echo "==============================================="
cd terraform

# Check if terraform is initialized
if [ ! -d ".terraform" ]; then
    echo "🔧 Initializing Terraform..."
    terraform init
fi

# Plan deployment
echo "📋 Creating deployment plan..."
terraform plan

# Deploy infrastructure
echo "🚀 Deploying infrastructure..."
terraform apply -auto-approve

if [ $? -ne 0 ]; then
    echo "❌ Infrastructure deployment failed!"
    exit 1
fi

cd ..

echo ""
echo "✅ Complete deployment successful!"
echo ""
echo "🌐 Your application should be available at:"
terraform -chdir=terraform output -raw elastic_beanstalk_url

echo ""
echo "📊 Deployment summary:"
echo "   - Application: Built and packaged ✅"
echo "   - Infrastructure: Deployed ✅"
echo "   - Database: Oracle RDS ready ✅"
echo "   - Application: Deployed to Elastic Beanstalk ✅"
echo ""
echo "🔍 To monitor your application:"
echo "   - Check AWS Elastic Beanstalk Console"
echo "   - View logs: eb logs (if EB CLI installed)"
echo "   - Application URL: $(terraform -chdir=terraform output -raw elastic_beanstalk_url)" 