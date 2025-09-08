# Ecommerce Application

A complete ecommerce application built with **Java Spring Boot**, **MongoDB**, and **Apache Kafka** for the hackathon requirements.

## Features

✅ **Place an order** - Complete order management system  
✅ **Add items to cart** - Cart management with real-time updates  
✅ **Order and add discount code** - Promotion code validation system  
✅ **Carbon calculation in Order service** - Environmental impact tracking  
✅ **Kafka-based asynchronous messaging** - For cart item additions  

## Tech Stack

- **Backend**: Java Spring Boot 3.x
- **Database**: MongoDB
- **Messaging**: Apache Kafka
- **Build Tool**: Maven

## Prerequisites

- Java 17 or higher
- MongoDB running on localhost:27017
- Apache Kafka running on localhost:9092
- Maven 3.6+

## Quick Start

1. **Clone and navigate to the project**
   ```bash
   cd ecomapp
   ```

2. **Install dependencies**
   ```bash
   ./mvnw clean install
   ```

3. **Start MongoDB and Kafka**
   ```bash
   # Start MongoDB
   mongod
   
   # Start Kafka (in separate terminals)
   bin/zookeeper-server-start.sh config/zookeeper.properties
   bin/kafka-server-start.sh config/server.properties
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/search?name={name}` - Search products
- `GET /api/products/available` - Get available products
- `POST /api/products` - Create new product

### Cart (Asynchronous with Kafka)
- `POST /api/cart/add?userId={userId}&productId={productId}&quantity={quantity}` - Add item to cart
- `GET /api/cart/{userId}` - Get user's cart
- `DELETE /api/cart/{userId}` - Clear cart

### Orders
- `POST /api/orders/place?userId={userId}&promotionCode={code}` - Place order with optional promotion
- `GET /api/orders/user/{userId}` - Get user's orders
- `GET /api/orders/{orderId}` - Get specific order
- `PUT /api/orders/{orderId}/status?status={status}` - Update order status

### Promotions
- `POST /api/promotions/validate?code={code}&orderAmount={amount}` - Validate promotion code
- `POST /api/promotions/calculate-discount?code={code}&orderAmount={amount}` - Calculate discount
- `POST /api/promotions` - Create new promotion

## Sample Data

The application automatically initializes with sample data:

### Products
- Gaming Laptop ($1200, Carbon: 45.5kg)
- Smartphone ($800, Carbon: 12.3kg)
- Wireless Headphones ($200, Carbon: 5.2kg)
- Tablet ($400, Carbon: 18.7kg)
- Smart Watch ($300, Carbon: 8.9kg)

### Promotion Codes
- `SAVE10` - 10% off orders over $100
- `SAVE50` - $50 off orders over $500
- `WELCOME20` - 20% off for new customers (min $50)

## Usage Examples

### 1. Add Items to Cart (Asynchronous)
```bash
curl -X POST "http://localhost:8080/api/cart/add?userId=user1&productId=PRODUCT_ID&quantity=2"
```

### 2. Place Order with Promotion
```bash
curl -X POST "http://localhost:8080/api/orders/place?userId=user1&promotionCode=SAVE10"
```

### 3. Get Order with Carbon Footprint
```bash
curl -X GET "http://localhost:8080/api/orders/ORDER_ID"
```

## Carbon Footprint Calculation

The system calculates carbon footprint using the formula:
```
Total Carbon Footprint = Σ(product.carbonFootprint × quantity)
```

Each order includes:
- Individual item carbon footprints
- Total order carbon footprint
- Environmental impact awareness

## Kafka Integration

Cart operations use Kafka for asynchronous processing:
- **Topic**: `cart-items`
- **Events**: ADD, REMOVE, UPDATE
- **Consumer Group**: `ecom-group`

This ensures scalable cart management and real-time updates.

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   REST APIs     │    │   Kafka Queue   │    │    MongoDB      │
│                 │    │                 │    │                 │
│ - Products      │◄──►│ - cart-items    │◄──►│ - products      │
│ - Cart          │    │ - async proc.   │    │ - carts         │
│ - Orders        │    │                 │    │ - orders        │
│ - Promotions    │    │                 │    │ - promotions    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## Environment Configuration

Key configuration in `application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/ecomapp
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=ecom-group
```

## Testing

Run tests with:
```bash
./mvnw test
```

## Contributing

1. Fork the repository
2. Create feature branch
3. Submit pull request

## License

MIT License - Built for Hackathon
