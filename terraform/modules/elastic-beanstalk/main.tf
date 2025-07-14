# Get current AWS account ID for constructing role ARNs
data "aws_caller_identity" "current" {}

# Note: IAM Role creation is restricted in AWS Academy
# You need to use existing default roles or create them manually
# For AWS Academy, typically use: aws-elasticbeanstalk-ec2-role and aws-elasticbeanstalk-service-role
#
# Uncomment below for full AWS environments:
#
# resource "aws_iam_role" "ec2_role" {
#   name = "${var.project_name}-${var.environment}-eb-ec2-role"
#   assume_role_policy = jsonencode({
#     Version = "2012-10-17"
#     Statement = [
#       {
#         Action = "sts:AssumeRole"
#         Effect = "Allow"
#         Principal = {
#           Service = "ec2.amazonaws.com"
#         }
#       }
#     ]
#   })
#   tags = var.tags
# }
#
# resource "aws_iam_instance_profile" "ec2_profile" {
#   name = "${var.project_name}-${var.environment}-eb-ec2-profile"
#   role = aws_iam_role.ec2_role.name
# }

# Security Group for Elastic Beanstalk
resource "aws_security_group" "eb_sg" {
  name        = "${var.project_name}-${var.environment}-eb-sg"
  description = "Security group for Elastic Beanstalk"
  vpc_id      = var.vpc_id

  # HTTP access
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # HTTPS access
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Application port (8080 for Spring Boot)
  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # SSH access (optional)
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.tags, {
    Name = "${var.project_name}-${var.environment}-eb-sg"
  })
}

# Elastic Beanstalk Application
resource "aws_elastic_beanstalk_application" "app" {
  name        = "${var.project_name}-${var.environment}-app"
  description = "Vacuandes Java Application"

  # Note: appversion_lifecycle requires IAM service role which is restricted in AWS Academy
  # Uncomment below for full AWS environments:
  # appversion_lifecycle {
  #   service_role          = aws_iam_role.service_role.arn
  #   max_count             = 10
  #   delete_source_from_s3 = true
  # }

  tags = var.tags
}

# S3 Bucket for Application Versions
resource "aws_s3_bucket" "app_versions" {
  bucket = "${var.project_name}-${var.environment}-app-versions-${random_id.bucket_suffix.hex}"
  tags   = var.tags
}

resource "random_id" "bucket_suffix" {
  byte_length = 4
}

# Note: Application version is created by deployment script after S3 upload
# This avoids the chicken-and-egg problem where Terraform tries to create
# an application version before the ZIP file exists in S3

# Elastic Beanstalk Environment
resource "aws_elastic_beanstalk_environment" "env" {
  name                = "${var.project_name}-${var.environment}-env"
  application         = aws_elastic_beanstalk_application.app.name
  solution_stack_name = "64bit Amazon Linux 2023 v4.6.0 running Corretto 11"
  # version_label is set by deployment script after uploading application

  # VPC Configuration
  setting {
    namespace = "aws:ec2:vpc"
    name      = "VPCId"
    value     = var.vpc_id
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "Subnets"
    value     = join(",", var.public_subnet_ids)
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "ELBSubnets"
    value     = join(",", var.public_subnet_ids)
  }

  # Instance Configuration
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "InstanceType"
    value     = "t3.micro"
  }

  # IAM Instance Profile (required for Elastic Beanstalk)
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = "LabInstanceProfile"  # Default instance profile in AWS Academy
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "SecurityGroups"
    value     = aws_security_group.eb_sg.id
  }

  # Auto Scaling Configuration
  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MinSize"
    value     = "1"
  }

  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MaxSize"
    value     = "2"
  }

  # Load Balancer Configuration
  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "LoadBalancerType"
    value     = "application"
  }

  # Service Role (required for Elastic Beanstalk)
  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "ServiceRole"
    value     = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/LabRole"
  }

  setting {
    namespace = "aws:elbv2:listener:80"
    name      = "Protocol"
    value     = "HTTP"
  }

  # Health Check Configuration
  setting {
    namespace = "aws:elasticbeanstalk:healthreporting:system"
    name      = "SystemType"
    value     = "enhanced"
  }

  setting {
    namespace = "aws:elasticbeanstalk:application"
    name      = "Application Healthcheck URL"
    value     = "/health"
  }

  # Java Configuration for Amazon Linux 2023 (using environment variables)
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "JAVA_OPTS"
    value     = "-Xmx512m -Xms512m"
  }

  # Database Environment Variables
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "DB_HOST"
    value     = var.db_endpoint
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "DB_PORT"
    value     = var.db_port
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "DB_NAME"
    value     = var.db_name
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "DB_USERNAME"
    value     = var.db_username
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "DB_PASSWORD"
    value     = var.db_password
  }

  # Spring Profile
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "SPRING_PROFILES_ACTIVE"
    value     = "production"
  }

  tags = var.tags
} 