# Vacuandes - Vaccination Management System

A comprehensive vaccination management system built with Java and deployed on AWS infrastructure.

## Overview

Vacuandes is a vaccination management system designed to handle:

- Citizen registration and management
- Vaccination appointments (citas)
- Vaccination points management
- Vaccine inventory tracking
- Reporting and analytics

The system is currently a Java Swing application that is being modernized to include:

- REST API endpoints for web services
- Cloud-native deployment on AWS
- Infrastructure as Code with Terraform
- Containerized deployment with Docker

## Project Structure

```
├── src/main/java/                     # Java source code
│   └── uniandes/isis2304/vacuandes/
│       ├── interfazApp/               # UI and REST controllers
│       ├── negocio/                   # Business logic layer
│       ├── persistencia/              # Data access layer
│       └── config/                    # Spring configuration
├── src/main/resources/                # Application resources
├── terraform/                         # Infrastructure as Code
│   ├── modules/                       # Terraform modules
│   └── *.tf                          # Terraform configuration
├── lib/                              # External JAR dependencies
├── docs/                             # Documentation and SQL scripts
├── Dockerfile                        # Container configuration
├── pom.xml                           # Maven build configuration
└── deploy.sh                         # Deployment script
```

## Technology Stack

### Current Application

- **Java 11**: Core programming language
- **JPA/JDO**: Object-relational mapping
- **DataNucleus**: JPA implementation
- **Oracle Database**: Primary database
- **Java Swing**: GUI framework

### Modernization Stack

- **Spring Boot**: Web framework and dependency injection
- **Spring Web**: REST API endpoints
- **Docker**: Containerization
- **AWS Elastic Beanstalk**: Application hosting
- **AWS RDS**: Managed database service
- **AWS S3 + CloudFront**: Static content delivery

### Infrastructure

- **Terraform**: Infrastructure as Code
- **AWS VPC**: Network isolation
- **AWS Security Groups**: Network security
- **AWS IAM**: Identity and access management

## Quick Start

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Docker
- AWS CLI configured
- Terraform >= 1.0

### Local Development

1. **Clone the repository**:

   ```bash
   git clone <repository-url>
   cd vacuandes
   ```

2. **Build the application**:

   ```bash
   mvn clean compile
   ```

3. **Run the Swing application**:
   ```bash
   ./run.sh
   ```

### Cloud Deployment

1. **Configure Terraform variables**:

   ```bash
   cd terraform
   cp terraform.tfvars.example terraform.tfvars
   # Edit terraform.tfvars with your values
   ```

2. **Deploy everything**:

   ```bash
   ./deploy.sh
   ```

   Or deploy components individually:

   ```bash
   ./deploy.sh infrastructure  # Deploy infrastructure only
   ./deploy.sh build          # Build application only
   ./deploy.sh deploy         # Deploy to existing infrastructure
   ```

## API Endpoints

The application exposes REST endpoints for future frontend integration:

- `GET /api/v1/health` - Health check
- `GET /api/v1/ciudadanos` - Get all citizens
- `GET /api/v1/citas` - Get all appointments
- `GET /api/v1/puntos-vacunacion` - Get vaccination points
- `GET /api/v1/reports/vaccination-index` - Vaccination index report
- `GET /api/v1/reports/citizens-attended` - Citizens attended report
- `GET /api/v1/reports/effective-points` - Most effective points report

## Database Schema

The system uses an Oracle database with tables for:

- `CIUDADANO` - Citizens information
- `CITA` - Vaccination appointments
- `PUNTOVACUNACION` - Vaccination points
- `VACUNA` - Vaccine information
- `LOTEVACUNA` - Vaccine lots
- `TALENTOHUMANO` - Human resources
- And more...

See `docs/ConstruccionBD.sql` for complete schema.

## AWS Architecture

The infrastructure includes:

- **VPC**: Isolated network environment
- **RDS Oracle**: Managed database (db.t3.micro)
- **Elastic Beanstalk**: Application hosting (t3.micro instances)
- **S3 + CloudFront**: Static content delivery for future frontend
- **Security Groups**: Network access control
- **IAM Roles**: Service permissions

## Development Roadmap

### Phase 1: Infrastructure ✅

- [x] Terraform infrastructure setup
- [x] Docker containerization
- [x] AWS deployment pipeline
- [x] Basic REST API skeleton

### Phase 2: API Implementation (In Progress)

- [ ] Complete REST endpoint implementation
- [ ] Authentication and authorization
- [ ] API documentation with Swagger
- [ ] Unit and integration tests

### Phase 3: Frontend Development (Planned)

- [ ] React/Vue.js frontend application
- [ ] Progressive Web App (PWA) features
- [ ] Mobile-responsive design
- [ ] Real-time updates

### Phase 4: Advanced Features (Planned)

- [ ] CI/CD pipeline with GitHub Actions
- [ ] Monitoring and logging
- [ ] SSL certificates
- [ ] Custom domain setup

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## Security

- Database credentials are managed through environment variables
- All traffic is encrypted in transit
- Database is isolated in private subnets
- Regular security updates applied

## Monitoring

The application includes:

- Health check endpoints
- CloudWatch metrics
- Application logs
- Database performance monitoring

## Support

For issues and questions:

1. Check the troubleshooting section in `terraform/README.md`
2. Review application logs in AWS CloudWatch
3. Check Elastic Beanstalk environment health

## License

This project is developed as part of an academic program at Universidad de los Andes.

## Acknowledgments

- Universidad de los Andes - ISIS2304 course
- AWS for cloud infrastructure
- Spring Boot community
- Terraform community
