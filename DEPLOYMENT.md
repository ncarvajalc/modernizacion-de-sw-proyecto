# Vacuandes Deployment Guide

## Issue Resolution

The deployment error you encountered:

```
there is no Procfile and no .jar file at root level of your source bundle
```

This happened because **Elastic Beanstalk expects either a Procfile or a JAR file at the root level** of the deployment package, but our JAR was in the `target/` directory.

## Solution Implemented

### 1. **Created Procfile**

```
web: java -Djava.awt.headless=true -jar vacuandes-1.0.0.jar
```

This tells Elastic Beanstalk how to run your Java application in headless mode (web services).

### 2. **Updated Build Process**

- **deploy-eb.sh**: Builds app, copies JAR to root, creates deployment package
- **deploy-full.sh**: Complete workflow (build + infrastructure deployment)

### 3. **Enhanced Terraform Configuration**

- Added S3 bucket for application versions
- Added application version resource with proper JAR deployment
- Updated Elastic Beanstalk environment to use the application version

## Deployment Options

### Option 1: Quick Deployment (Recommended)

```bash
./deploy-full.sh
```

This runs the complete workflow: build → package → deploy infrastructure → deploy application.

### Option 2: Step-by-Step

```bash
# Step 1: Build and package application
./deploy-eb.sh

# Step 2: Deploy infrastructure
cd terraform
terraform apply
```

## What's Fixed

✅ **JAR Location**: JAR file now copied to root level  
✅ **Procfile**: Created to tell EB how to run the app  
✅ **S3 Integration**: Deployment package uploaded to S3  
✅ **Terraform Integration**: Application version deployed automatically  
✅ **AWS Academy Compatible**: Uses LabRole and LabInstanceProfile

## Application Architecture

```
Root Level:
├── vacuandes-1.0.0-jar-with-dependencies.jar  # Application JAR
├── Procfile                                   # Tells EB how to run
└── vacuandes-deployment.zip                   # Deployment package
```

## Environment Variables

The application receives these environment variables from Terraform:

- `DB_HOST`: RDS Oracle endpoint
- `DB_PORT`: Database port (1521)
- `DB_NAME`: Database name
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JAVA_OPTS`: JVM options (-Xmx512m -Xms512m)

## Monitoring

After deployment, monitor your application:

- **AWS Console**: Elastic Beanstalk → Environments → vacuandes-dev-env
- **Application URL**: From Terraform output
- **Logs**: Available in EB Console or via `eb logs`

## Troubleshooting

If deployment fails:

1. Check EB Console for detailed error messages
2. Review application logs in EB Console
3. Verify JAR file is built correctly: `ls -la target/`
4. Check if all dependencies are included in fat JAR

## Next Steps

After successful deployment:

1. Test application endpoints
2. Verify database connectivity
3. Add monitoring and alerting
4. Configure SSL/TLS (if needed)
5. Set up CI/CD pipeline
