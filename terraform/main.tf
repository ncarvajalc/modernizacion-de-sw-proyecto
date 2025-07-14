terraform {
  required_version = ">= 1.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# Data sources
data "aws_availability_zones" "available" {
  state = "available"
}

data "aws_caller_identity" "current" {}

# VPC and Networking
module "vpc" {
  source = "./modules/vpc"
  
  project_name = var.project_name
  environment  = var.environment
  cidr_block   = var.vpc_cidr
  
  availability_zones = data.aws_availability_zones.available.names
  
  tags = var.tags
}

# RDS Oracle Database
module "rds" {
  source = "./modules/rds"
  
  project_name = var.project_name
  environment  = var.environment
  
  vpc_id             = module.vpc.vpc_id
  private_subnet_ids = module.vpc.private_subnet_ids
  
  db_name     = var.db_name
  db_username = var.db_username
  db_password = var.db_password
  
  tags = var.tags
}

# Elastic Beanstalk Application
module "elastic_beanstalk" {
  source = "./modules/elastic-beanstalk"
  
  project_name = var.project_name
  environment  = var.environment
  
  vpc_id            = module.vpc.vpc_id
  public_subnet_ids = module.vpc.public_subnet_ids
  
  # Database connection info
  db_endpoint = module.rds.db_endpoint
  db_port     = module.rds.db_port
  db_name     = var.db_name
  db_username = var.db_username
  db_password = var.db_password
  
  tags = var.tags
  
  # Explicit dependency to ensure RDS is created first
  depends_on = [module.rds]
}

# S3 and CloudFront for future client app
module "client_app" {
  source = "./modules/client-app"
  
  project_name = var.project_name
  environment  = var.environment
  
  tags = var.tags
} 