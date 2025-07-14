output "bucket_name" {
  description = "S3 bucket name for client app"
  value       = aws_s3_bucket.client_app.bucket
}

output "bucket_domain_name" {
  description = "S3 bucket domain name"
  value       = aws_s3_bucket.client_app.bucket_domain_name
}

output "bucket_regional_domain_name" {
  description = "S3 bucket regional domain name"
  value       = aws_s3_bucket.client_app.bucket_regional_domain_name
}

# CloudFront outputs - DISABLED FOR AWS ACADEMY
# output "cloudfront_distribution_id" {
#   description = "CloudFront distribution ID"
#   value       = aws_cloudfront_distribution.client_app.id
# }

# output "cloudfront_domain" {
#   description = "CloudFront domain name"
#   value       = aws_cloudfront_distribution.client_app.domain_name
# }

# output "cloudfront_url" {
#   description = "CloudFront URL"
#   value       = "https://${aws_cloudfront_distribution.client_app.domain_name}"
# }

output "deployment_artifacts_bucket" {
  description = "S3 bucket for deployment artifacts"
  value       = aws_s3_bucket.deployment_artifacts.bucket
} 