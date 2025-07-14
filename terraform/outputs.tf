output "vpc_id" {
  description = "ID of the VPC"
  value       = module.vpc.vpc_id
}

output "rds_endpoint" {
  description = "RDS instance endpoint"
  value       = module.rds.db_endpoint
}

output "rds_port" {
  description = "RDS instance port"
  value       = module.rds.db_port
}

output "elastic_beanstalk_application_name" {
  description = "Elastic Beanstalk application name"
  value       = module.elastic_beanstalk.application_name
}

output "elastic_beanstalk_environment_name" {
  description = "Elastic Beanstalk environment name"
  value       = module.elastic_beanstalk.environment_name
}

output "elastic_beanstalk_url" {
  description = "Elastic Beanstalk environment URL"
  value       = module.elastic_beanstalk.environment_url
}

output "elastic_beanstalk_app_versions_bucket" {
  description = "S3 bucket for Elastic Beanstalk application versions"
  value       = module.elastic_beanstalk.app_versions_bucket
}

output "client_app_bucket_name" {
  description = "S3 bucket name for client app"
  value       = module.client_app.bucket_name
}

# CloudFront outputs - DISABLED FOR AWS ACADEMY
# output "client_app_cloudfront_domain" {
#   description = "CloudFront domain for client app"
#   value       = module.client_app.cloudfront_domain
# }

# output "client_app_cloudfront_url" {
#   description = "CloudFront URL for client app"
#   value       = module.client_app.cloudfront_url
# }

# Manual Configuration Instructions for AWS Academy
output "manual_configuration_instructions" {
  description = "Manual configuration steps required for AWS Academy"
  value = <<-EOT
    
    ==========================================
    MANUAL CONFIGURATION REQUIRED (AWS Academy)
    ==========================================
    
    Due to AWS Academy restrictions, the following resources need manual configuration:
    
    1. IAM ROLES FOR ELASTIC BEANSTALK:
       - Go to IAM Console
       - Create role: aws-elasticbeanstalk-ec2-role (if not exists)
       - Attach policies: AWSElasticBeanstalkWebTier, AWSElasticBeanstalkWorkerTier
       - Create role: aws-elasticbeanstalk-service-role (if not exists)
       - Attach policy: AWSElasticBeanstalkService
    
    2. RDS PARAMETER GROUP (Optional):
       - Go to RDS Console > Parameter Groups
       - Create new parameter group for oracle-se2-19
       - Modify parameters: open_cursors=1000, processes=150
       - Apply to RDS instance: ${module.rds.db_instance_id}
    
    3. CLOUDFRONT DISTRIBUTION (DISABLED FOR AWS ACADEMY):
       - CloudFront distribution creation is not allowed in AWS Academy
       - You can access the S3 bucket directly via website hosting endpoint
       - For production, enable CloudFront in a full AWS account
    
    4. ELASTIC BEANSTALK INSTANCE PROFILE:
       - Go to Elastic Beanstalk Console
       - Environment: ${module.elastic_beanstalk.environment_name}
       - Configuration > Security > EC2 instance profile
       - Set to: aws-elasticbeanstalk-ec2-role
    
    ==========================================
    
  EOT
}

output "aws_academy_simplified_setup" {
  description = "Simplified setup commands for AWS Academy"
  value = <<-EOT
    
    ==========================================
    AWS ACADEMY QUICK SETUP COMMANDS
    ==========================================
    
    1. Set up environment variables for your application:
       
       DB_HOST=${module.rds.db_endpoint}
       DB_PORT=${module.rds.db_port}
       DB_NAME=vacuandes
       DB_USERNAME=vacuandes_user
       DB_PASSWORD=your_password_from_tfvars
    
    2. Upload your application to Elastic Beanstalk:
       
       aws elasticbeanstalk create-application-version \
         --application-name ${module.elastic_beanstalk.application_name} \
         --version-label v1.0.0 \
         --source-bundle S3Bucket=your-deployment-bucket,S3Key=vacuandes-deployment.zip
       
       aws elasticbeanstalk update-environment \
         --environment-name ${module.elastic_beanstalk.environment_name} \
         --version-label v1.0.0
    
    3. Access your application at:
       ${module.elastic_beanstalk.environment_url}
    
    ==========================================
    
  EOT
} 