# AWS Academy Setup Guide for Vacuandes

This guide provides step-by-step instructions for deploying Vacuandes in an AWS Academy Lab environment, which has certain permission restrictions.

## Prerequisites

1. AWS Academy Lab environment activated
2. AWS CLI configured with lab credentials
3. Terraform installed locally

## Step 1: Deploy Infrastructure

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
# Edit terraform.tfvars with your values
terraform init
terraform plan
terraform apply
```

## Step 2: Manual IAM Configuration

### Create EC2 Instance Profile for Elastic Beanstalk

1. **Go to IAM Console** → Roles
2. **Check if roles exist**:

   - `aws-elasticbeanstalk-ec2-role`
   - `aws-elasticbeanstalk-service-role`

3. **If they don't exist, create them**:

   **For EC2 Role:**

   ```bash
   # Create trust policy file
   cat > eb-ec2-trust-policy.json << EOF
   {
     "Version": "2012-10-17",
     "Statement": [
       {
         "Effect": "Allow",
         "Principal": {
           "Service": "ec2.amazonaws.com"
         },
         "Action": "sts:AssumeRole"
       }
     ]
   }
   EOF

   # Create role (if permissions allow)
   aws iam create-role \
     --role-name aws-elasticbeanstalk-ec2-role \
     --assume-role-policy-document file://eb-ec2-trust-policy.json

   # Attach policies
   aws iam attach-role-policy \
     --role-name aws-elasticbeanstalk-ec2-role \
     --policy-arn arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier

   aws iam attach-role-policy \
     --role-name aws-elasticbeanstalk-ec2-role \
     --policy-arn arn:aws:iam::aws:policy/AWSElasticBeanstalkWorkerTier

   # Create instance profile
   aws iam create-instance-profile \
     --instance-profile-name aws-elasticbeanstalk-ec2-role

   aws iam add-role-to-instance-profile \
     --instance-profile-name aws-elasticbeanstalk-ec2-role \
     --role-name aws-elasticbeanstalk-ec2-role
   ```

## Step 3: Configure Elastic Beanstalk

1. **Go to Elastic Beanstalk Console**
2. **Find your environment** (check terraform output for name)
3. **Configuration** → **Security**
4. **Edit** → Set **EC2 instance profile** to `aws-elasticbeanstalk-ec2-role`
5. **Apply** changes

## Step 4: Build and Deploy Application

```bash
# From project root
./build-local.sh

# Create deployment package
zip -r vacuandes-deployment.zip \
  Dockerfile \
  src/ \
  target/vacuandes-1.0.0-jar-with-dependencies.jar \
  lib/ \
  pom.xml

# Upload to S3 (create bucket first if needed)
aws s3 mb s3://your-deployment-bucket-name
aws s3 cp vacuandes-deployment.zip s3://your-deployment-bucket-name/

# Create application version
aws elasticbeanstalk create-application-version \
  --application-name $(terraform output -raw elastic_beanstalk_application_name) \
  --version-label v1.0.0 \
  --source-bundle S3Bucket=your-deployment-bucket-name,S3Key=vacuandes-deployment.zip

# Deploy to environment
aws elasticbeanstalk update-environment \
  --environment-name $(terraform output -raw elastic_beanstalk_environment_name) \
  --version-label v1.0.0
```

## Step 5: Configure Database Connection

Set environment variables in Elastic Beanstalk:

1. **Go to Elastic Beanstalk Console**
2. **Configuration** → **Software**
3. **Edit** → **Environment properties**
4. **Add the following**:

```
DB_HOST = <from terraform output>
DB_PORT = 1521
DB_NAME = vacuandes
DB_USERNAME = vacuandes_user
DB_PASSWORD = <your password from terraform.tfvars>
SPRING_PROFILES_ACTIVE = production
```

## Step 6: Optional - Configure CloudFront (If Permissions Allow)

If CloudFront configuration failed:

1. **Go to S3 Console**
2. **Find your bucket** (check terraform output)
3. **Permissions** → **Bucket Policy**
4. **Add policy** to allow CloudFront access

5. **Go to CloudFront Console**
6. **Find your distribution** (check terraform output)
7. **Origins** → **Edit**
8. **Set up Origin Access Control** manually

## Step 7: Test Application

1. **Get application URL** from terraform output
2. **Test endpoints**:
   ```bash
   curl https://your-app-url/api/v1/health
   curl https://your-app-url/api/v1/ciudadanos
   ```

## Troubleshooting

### Common Issues

1. **Application won't start**:

   - Check Elastic Beanstalk logs
   - Verify environment variables
   - Ensure database is accessible

2. **Database connection errors**:

   - Check security group rules
   - Verify RDS endpoint and credentials
   - Test connection from EC2 instance

3. **Permission errors**:
   - Verify IAM roles are attached
   - Check AWS Academy lab permissions
   - Some operations may require manual setup

### Useful Commands

```bash
# Check Elastic Beanstalk status
aws elasticbeanstalk describe-environments \
  --environment-names $(terraform output -raw elastic_beanstalk_environment_name)

# View application logs
aws elasticbeanstalk describe-events \
  --environment-name $(terraform output -raw elastic_beanstalk_environment_name)

# Check RDS status
aws rds describe-db-instances \
  --db-instance-identifier $(terraform output -raw rds_endpoint | cut -d'.' -f1)
```

## Cost Management

- **Monitor AWS Academy credits** in AWS Console
- **Stop/Start RDS** when not in use to save costs:

  ```bash
  # Stop RDS
  aws rds stop-db-instance --db-instance-identifier your-db-identifier

  # Start RDS
  aws rds start-db-instance --db-instance-identifier your-db-identifier
  ```

## Next Steps

1. **Implement complete REST API** in Java application
2. **Add authentication/authorization**
3. **Build frontend application**
4. **Set up monitoring and logging**
5. **Configure CI/CD pipeline** (if permissions allow)

---

**Note**: AWS Academy environments have temporary credentials and limited permissions. Some advanced features may require a full AWS account.
