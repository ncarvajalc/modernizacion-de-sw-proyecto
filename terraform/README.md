# Vacuandes Infrastructure

This Terraform configuration sets up the complete infrastructure for the Vacuandes vaccination management system on AWS.

## Architecture

The infrastructure includes:

- **VPC**: Custom VPC with public and private subnets across multiple availability zones
- **RDS Oracle Database**: Smallest/cheapest Oracle instance (`db.t3.micro`) in private subnets
- **Elastic Beanstalk**: Java application hosting environment with auto-scaling
- **S3 + CloudFront**: Static website hosting infrastructure for future client application
- **Security Groups**: Properly configured security groups for all components
- **IAM Roles**: Necessary IAM roles and policies for all services

## Prerequisites

1. AWS CLI configured with appropriate credentials
2. Terraform >= 1.0 installed
3. Docker installed (for building application containers)

## Quick Start

1. **Clone and navigate to the terraform directory**:

   ```bash
   cd terraform
   ```

2. **Copy the example variables file**:

   ```bash
   cp terraform.tfvars.example terraform.tfvars
   ```

3. **Edit terraform.tfvars** with your specific values:

   ```bash
   nano terraform.tfvars
   ```

   **Important**: Make sure to set a secure password for `db_password`.

4. **Initialize Terraform**:

   ```bash
   terraform init
   ```

5. **Plan the deployment**:

   ```bash
   terraform plan
   ```

6. **Apply the configuration**:
   ```bash
   terraform apply
   ```

## Configuration

### Database Configuration

The RDS Oracle instance is configured with:

- **Instance Class**: `db.t3.micro` (smallest/cheapest)
- **Engine**: Oracle Express Edition
- **Storage**: 20GB GP2 with auto-scaling up to 100GB
- **Backup**: 7-day retention
- **Security**: Encrypted storage, VPC-only access

### Elastic Beanstalk Configuration

The Java application environment includes:

- **Instance Type**: `t3.micro`
- **Platform**: Java 11 on Amazon Linux 2
- **Auto Scaling**: 1-2 instances
- **Load Balancer**: Application Load Balancer
- **Health Checks**: Enhanced health monitoring

### Client App Infrastructure

Ready for future React/Vue.js frontend:

- **S3 Bucket**: Static website hosting
- **CloudFront**: CDN with custom error pages for SPA routing
- **Security**: Origin Access Control for S3 protection

## Environment Variables

The following environment variables are automatically configured in Elastic Beanstalk:

- `DB_HOST`: Database endpoint
- `DB_PORT`: Database port (1521)
- `DB_NAME`: Database name
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `SPRING_PROFILES_ACTIVE`: Set to "production"

## Deployment

### Java Application

1. **Build the application**:

   ```bash
   mvn clean package
   ```

2. **Create deployment package**:

   ```bash
   zip -r vacuandes-app.zip Dockerfile src/ target/classes/ lib/ pom.xml
   ```

3. **Deploy to Elastic Beanstalk**:
   - Use AWS Console or AWS CLI
   - Upload the ZIP file to your Elastic Beanstalk environment

### Client Application (Future)

Deploy React/Vue.js apps to the S3 bucket:

```bash
aws s3 sync ./build/ s3://your-client-bucket-name --delete
aws cloudfront create-invalidation --distribution-id YOUR_DISTRIBUTION_ID --paths "/*"
```

## Outputs

After deployment, Terraform will output:

- **VPC ID**: For reference in other configurations
- **Database Endpoint**: For local development connections
- **Elastic Beanstalk URL**: Your application URL
- **CloudFront URL**: Your future client application URL

## Security Notes

1. **Database Security**:

   - Database is in private subnets only
   - Security group restricts access to VPC CIDR only
   - Encrypted storage enabled

2. **Application Security**:

   - Application runs in public subnets but with restricted security groups
   - HTTPS redirect enabled in CloudFront
   - Environment variables for sensitive data

3. **Best Practices**:
   - Use AWS Secrets Manager for production passwords
   - Enable CloudTrail for audit logging
   - Consider using AWS Certificate Manager for SSL certificates

## Cost Optimization

This configuration is optimized for minimal costs:

- **RDS**: `db.t3.micro` instance (free tier eligible)
- **EC2**: `t3.micro` instances (free tier eligible)
- **S3**: Standard storage with lifecycle policies
- **CloudFront**: Free tier includes 1TB transfer

## Monitoring

- **CloudWatch**: Automatic metrics for all services
- **Elastic Beanstalk**: Built-in application monitoring
- **RDS**: Database performance insights available
- **Health Checks**: Application health monitoring at `/health`

## Troubleshooting

### Common Issues

1. **Database Connection Issues**:

   - Check security group rules
   - Verify database endpoint in environment variables
   - Ensure database is running

2. **Application Deployment Issues**:

   - Check Elastic Beanstalk logs
   - Verify Java version compatibility
   - Ensure all dependencies are included

3. **DNS Issues**:
   - CloudFront distribution takes time to propagate
   - Check origin configuration

## Cleanup

To destroy all resources:

```bash
terraform destroy
```

**Warning**: This will permanently delete all resources including the database.

## Next Steps

1. **Configure CI/CD Pipeline**: Set up GitHub Actions or AWS CodePipeline
2. **Add SSL Certificate**: Use AWS Certificate Manager
3. **Enable Monitoring**: Set up CloudWatch alarms
4. **Add Backup Strategy**: Configure RDS snapshots
5. **Implement Web Services**: Add REST API endpoints to the Java application
6. **Build Frontend**: Create React/Vue.js client application
