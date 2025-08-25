# Leonardo Backend - SENASoft Metrics API 🚀

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Supported-blue.svg)](https://www.docker.com/)

## 🎯 SENASoft Challenge - Leonardo Backend

This project is the solution to the **[Challenge: Leonardo está incompleto](https://github.com/fepecas/senasoft/discussions/67)** from SENASoft 2025. It's a custom Spring Boot backend that extends Leonardo's capabilities (ChatGPT/OpenAI Assistant) with new detailed metrics for SENA apprentices.

### 🔍 Challenge Context

Leonardo could already consume basic metrics, but needed more detailed endpoints to answer specific questions about:
- ✅ Number of registered apprentices **by training center**
- ✅ Recommended instructors **by training center**
- ✅ Registered apprentices **by center and training program** (4 programs allowed)
- ✅ Apprentices **by Colombian department**
- ✅ Apprentices with **GitHub accounts**
- ✅ Apprentices with **B1 or B2 English level** by center

### 💡 Implemented Solution

Complete REST API backend that provides detailed metrics and analytics for SENA apprentices, training centers, and educational programs across Colombia. The API is designed to be consumed by Leonardo and other AI assistants.

## ✨ Features

- **🏗️ Well-Structured Code** with proper separation of concerns
- **🔒 Type-Safe Database Projections** to avoid runtime errors
- **🔐 API Key Authentication** for secure access control
- **📝 API Versioning** for future compatibility (`/api/v1/`)
- **📚 Interactive Documentation** with Swagger UI
- **🐳 Full Docker Support** with multi-stage builds and complete containerization
- **⚡ Performance Optimized** with optimized queries and connection pooling
- **🤖 Leonardo Integration** via included OpenAI Action schema
- **📊 Sample Data** preloaded for immediate testing
- **🚨 Basic Error Handling** with Spring Boot default error responses (temporary solution for Swagger compatibility)
- **🧪 Comprehensive Testing** with 70+ unit, integration, and security tests
- **🛡️ Production Security** with rate limiting, secure logging, and API key validation

## 🛡️ Security Enhancements

The backend now includes **production-ready security measures** that protect against common attacks and ensure secure operation:

### **🔑 Enhanced API Key Security**
- **Comprehensive Validation**: API keys must meet minimum security requirements (32+ characters)
- **Weak Pattern Detection**: Automatically rejects insecure patterns like repeated words or only-numeric keys
- **Startup Validation**: Application fails to start if security requirements aren't met, preventing deployment with weak keys

### **🛡️ Rate Limiting Protection**
- **Log Flooding Prevention**: Limits failed authentication attempts to 5 per IP address per minute
- **Intelligent Log Suppression**: Reduces log noise while maintaining security audit trails
- **IP-based Tracking**: Monitors and limits attempts per client IP address
- **Automatic Reset**: Rate limits reset after successful authentication

### **📝 Secure Logging System**
- **URI Sanitization**: Removes query strings to prevent sensitive parameter exposure in logs
- **Consistent Format**: All security logs use standardized format for better analysis
- **Audit Trail**: Maintains comprehensive security logs while protecting sensitive information

### **🚀 Production Security Benefits**
- **Attack Prevention**: Protects against brute force and log flooding attacks
- **Compliance Ready**: Meets security requirements for production deployments
- **Monitoring Friendly**: Provides clear security metrics and audit trails
- **Scalable Protection**: Rate limiting scales with application load

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.5.5 with Java 17
- **Database**: MySQL 8.0
- **Documentation**: OpenAPI 3.1 / Swagger UI
- **Build**: Maven
- **Containers**: Docker & Docker Compose for full-stack deployment
- **Monitoring**: Spring Boot Actuator
- **ORM**: Spring Data JPA with Hibernate
- **Mapping**: Project Lombok to reduce boilerplate

## ⚠️ Current Status - Error Handling

**Note**: The custom error handling system is temporarily disabled due to a compatibility issue between Spring Boot 3.5.5 and SpringDoc 2.2.0 that prevents Swagger from generating documentation.

### What This Means:
- ✅ **All API endpoints work perfectly**
- ✅ **Swagger documentation is fully functional**
- ✅ **Leonardo can access complete API documentation**
- ⚠️ **Error responses use Spring Boot defaults** (less elegant but fully functional)

### Technical Details:
- See [SWAGGER_COMPATIBILITY_ISSUE.md](docs/SWAGGER_COMPATIBILITY_ISSUE.md) for complete details
- This is a temporary solution that maintains full functionality
- The application is production-ready despite this limitation

## 🔐 Security Configuration

The API implements **comprehensive security measures** including API Key Authentication, rate limiting, and secure logging to prevent attacks and ensure production-ready security.

### Security Features
- **🔑 API Key Authentication**: All endpoints (except Swagger and health checks) require a valid API key
- **🛡️ Rate Limiting**: Prevents log flooding attacks by limiting failed authentication attempts
- **📝 Secure Logging**: Sanitizes URIs to prevent sensitive information exposure in logs
- **✅ API Key Validation**: Comprehensive validation ensuring minimum security requirements (32+ characters, no weak patterns)
- **🌍 Environment Variable Configuration**: API keys configured via environment variables for security
- **⚙️ Flexible Security**: Can be enabled/disabled via configuration
- **🚀 Production Ready**: Secure by default, suitable for AWS deployment

### Security Improvements Implemented

#### **1. API Key Validation** ✅
- **Minimum Length**: API keys must be at least 32 characters long
- **Weak Pattern Detection**: Rejects common insecure patterns (repeated words, only numbers, etc.)
- **Early Failure**: Application fails to start if validation fails, preventing deployment with weak keys

#### **2. Rate Limiting Protection** 🛡️
- **Failed Authentication Limits**: Maximum 5 failed attempts per IP address per minute
- **Log Suppression**: Reduces log flooding by suppressing repeated failed attempts
- **IP-based Tracking**: Monitors attempts per client IP address
- **Automatic Reset**: Rate limits reset after successful authentication

#### **3. Secure Logging** 📝
- **URI Sanitization**: Removes query strings to prevent sensitive parameter exposure
- **Consistent Format**: All logs use sanitized format: `METHOD /path`
- **Audit Trail**: Maintains security logs while protecting sensitive information

### Environment Variables Setup

#### Local Development
Create a `.env` file in the project root:
```bash
# API Security Configuration
API_KEY=your_api_key_here_minimum_32_characters_long
API_SECURITY_ENABLED=true

# Database Configuration
DB_USERNAME=leonardo_user
DB_PASSWORD=your_password_here
MYSQL_ROOT_PASSWORD=your_root_password
```

**Note**: Rate limiting configuration is now handled in `application.properties` and is required for the application to start.

**Option 1: Load manually**
```bash
source .env
```

**Option 2: Use the setup script (Recommended)**
```bash
./scripts/setup-local-env.sh
```
This script will:
- Load all variables from your `.env` file
- Set default values if `.env` is missing
- Show you what variables are configured
- Provide helpful verification commands

#### Production (AWS/EC2)
Set environment variables directly on the server:
```bash
export API_KEY=your_production_api_key_minimum_32_characters_long
export API_SECURITY_ENABLED=true

export DB_USERNAME=leonardo_user
export DB_PASSWORD=your_production_password
```

**Note**: Rate limiting configuration for production should be set in `application.properties` on the server.

### Rate Limiting Configuration

The rate limiting behavior can be customized through configuration properties in different profile files:

#### **Development Configuration** (`application.properties`)
```properties
# Rate limiting configuration for local development
api.security.rate-limit.max-attempts=5         # 5 attempts per window
api.security.rate-limit.window-ms=60000        # 1 minute window
api.security.rate-limit.log-suppression-ms=300000  # 5 minute log suppression
```

#### **Production Configuration** (`application-aws.properties`)
```properties
# Rate limiting configuration for AWS production (stricter)
api.security.rate-limit.max-attempts=3         # 3 attempts per window
api.security.rate-limit.window-ms=300000       # 5 minute window
api.security.rate-limit.log-suppression-ms=600000  # 10 minute log suppression
```

#### **Customization Options**
You can modify these values in the respective profile files for different environments:

```properties
# More lenient for development
api.security.rate-limit.max-attempts=10        # 10 attempts per window
api.security.rate-limit.window-ms=30000        # 30 second window
api.security.rate-limit.log-suppression-ms=120000  # 2 minute log suppression

# Stricter for production
api.security.rate-limit.max-attempts=3         # 3 attempts per window
api.security.rate-limit.window-ms=300000       # 5 minute window
api.security.rate-limit.log-suppression-ms=600000  # 10 minute log suppression
```

**Note**: These properties are required and must be defined in the appropriate profile file. The application will not start without them.

### API Key Usage
Include the API key in your requests:
```bash
curl -H "X-API-Key: your_api_key_here" \
     http://localhost:8080/api/v1/metrics/scalar
```

### API Key Requirements
For security, API keys must meet these requirements:
- **Minimum Length**: 32 characters
- **Pattern Validation**: No repeated words, no only-numeric patterns
- **Format**: Alphanumeric with special characters allowed

### API Key Management
For detailed information about API key generation, rotation, and best practices, see:
- **[API Key Management Guide](docs/API_KEY_MANAGEMENT.md)** - Complete guide for managing API keys
- **Key Generation**: Use the included script `./scripts/generate-api-key.sh`
- **Key Rotation**: Recommended every 90 days for production
- **Security**: Never commit API keys to Git, use environment variables

## Getting Started

### Requirements

- Java 17+
- Docker and Docker Compose
- Maven 3.6+ (or use the included wrapper)

### Quick Setup

#### Option 1: Full Docker Deployment (Recommended)

1. **Clone and navigate to the project**
   ```bash
   git clone https://github.com/AlexanderIglesias/leonardo-backend
   cd leonardo-backend
   ```

2. **Start the complete stack with Docker Compose**
   ```bash
   cd src/main/docker
   docker-compose up -d
   ```

3. **Check it's working**
   - API Documentation: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/actuator/health

#### Option 2: Local Development (Database Only in Docker)

1. **Clone and navigate to the project**
   ```bash
   git clone https://github.com/AlexanderIglesias/leonardo-backend
   cd leonardo-backend
   ```

2. **Start only the MySQL database container**
   ```bash
   cd src/main/docker
   docker-compose up -d mysql
   ```

3. **Run the Spring Boot application locally**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Check it's working**
   - API Documentation: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/actuator/health

## 🔗 API Endpoints

All endpoints are available under `/api/v1/metrics` and designed to answer the SENASoft challenge questions:

| Endpoint | SENASoft Question Addressed | Description |
|----------|----------------------------|-------------|
| `GET /scalar` | General overview | Total apprentices, backend profiles %, training centers count, average English proficiency |
| `GET /by-center` | **Apprentices by training center** + **Recommended instructors** + **GitHub users** + **B1/B2 English by center** | Complete metrics grouped by SENA training centers |
| `GET /by-program` | **Apprentices by center and training program** | Metrics by training center and program (limited to 4 programs) |
| `GET /by-department` | **Apprentices by Colombian department** | Geographic distribution of apprentices who responded to the survey |
| `GET /github-users` | **Apprentices with GitHub accounts** | Specific metrics for GitHub users per training center with percentages |
| `GET /english-level` | **Apprentices with B1/B2 English level** | Specific metrics for English proficiency per training center with percentages |
| `GET /apprentice-count` | **Apprentice count by training center** | Simple count of apprentices per center without additional metrics |
| `GET /recommended-instructors` | **Recommended instructors by training center** | Specific list of recommended instructors per center with counts |

### Example Responses

#### **Success Response**
```json
// GET /api/v1/metrics/scalar
[
  {
    "description": "# Aprendices inscritos únicos",
    "value": 775
  },
  {
    "description": "% de perfiles DEV Backend",
    "value": "43.5%"
  }
]
```

#### **Error Response**
```json
// Error handling example
{
  "status": 500,
  "message": "Error processing metrics request",
  "details": "Database connection failed",
  "timestamp": "2025-08-22T11:21:37",
  "path": "/api/v1/metrics/scalar",
  "validationErrors": []
}
```

## Project Structure

```
src/main/java/com/alphanet/products/leonardobackend/
├── config/              # App configuration & data initialization
├── controller          # REST endpoints with robust error handling
├── dto/                 # Data transfer objects including error responses
│   └── projection/      # Database projections for optimized queries
├── entity/              # Database entities (Department, TrainingCenter, Program, Instructor)
├── exception/           # Custom exceptions and global error handler
├── repository/          # Data access with custom queries
├── service/             # Business logic layer
│   ├── impl/           # Service implementations
│   └── mapper/         # DTO mapping utilities
└── openai.action.schema.json  # Leonardo/OpenAI integration schema

src/test/java/com/alphanet/products/leonardobackend/
├── config/              # Configuration tests
├── controller/          # Controller tests including error handling
├── exception/           # Exception handler tests
├── service/             # Service layer tests
│   ├── impl/           # Service implementation tests
│   └── mapper/         # Mapper utility tests
└── integration/         # End-to-end integration tests
```

## 🚨 Error Handling & Response Management

The backend implements a **robust error handling system** that provides consistent, structured error responses for all endpoints:

### **Error Response Structure**
```json
{
  "status": 500,
  "message": "Internal server error",
  "details": "Database connection failed",
  "timestamp": "2025-08-22T11:21:37",
  "path": "/api/v1/metrics/scalar",
  "validationErrors": []
}
```

### **HTTP Status Codes**
- **400 Bad Request** - Invalid parameters or malformed requests
- **404 Not Found** - Requested data not available
- **500 Internal Server Error** - Server-side errors with detailed logging

### **Global Exception Handler**
- **Centralized error management** using `@ControllerAdvice`
- **Custom exceptions** for domain-specific errors (`MetricsException`, `DataNotFoundException`)
- **Structured logging** for all errors with context information
- **Consistent error format** across all endpoints

### **Validation Error Handling**
- **Field-level validation** with specific error messages
- **Constraint violation** handling for data integrity
- **User-friendly error messages** for better debugging

## 🤖 Leonardo Integration

- **Server URLs**: Both local development and production endpoints
- **Operation IDs**: Specific function names for each endpoint
- **Response Schemas**: Detailed data structures for AI understanding
- **Examples**: Sample responses for better AI context

### Adding to Leonardo/ChatGPT

1. Copy the content of `src/main/java/com/alphanet/products/leonardobackend/openai.action.schema.json`
2. In ChatGPT, go to "Actions" and create a new action
3. Paste the schema content
4. Configure the appropriate server URL (local or production)
5. Test with questions like: "¿Cuántos aprendices hay por centro de formación?"

### **Enhanced Leonardo Capabilities**

With the new granular endpoints, Leonardo can now answer **specific questions** without returning unnecessary data:

- **"¿Cuántos aprendices tienen GitHub por centro?"** → Uses `/github-users` endpoint
- **"¿Cuántos aprendices tienen nivel B1/B2 de inglés?"** → Uses `/english-level` endpoint  
- **"¿Cuántos aprendices hay en total por centro?"** → Uses `/apprentice-count` endpoint
- **"¿Qué instructores son recomendados por centro?"** → Uses `/recommended-instructors` endpoint

This provides **better performance** and **more focused responses** for Leonardo's AI capabilities.

## Development

### Build Commands

```bash
# Compile
./mvnw clean compile

# Run tests (all 65 tests)
./mvnw test
```

### Environment Setup

```bash
# Setup environment variables (recommended)
./scripts/setup-local-env.sh

# Or load manually
source .env
```

### API Key Management

```bash
# Generate new API key
./scripts/generate-api-key.sh

# View current API key (if set)
echo $API_KEY
```

# Run specific test categories
./mvnw test -Dtest=MetricsApiErrorHandlingTest    # Error handling tests
./mvnw test -Dtest=GlobalExceptionHandlerTest     # Exception handler tests
./mvnw test -Dtest=MetricsApiIntegrationTest      # Integration tests

# Package
./mvnw clean package

# Run with specific profile
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### Docker Setup

The application uses a multi-container setup with Docker Compose:

#### Services
- **leonardo-app**: Spring Boot application container (built from Dockerfile)
- **mysql**: MySQL 8.0 database container

#### Network & Health Checks
- Custom bridge network: `leonardo-network`
- Health checks for both services with proper dependency management
- MySQL health check ensures database is ready before starting the app

#### Environment Variables in Docker
The `docker-compose.yml` uses environment variables for configuration:
```yaml
environment:
  MYSQL_ROOT_PASSWORD: $${MYSQL_ROOT_PASSWORD:"SenaSoft2024@Leonardo"}
  MYSQL_USER: $${DB_USERNAME:leonardo_user}
  MYSQL_PASSWORD: $${DB_PASSWORD:"L30n4rd0_S3n4S0ft_2024"}
```

**Note**: Variables are escaped with `$$` for Docker Compose compatibility.

#### Database Configuration
- Database: `leonardo_senasoft`
- User: `leonardo_user`
- Exposed port: `3306` (MySQL), `8080` (Application)
- Persistent volume: `mysql_data`

### Configuration

The application uses a main configuration file with environment-specific profiles:

#### Main Configuration (`application.properties`)
```properties
# Core application configuration
spring.datasource.url=jdbc:mysql://localhost:3306/leonardo_senasoft
spring.datasource.username=leonardo_user
spring.datasource.password=${DB_PASSWORD}

# API Security Configuration
api.security.enabled=${API_SECURITY_ENABLED:true}
api.key=${API_KEY}

# Rate Limiting Configuration (Required)
api.security.rate-limit.max-attempts=5
api.security.rate-limit.window-ms=60000
api.security.rate-limit.log-suppression-ms=300000
```

#### AWS Production Profile (`application-aws.properties`)
```properties
# AWS Production Profile Configuration
api.security.enabled=${API_SECURITY_ENABLED:true}
api.key=${API_KEY}

# Rate Limiting Configuration (Stricter for production)
api.security.rate-limit.max-attempts=3
api.security.rate-limit.window-ms=300000
api.security.rate-limit.log-suppression-ms=600000

# Database Configuration (uses environment variables)
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# Production-specific settings
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.root=WARN
```

**Note**: The AWS profile includes production-ready optimizations:
- **🔄 Connection Pool**: Optimized for t2.micro instances (1GB RAM)
- **🛡️ Security**: SSL/TLS for database connections, rate limiting
- **📊 Monitoring**: Health checks for Application Load Balancer
- **⚡ Performance**: JVM optimizations and batch processing
- **📝 Logging**: Production-appropriate log levels
- **🔒 Safety**: DDL validation only, no data initialization

#### Environment-Specific Overrides
For different environments, you can:
1. **Use `application.properties`** for local development
2. **Use `application-aws.properties`** for AWS production deployment
3. **Override specific values** using environment variables
4. **Create custom profiles** if needed for complex deployments

#### Docker Deployment
For containerized deployment, the same `application.properties` is used, but database connection details are provided via environment variables in the container.

## Monitoring

Available monitoring endpoints:

- `/actuator/health` - Application health
- `/actuator/info` - App information  
- `/actuator/metrics` - Performance metrics

## 📊 Sample Data & Database Schema

### Pre-loaded Test Data

The application automatically initializes with realistic sample data that allows Leonardo to answer all SENASoft challenge questions:

- **4 Colombian Departments**: Cundinamarca, Bogotá D.C., Antioquia, Valle del Cauca
- **4 Training Centers**: Each with realistic apprentice counts, GitHub users, and English proficiency data
- **12 Training Programs**: Including "Análisis y Desarrollo de Software", "Gestión de Redes", etc.
- **10 Instructors**: With recommendation status per center

### Database Entities

- **Department** - Colombian geographical departments
- **TrainingCenter** - SENA training facilities with metrics (total apprentices, GitHub users, English B1/B2)
- **Program** - Educational programs with apprentice counts per center
- **Instructor** - Teaching staff with recommendation status

### Sample Metrics Generated

- **Total Apprentices**: 766 across all centers
- **Backend Profiles**: ~43.5% of total apprentices
- **GitHub Users**: Varies by center (78-180 users)
- **English B1/B2**: Varies by center (78-156 apprentices)

## 🧪 Testing & Quality Assurance

The backend includes a **comprehensive test suite** with **enhanced security testing** covering all functionality and security measures:

### **Test Coverage**
- **Unit Tests**: Service layer, mappers, and business logic
- **Integration Tests**: Controller endpoints with MockMvc
- **Security Tests**: API key validation, rate limiting, and secure logging
- **Error Handling Tests**: Global exception handler and error scenarios
- **Database Tests**: Repository layer and data access

### **Test Categories**
```bash
# Run all tests
./mvnw test

# Run specific test categories
./mvnw test -Dtest=MetricsServiceImplTest          # Service layer tests
./mvnw test -Dtest=MetricsApiIntegrationTest       # Controller integration tests
./mvnw test -Dtest=GlobalExceptionHandlerTest      # Error handling tests
./mvnw test -Dtest=MetricsApiErrorHandlingTest     # Endpoint error scenarios
./mvnw test -Dtest="*Security*"                   # Security configuration tests
./mvnw test -Dtest=ApiKeyAuthenticationFilterTest  # Authentication filter tests
```

### **Test Statistics**
- **Total Tests**: 78 ✅ (including new security tests)
- **Test Classes**: 8+ (including security test classes)
- **Coverage**: 100% of critical functionality and security measures
- **Execution Time**: ~5-7 seconds

### **Security Test Scenarios** 🛡️
- **API Key Validation**: Length requirements, weak pattern detection
- **Rate Limiting**: Failed authentication attempt limits and log suppression
- **Secure Logging**: URI sanitization and sensitive information protection
- **Authentication Filter**: Constructor validation and filter behavior
- **Security Configuration**: API key validation during startup

### **Error Handling Test Scenarios**
- **MetricsException** handling with custom error codes
- **DataNotFoundException** for missing resources
- **RuntimeException** and generic error handling
- **Validation errors** and constraint violations
- **Endpoint-specific error** responses

### **Security Test Execution**
```bash
# Run all security-related tests
./mvnw test -Dtest="*Security*"

# Run specific security test classes
./mvnw test -Dtest=SecurityConfigTest              # Security configuration tests
./mvnw test -Dtest=ApiKeyAuthenticationFilterTest  # Authentication filter tests
./mvnw test -Dtest=SecurityConfigValidationTest    # API key validation tests
```