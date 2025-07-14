# S3 Bucket for static website hosting
resource "aws_s3_bucket" "client_app" {
  bucket = "${var.project_name}-${var.environment}-client-app-${random_id.bucket_suffix.hex}"

  tags = merge(var.tags, {
    Name = "${var.project_name}-${var.environment}-client-app"
  })
}

# Random ID for bucket suffix
resource "random_id" "bucket_suffix" {
  byte_length = 4
}

# S3 Bucket Website Configuration
resource "aws_s3_bucket_website_configuration" "client_app" {
  bucket = aws_s3_bucket.client_app.id

  index_document {
    suffix = "index.html"
  }

  error_document {
    key = "error.html"
  }
}

# S3 Bucket Public Access Block
resource "aws_s3_bucket_public_access_block" "client_app" {
  bucket = aws_s3_bucket.client_app.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

# S3 Bucket Policy for CloudFront
resource "aws_s3_bucket_policy" "client_app" {
  bucket = aws_s3_bucket.client_app.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "AllowCloudFrontServicePrincipal"
        Effect = "Allow"
        Principal = {
          Service = "cloudfront.amazonaws.com"
        }
        Action   = "s3:GetObject"
        Resource = "${aws_s3_bucket.client_app.arn}/*"
        # Condition = {
        #   StringEquals = {
        #     "AWS:SourceArn" = aws_cloudfront_distribution.client_app.arn
        #   }
        # }
        # Note: For AWS Academy, you may need to use a simpler bucket policy
      }
    ]
  })

  depends_on = [aws_s3_bucket_public_access_block.client_app]
}

# Note: CloudFront Origin Access Control creation is restricted in AWS Academy
# You'll need to configure this manually in the AWS Console
# 
# Uncomment below for full AWS environments:
# resource "aws_cloudfront_origin_access_control" "client_app" {
#   name                              = "${var.project_name}-${var.environment}-client-app-oac"
#   description                       = "Origin access control for client app"
#   origin_access_control_origin_type = "s3"
#   signing_behavior                  = "always"
#   signing_protocol                  = "sigv4"
# }

# CloudFront Distribution - DISABLED FOR AWS ACADEMY
# Note: AWS Academy doesn't allow CloudFront distribution creation
# Uncomment below for full AWS environments:
# resource "aws_cloudfront_distribution" "client_app" {
#   origin {
#     domain_name = aws_s3_bucket.client_app.bucket_regional_domain_name
#     origin_id   = "S3-${aws_s3_bucket.client_app.id}"
#     # origin_access_control_id = aws_cloudfront_origin_access_control.client_app.id  # Disabled for AWS Academy
#     
#     # Use legacy Origin Access Identity for AWS Academy
#     s3_origin_config {
#       origin_access_identity = ""  # Will need to be configured manually
#     }
#   }
#
#   enabled             = true
#   is_ipv6_enabled     = true
#   default_root_object = "index.html"
#
#   default_cache_behavior {
#     allowed_methods  = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
#     cached_methods   = ["GET", "HEAD"]
#     target_origin_id = "S3-${aws_s3_bucket.client_app.id}"
#
#     forwarded_values {
#       query_string = false
#       cookies {
#         forward = "none"
#       }
#     }
#
#     viewer_protocol_policy = "redirect-to-https"
#     min_ttl                = 0
#     default_ttl            = 3600
#     max_ttl                = 86400
#   }
#
#   # Cache behavior for API calls (when API is added)
#   ordered_cache_behavior {
#     path_pattern     = "/api/*"
#     allowed_methods  = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
#     cached_methods   = ["GET", "HEAD"]
#     target_origin_id = "S3-${aws_s3_bucket.client_app.id}"
#
#     forwarded_values {
#       query_string = true
#       headers      = ["Authorization", "Content-Type"]
#       cookies {
#         forward = "all"
#       }
#     }
#
#     viewer_protocol_policy = "redirect-to-https"
#     min_ttl                = 0
#     default_ttl            = 0
#     max_ttl                = 0
#   }
#
#   restrictions {
#     geo_restriction {
#       restriction_type = "none"
#     }
#   }
#
#   viewer_certificate {
#     cloudfront_default_certificate = true
#   }
#
#   # Custom error response for SPA routing
#   custom_error_response {
#     error_code         = 404
#     response_code      = 200
#     response_page_path = "/index.html"
#   }
#
#   custom_error_response {
#     error_code         = 403
#     response_code      = 200
#     response_page_path = "/index.html"
#   }
#
#   tags = merge(var.tags, {
#     Name = "${var.project_name}-${var.environment}-client-app-distribution"
#   })
# }

# S3 Bucket for deployment artifacts
resource "aws_s3_bucket" "deployment_artifacts" {
  bucket = "${var.project_name}-${var.environment}-deployment-artifacts-${random_id.artifacts_suffix.hex}"

  tags = merge(var.tags, {
    Name = "${var.project_name}-${var.environment}-deployment-artifacts"
  })
}

resource "random_id" "artifacts_suffix" {
  byte_length = 4
}

# S3 Bucket versioning for deployment artifacts
resource "aws_s3_bucket_versioning" "deployment_artifacts" {
  bucket = aws_s3_bucket.deployment_artifacts.id
  versioning_configuration {
    status = "Enabled"
  }
} 