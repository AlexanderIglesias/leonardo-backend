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

- **🏗️ Clean Architecture** with proper separation of concerns
- **🔒 Type-Safe Database Projections** to avoid runtime errors
- **📝 API Versioning** for future compatibility (`/api/v1/`)
- **📚 Interactive Documentation** with Swagger UI
- **🐳 Full Docker Support** with multi-stage builds and complete containerization
- **⚡ Performance Optimized** with optimized queries and connection pooling
- **🤖 Leonardo Integration** via included OpenAI Action schema
- **📊 Sample Data** preloaded for immediate testing

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.5.5 with Java 17
- **Database**: MySQL 8.0
- **Documentation**: OpenAPI 3.1 / Swagger UI
- **Build**: Maven
- **Containers**: Docker & Docker Compose for full-stack deployment
- **Monitoring**: Spring Boot Actuator
- **ORM**: Spring Data JPA with Hibernate
- **Mapping**: Project Lombok to reduce boilerplate

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

### Example Response

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

## Project Structure

```
src/main/java/com/alphanet/products/leonardobackend/
├── config/              # App configuration & data initialization
├── controller/          # REST endpoints
├── dto/                 # Data transfer objects
│   └── projection/      # Database projections
├── entity/              # Database entities (Department, TrainingCenter, Program, Instructor)
├── repository/          # Data access with custom queries
├── service/             # Business logic
│   ├── impl/           # Service implementations
│   └── mapper/         # DTO mapping utilities
└── openai.action.schema.json  # Leonardo/OpenAI integration schema
```

## 🤖 Leonardo Integration

This backend includes a complete OpenAI Action schema (`openai.action.schema.json`) that allows Leonardo to consume the API endpoints. The schema defines:

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

## Development

### Build Commands

```bash
# Compile
./mvnw clean compile

# Run tests
./mvnw test

# Package
./mvnw clean package
```

### Docker Architecture

The application uses a multi-container setup with Docker Compose:

#### Services
- **leonardo-app**: Spring Boot application container (built from Dockerfile)
- **mysql**: MySQL 8.0 database container

#### Network & Health Checks
- Custom bridge network: `leonardo-network`
- Health checks for both services with proper dependency management
- MySQL health check ensures database is ready before starting the app

#### Database Configuration
- Database: `leonardo_senasoft`
- User: `leonardo_user`
- Exposed port: `3306` (MySQL), `8080` (Application)
- Persistent volume: `mysql_data`

### Configuration

The application supports multiple profiles:

#### Development Profile (`application.properties`)
```properties
# Local development with Docker database
spring.datasource.url=jdbc:mysql://localhost:3306/leonardo_senasoft
spring.datasource.username=leonardo_user
spring.profiles.active=dev
```

#### Docker Profile (`application-docker.properties`)
```properties
# Containerized deployment
# Database URL provided via environment variables
# Optimized connection pool settings for containers
spring.profiles.active=docker
```

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

## Testing

Run the test suite:

```bash
./mvnw test
```

This includes unit tests and integration tests with the database.

## 🚀 Deployment Options

### Option 1: Full Docker Stack (Recommended)
```bash
cd src/main/docker
docker-compose up -d
```
This starts both MySQL and the Spring Boot application in containers.

### Option 2: Local Development
```bash
# Start only database
cd src/main/docker && docker-compose up -d mysql
# Run app locally
./mvnw spring-boot:run
```

### Option 3: Production JAR
```bash
# Build optimized JAR
./mvnw clean package
# Run with external database
java -jar target/leonardo-backend-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://your-mysql-host:3306/leonardo_senasoft \
  --spring.datasource.username=your-user \
  --spring.datasource.password=your-password
```

### Docker Commands
```bash
# Build only the application image
docker build -t leonardo-backend .

# View logs
docker-compose logs -f leonardo-app

# Stop services
docker-compose down

# Stop and remove volumes (⚠️ deletes data)
docker-compose down -v
```

## 📦 SENASoft Challenge Deliverables

This project provides all required deliverables for the SENASoft challenge:

✅ **Data Structures**: Complete MySQL database schema with entities for departments, training centers, programs, and instructors

✅ **Sample Data**: Realistic test data that allows Leonardo to answer all challenge questions without requiring real registration data

✅ **Backend Integration**: Fully functional Spring Boot API with all necessary endpoints to support the required queries

✅ **OpenAI Integration**: Complete `openai.action.schema.json` ready for Leonardo/ChatGPT integration

✅ **Docker Containerization**: Multi-stage Dockerfile and Docker Compose for complete deployment

✅ **Documentation**: This comprehensive README with setup and usage instructions

## 🚀 AWS Deployment (Free Tier)

This backend is ready for deployment on AWS using only **FREE TIER** services:

### Quick AWS Deployment

1. **Pre-deployment Check**
   ```bash
   ./scripts/pre-deployment-check.sh
   ```

2. **Create AWS Resources**
   - EC2 t2.micro instance (Amazon Linux 2023)
   - RDS MySQL db.t3.micro database
   - Configure Security Groups

3. **Deploy Application**
   ```bash
   # Update with your EC2 IP and SSH key
   ./scripts/deploy-to-aws.sh
   ```

📖 **Complete Guide**: See [AWS Deployment Guide](docs/AWS_DEPLOYMENT_GUIDE.md) for detailed instructions.

### 💰 AWS Free Tier Usage
- **EC2**: t2.micro (750 hours/month)
- **RDS**: db.t3.micro (750 hours/month)  
- **Storage**: 30 GB EBS + 20 GB RDS
- **Cost**: $0.00/month (within limits)

## 🚀 Testing Leonardo Integration

Once deployed and integrated with Leonardo, you can test with these Spanish questions:

- "¿Cuántos aprendices hay inscritos por centro de formación?"
- "¿Qué instructores son recomendados en cada centro?"
- "¿Cuántos aprendices hay por programa de formación?"
- "¿Cuál es la distribución de aprendices por departamento?"
- "¿Cuántos aprendices reportan tener GitHub?"
- "¿Cuántos aprendices tienen nivel de inglés B1 o B2?"

---

**Built for SENASoft 2025 Challenge** 🇨🇴  
*Extending Leonardo's capabilities with detailed SENA apprentice metrics*
