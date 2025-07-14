# Security Group for RDS
resource "aws_security_group" "rds" {
  name        = "${var.project_name}-${var.environment}-rds-sg"
  description = "Security group for RDS Oracle database"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 1521
    to_port     = 1521
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
    Name = "${var.project_name}-${var.environment}-rds-sg"
  })
}

# DB Subnet Group
resource "aws_db_subnet_group" "main" {
  name       = "${var.project_name}-${var.environment}-db-subnet-group"
  subnet_ids = var.private_subnet_ids

  tags = merge(var.tags, {
    Name = "${var.project_name}-${var.environment}-db-subnet-group"
  })
}

# Note: Parameter Group creation with custom parameters is restricted in AWS Academy
# Using default parameter group instead - custom parameters need manual configuration
# Uncomment below for full AWS environments:
#
# resource "aws_db_parameter_group" "oracle" {
#   family = "oracle-se2-19"
#   name   = "${var.project_name}-${var.environment}-oracle-params"
#
#   parameter {
#     name  = "open_cursors"
#     value = "1000"
#   }
#
#   parameter {
#     name  = "processes"
#     value = "150"
#   }
#
#   tags = merge(var.tags, {
#     Name = "${var.project_name}-${var.environment}-oracle-params"
#   })
# }

# RDS Oracle Instance (smallest/cheapest)
resource "aws_db_instance" "oracle" {
  identifier = "${var.project_name}-${var.environment}-oracle"

  # Oracle Standard Edition - smallest supported option for RDS
  engine         = "oracle-se2"
  engine_version = "19.0.0.0.ru-2023-04.rur-2023-04.r1"
  instance_class = "db.t3.small"  # Minimum supported instance class for Oracle SE2
  license_model  = "license-included"  # Required for Oracle RDS

  allocated_storage     = 20
  max_allocated_storage = 100
  storage_type          = "gp2"
  storage_encrypted     = true

  # Database configuration
  db_name  = var.db_name
  username = var.db_username
  password = var.db_password

  # Network configuration
  db_subnet_group_name   = aws_db_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  publicly_accessible    = false
  # parameter_group_name   = aws_db_parameter_group.oracle.name  # Disabled for AWS Academy

  # Maintenance and backup
  backup_retention_period = 7
  backup_window          = "03:00-04:00"
  maintenance_window     = "sun:04:00-sun:05:00"

  # Performance and monitoring
  performance_insights_enabled = false
  monitoring_interval         = 0

  # Skip final snapshot for development
  skip_final_snapshot       = true
  delete_automated_backups  = true
  deletion_protection       = false

  tags = merge(var.tags, {
    Name = "${var.project_name}-${var.environment}-oracle"
  })
} 