# E-commerce Application

A Spring Boot-based e-commerce application with MongoDB and Kafka integration, optimized for resource-constrained environments like Killercoda.

## 🚀 Quick Start

### Local Development
```bash
# Start all services with Docker Compose
./start-dev.sh
```

### Killercoda/Resource-Constrained Environment
```bash
# Use the optimized configuration
docker-compose -f docker-compose.yml -f docker-compose.killercoda.yml up -d
```

## 📋 Prerequisites

- Docker & Docker Compose
- Java 21+ (for local development)
- Maven 3.6+ (for local development)

## 🏗️ Architecture

The application consists of:
- **Spring Boot Application** (Port 8080)
- **MongoDB** (Port 27017) - Database
- **Apache Kafka** (Port 9092) - Message broker
- **Zookeeper** (Port 2181) - Kafka coordination

## 🔧 Configuration

### Resource Limits (Killercoda-optimized)
- **Total Memory**: ~896MB
- **Total CPU**: ~1.5 cores
- **Storage**: ~2GB

### Environment Variables
- `SPRING_DATA_MONGODB_URI` - MongoDB connection string
- `SPRING_KAFKA_BOOTSTRAP_SERVERS` - Kafka server addresses
- `SPRING_PROFILES_ACTIVE` - Active Spring profile
- `JAVA_OPTS` - JVM optimization options

## 🛠️ Development

### Building the Application
```bash
mvn clean package -DskipTests
```

### Running Tests
```bash
mvn test
```

### Local Development (without Docker)
```bash
# Start MongoDB and Kafka locally
# Then run:
mvn spring-boot:run
```

## 🐳 Docker Deployment

### Standard Deployment
```bash
docker-compose up -d
```

### Resource-Constrained Deployment
```bash
docker-compose -f docker-compose.yml -f docker-compose.killercoda.yml up -d
```

### Building Custom Image
```bash
docker build -t ecomapp:latest .
```

## 📊 Monitoring & Health Checks

### Health Check Endpoint
```bash
curl http://localhost:8080/actuator/health
```

### API Documentation
Visit: http://localhost:8080/swagger-ui.html

### Service Status
```bash
docker-compose ps
```

### View Logs
```bash
# All services
docker-compose logs

# Specific service
docker-compose logs ecomapp
docker-compose logs mongodb
docker-compose logs kafka
```

## 🔄 CI/CD Workflows

The project includes GitHub Actions workflows for:

### 1. PR Build (`pr-build.yml`)
- Runs on pull requests
- Executes tests and builds
- Performs security scans
- Tests Docker image build

### 2. CI/CD Pipeline (`ci-cd.yml`)
- Runs on push to main/develop
- Builds and tests application
- Builds and pushes Docker images
- Deploys to staging/production

### 3. Release Workflow (`release.yml`)
- Creates releases with version tags
- Builds release artifacts
- Publishes Docker images

### 4. Killercoda Deployment (`killercoda-deploy.yml`)
- Creates optimized deployment packages
- Resource-constrained configurations
- Automated deployment scripts

## 🎯 API Endpoints

### Core Endpoints
- `GET /actuator/health` - Health check
- `GET /api/products` - List products
- `POST /api/products` - Create product
- `GET /api/cart` - Get cart
- `POST /api/cart/items` - Add to cart
- `GET /api/orders` - List orders
- `POST /api/orders` - Create order

## 🚨 Troubleshooting

### Common Issues

#### Application Won't Start
```bash
# Check logs
docker-compose logs ecomapp

# Restart services
docker-compose restart

# Clean restart
docker-compose down && docker-compose up -d
```

#### Database Connection Issues
```bash
# Check MongoDB status
docker-compose logs mongodb

# Verify connection
docker-compose exec mongodb mongo --eval "db.adminCommand('ismaster')"
```

#### Kafka Issues
```bash
# Check Kafka logs
docker-compose logs kafka

# List topics
docker-compose exec kafka kafka-topics --bootstrap-server localhost:9092 --list
```

#### Memory Issues (Killercoda)
```bash
# Check resource usage
docker stats

# Use memory-optimized configuration
docker-compose -f docker-compose.yml -f docker-compose.killercoda.yml up -d
```

## 🛡️ Security

- Non-root user in Docker container
- Resource limits configured
- Health checks implemented
- Security scanning in CI/CD

## 📈 Performance Optimization

### For Resource-Constrained Environments
- JVM heap size limited to 256MB
- Kafka heap size limited to 128MB
- MongoDB memory limits applied
- Multi-stage Docker build for smaller images

### Monitoring Resource Usage
```bash
# Real-time resource usage
docker stats

# Container resource limits
docker-compose config
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes with tests
4. Submit a pull request

The CI/CD pipeline will automatically:
- Run tests
- Perform security scans
- Build Docker images
- Validate the changes

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For issues and questions:
1. Check the troubleshooting section
2. Review the logs: `docker-compose logs`
3. Create an issue in the repository
