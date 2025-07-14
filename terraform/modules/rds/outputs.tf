output "db_instance_id" {
  description = "RDS instance ID"
  value       = aws_db_instance.oracle.id
}

output "db_endpoint" {
  description = "RDS instance endpoint"
  value       = aws_db_instance.oracle.endpoint
}

output "db_port" {
  description = "RDS instance port"
  value       = aws_db_instance.oracle.port
}

output "db_arn" {
  description = "RDS instance ARN"
  value       = aws_db_instance.oracle.arn
}

output "db_security_group_id" {
  description = "Security group ID for RDS"
  value       = aws_security_group.rds.id
}

output "db_subnet_group_name" {
  description = "DB subnet group name"
  value       = aws_db_subnet_group.main.name
} 