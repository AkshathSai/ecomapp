#!/bin/bash

# E-commerce Application - Local Development Setup
echo "🚀 Starting E-commerce Application for Local Development..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Stop any existing containers
echo "🛑 Stopping existing containers..."
docker-compose down --remove-orphans

# Pull latest images
echo "📦 Pulling latest images..."
docker-compose pull

# Build and start services
echo "🏗️ Building and starting services..."
docker-compose up --build -d

# Wait for services to be ready
echo "⏳ Waiting for services to start..."
sleep 45

# Check service health
echo "🔍 Checking service health..."
docker-compose ps

# Test application endpoints
echo "🧪 Testing application endpoints..."
sleep 10

# Health check
if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ Application health check passed"
else
    echo "❌ Application health check failed"
fi

echo ""
echo "🎉 Setup complete!"
echo "📱 Application URL: http://localhost:8080"
echo "📊 API Documentation: http://localhost:8080/swagger-ui.html"
echo "🔍 Health Check: http://localhost:8080/actuator/health"
echo "🗄️ MongoDB: localhost:27017"
echo "📡 Kafka: localhost:9092"
echo ""
echo "🔧 Useful commands:"
echo "  docker-compose logs -f ecomapp    # View app logs"
echo "  docker-compose restart ecomapp   # Restart app"
echo "  docker-compose down              # Stop all services"
