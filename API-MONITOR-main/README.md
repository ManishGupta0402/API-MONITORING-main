# API Monitoring Application

A comprehensive Spring Boot application for monitoring API endpoints with real-time health checks, advanced metrics collection, admin management interface, and security features.

## 🚀 Features

### Core Monitoring
- **Real-time API Monitoring**: Continuous health checks for configured endpoints
- **Enhanced Response Time Metrics**: Latest, average, min/max response times with trend analysis
- **Beautiful Dashboard**: Web-based dashboard with real-time updates and comprehensive metrics
- **SSL Certificate Handling**: Configurable SSL verification (disable for local testing with self-signed certificates)
- **Scheduled Health Checks**: Automatic monitoring with configurable intervals
- **Database Storage**: Persistent storage with H2 database (easily configurable for other databases)

### 🛠️ Admin Panel
- **Complete CRUD Operations**: Add, edit, delete API endpoints through intuitive web interface
- **Modal-based Forms**: User-friendly forms with validation
- **Bulk Operations**: Run health checks for all endpoints at once
- **Real-time Management**: Instant updates and feedback

### 📊 Advanced Dashboard
- **Comprehensive Metrics Display**: 
  - Latest response time with trend indicators (↗ ↘ ≈)
  - Average response time across all checks
  - Min-max response time ranges
  - Success rate and uptime percentages
- **Visual Trend Analysis**: Performance trend indicators based on recent checks
- **Responsive Design**: Optimized for desktop, tablet, and mobile
- **Auto-refresh**: Real-time updates every 30 seconds

### 🔒 Security Features
- **URL Masking**: Sensitive paths hidden in dashboard (shows only domain + `/***🔒`)
- **Admin-only Full URLs**: Complete URLs visible only in admin panel for management
- **Secure Display**: Protects API keys, tokens, and sensitive paths from exposure

### 🔧 API & Integration
- **RESTful API**: Complete REST API for managing endpoints and viewing statistics
- **Spring Boot Actuator**: Built-in health checks and metrics endpoints
- **Prometheus Integration**: Metrics export for monitoring tools
- **Environment Profiles**: Separate configurations for development and production

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Boot Actuator**
- **Spring WebFlux** (for non-blocking HTTP calls with SSL configuration)
- **H2 Database** (in-memory for demo, easily configurable)
- **Thymeleaf** (for dashboard and admin templates)
- **Micrometer** (for metrics)
- **Netty** (for advanced HTTP client with SSL handling)
- **Maven** (build tool)

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

#### Development Mode (SSL verification disabled)
```bash
# Option 1: Using development profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Option 2: Setting property directly
mvn spring-boot:run -Dspring-boot.run.arguments=--monitoring.ssl.ignore-certificate-errors=true

# Option 3: Using environment variable
export MONITORING_SSL_IGNORE_CERTIFICATE_ERRORS=true
mvn spring-boot:run
```

#### Production Mode (SSL verification enabled)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

#### Default Mode
```bash
mvn spring-boot:run
```

### 🌐 Access Points

- **Main Dashboard**: http://localhost:8080/dashboard
- **Admin Panel**: http://localhost:8080/admin (opens from dashboard)
- **REST API**: http://localhost:8080/api/
- **Health Check**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics
- **Prometheus**: http://localhost:8080/actuator/prometheus
- **H2 Console**: http://localhost:8080/h2-console

### 📊 Sample Data

The application automatically creates sample endpoints for testing:
- JSONPlaceholder Posts API
- JSONPlaceholder Users API
- HTTPBin Status API
- Google Health Check

## 📡 API Endpoints

### Endpoint Management

- `GET /api/endpoints` - Get all endpoints
- `GET /api/endpoints/active` - Get active endpoints only
- `GET /api/endpoints/{id}` - Get specific endpoint
- `POST /api/endpoints` - Create new endpoint
- `PUT /api/endpoints/{id}` - Update endpoint
- `DELETE /api/endpoints/{id}` - Delete endpoint
- `POST /api/endpoints/{id}/toggle` - Toggle endpoint active status
- `GET /api/endpoints/count/active` - Get count of active endpoints

### Monitoring & Statistics

- `GET /api/monitoring/stats` - Get all monitoring statistics
- `GET /api/monitoring/stats/active` - Get active endpoint statistics
- `GET /api/monitoring/stats/{endpointId}` - Get specific endpoint statistics
- `GET /api/monitoring/health-checks/{endpointId}` - Get health check history
- `GET /api/monitoring/health-checks/{endpointId}/since` - Get health checks since date
- `POST /api/monitoring/check/{endpointId}` - Trigger immediate health check
- `POST /api/monitoring/check/all` - Trigger all health checks

### Creating a New Endpoint

```bash
curl -X POST http://localhost:8080/api/endpoints \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My API",
    "url": "https://api.example.com/health",
    "httpMethod": "GET",
    "expectedStatus": 200,
    "timeoutMs": 5000,
    "checkIntervalMs": 30000,
    "isActive": true
  }'
```

## 🎨 Dashboard Features

### Main Dashboard
- **Real-time Statistics**: Total endpoints, active endpoints, healthy endpoints, average response time
- **Enhanced Endpoint Cards**: 
  - Latest response time with trend arrows (↗ getting slower, ↘ getting faster, ≈ stable)
  - Average response time across all checks
  - Min-max response time ranges
  - Visual status indicators (healthy/unhealthy/unknown)
- **Security Features**: URLs masked after domain for security (`https://api.example.com/***🔒`)
- **Manual Actions**: Refresh data and trigger health checks manually
- **Admin Access**: One-click access to admin panel
- **Auto-refresh**: Automatic updates every 30 seconds
- **Responsive Design**: Works on desktop, tablet, and mobile devices

### 🛠️ Admin Panel
- **Comprehensive Management Interface**:
  - Tabular view of all endpoints with full configuration details
  - Add new endpoints with form validation
  - Edit existing endpoints with pre-populated forms
  - Delete endpoints with confirmation dialogs
  - Bulk health check execution
- **Security Notice**: Full URLs displayed with security warning
- **Real-time Feedback**: Success/error messages for all operations
- **Modal Forms**: Professional interface with proper validation

## ⚙️ Configuration

### Application Properties

#### Main Configuration (`application.yml`)
```yaml
monitoring:
  check-interval: 30000  # Health check interval in milliseconds
  timeout: 5000          # Request timeout in milliseconds
  retry-attempts: 3      # Number of retry attempts
  ssl:
    ignore-certificate-errors: false  # Set to true for local testing

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,httptrace
      base-path: /actuator

logging:
  level:
    com.example.apimonitoring: INFO
    org.springframework.web: DEBUG
```

#### Development Profile (`application-dev.yml`)
```yaml
# Optimized for local development
monitoring:
  ssl:
    ignore-certificate-errors: true  # SSL verification disabled
  check-interval: 10000  # Faster checks (10 seconds)

logging:
  level:
    com.example.apimonitoring: DEBUG
    org.springframework.web.reactive.function.client: DEBUG
    reactor.netty.http.client: DEBUG
```

#### Production Profile (`application-prod.yml`)
```yaml
# Optimized for production
monitoring:
  ssl:
    ignore-certificate-errors: false  # SSL verification enabled
  check-interval: 60000  # Less frequent checks (60 seconds)

logging:
  level:
    com.example.apimonitoring: INFO
    org.springframework.web: INFO
    root: WARN
```

### Environment-Specific Startup

```bash
# Development (SSL disabled, debug logging)
java -jar target/api-monitoring-1.0.0.jar --spring.profiles.active=dev

# Production (SSL enabled, optimized logging)
java -jar target/api-monitoring-1.0.0.jar --spring.profiles.active=prod

# Custom SSL setting
java -jar target/api-monitoring-1.0.0.jar --monitoring.ssl.ignore-certificate-errors=true
```

### Database Configuration

The application uses H2 in-memory database by default. To use a different database:

1. Add the database dependency to `pom.xml`
2. Update `application.yml` with database connection details:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/monitoring
    username: your-username
    password: your-password
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

## 📊 Monitoring & Metrics

### Enhanced Response Time Tracking

The application now tracks comprehensive response time metrics:

- **Latest Response Time**: Most recent check with trend analysis
- **Average Response Time**: Historical average of successful checks
- **Min/Max Response Times**: Performance range analysis
- **Trend Indicators**: 
  - ↗ (red): Performance degrading (getting slower)
  - ↘ (green): Performance improving (getting faster)
  - ≈ (orange): Stable performance (< 10% change)

### Spring Boot Actuator Endpoints

- `/actuator/health` - Application health status
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus format metrics
- `/actuator/info` - Application information
- `/actuator/httptrace` - HTTP request trace information

### Custom Health Indicator

The application includes a custom health indicator that reports:
- Service status
- Number of active endpoints
- Timestamp of last check
- SSL configuration status

### Prometheus Integration

Metrics are automatically exported in Prometheus format and include:
- HTTP request metrics
- JVM metrics
- Custom application metrics
- Response time histograms
- Success rate counters

## 🔒 Security Features

### URL Masking
- **Dashboard Security**: Sensitive URL paths are hidden after the domain
  - `https://api.bank.com/accounts/12345/transactions?token=abc123` → `https://api.bank.com/***🔒`
- **Admin Panel Access**: Full URLs visible for management purposes with security warnings
- **Supported Domains**: Handles 20+ TLDs including `.com`, `.org`, `.net`, `.edu`, `.gov`, `.io`, `.ai`, etc.
- **Development Support**: Also masks localhost and IP address paths

### SSL Configuration
- **Flexible SSL Handling**: Can disable SSL verification for local testing
- **Environment-Specific**: Different SSL settings for dev/prod environments
- **Security Warnings**: Clear logging when SSL verification is disabled
- **Production Safety**: SSL verification enabled by default

## 🏗️ Architecture

### Project Structure

```
src/main/java/com/example/apimonitoring/
├── controller/          # REST controllers and web endpoints
│   ├── ApiEndpointController.java     # CRUD operations for endpoints
│   ├── MonitoringController.java      # Health check and stats APIs
│   └── DashboardController.java       # Web dashboard and admin routes
├── service/            # Business logic services
│   ├── ApiEndpointService.java       # Endpoint management
│   ├── HealthCheckService.java       # HTTP health checking
│   └── MonitoringService.java        # Monitoring orchestration
├── model/              # JPA entities
│   ├── ApiEndpoint.java              # Endpoint configuration
│   └── HealthCheck.java              # Health check results
├── repository/         # Data access layer
│   ├── ApiEndpointRepository.java    # Endpoint data access
│   └── HealthCheckRepository.java    # Health check data access
├── dto/                # Data transfer objects
│   ├── ApiEndpointRequest.java       # Endpoint creation/update
│   └── MonitoringStats.java          # Statistics response
├── config/             # Configuration classes
│   ├── WebClientConfig.java          # HTTP client with SSL config
│   └── DataInitializer.java          # Sample data setup
└── ApiMonitoringApplication.java

src/main/resources/
├── templates/          # Thymeleaf templates
│   ├── dashboard.html              # Main monitoring dashboard
│   └── admin.html                  # Admin management interface
├── application.yml     # Main configuration
├── application-dev.yml # Development profile
└── application-prod.yml # Production profile
```

### Key Components

- **ApiEndpointService**: Manages API endpoint CRUD operations with validation
- **HealthCheckService**: Performs HTTP health checks with configurable SSL handling
- **MonitoringService**: Orchestrates monitoring, scheduling, and statistics calculation
- **WebClientConfig**: Configures HTTP client with SSL certificate handling
- **Enhanced Statistics**: Comprehensive response time tracking and trend analysis

## 🔧 Development

### IDE Setup (Cursor/VS Code)

The project includes launch configurations for easy development:

```json
// .vscode/launch.json
{
  "configurations": [
    {
      "name": "Run API Monitor (Dev Profile)",
      "type": "java",
      "request": "launch",
      "mainClass": "com.example.apimonitoring.ApiMonitoringApplication",
      "args": "--spring.profiles.active=dev"
    }
  ]
}
```

### Running Tests

```bash
mvn test
```

### Building for Production

```bash
# Clean build
mvn clean package

# Run with production profile
java -jar target/api-monitoring-1.0.0.jar --spring.profiles.active=prod
```

### Docker Support

Create a `Dockerfile`:

```dockerfile
FROM openjdk:17-jre-slim

# Copy the jar file
COPY target/api-monitoring-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

Build and run:

```bash
# Build the application
mvn clean package

# Build Docker image
docker build -t api-monitoring .

# Run with development profile (SSL disabled)
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev api-monitoring

# Run with production profile (SSL enabled)
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod api-monitoring
```

### Development Tips

1. **Use Development Profile**: Automatically disables SSL verification and enables debug logging
2. **Admin Panel**: Use for easy endpoint management during development
3. **URL Masking**: Test with various URL formats to ensure proper masking
4. **Response Time Metrics**: Add endpoints with different response characteristics to see trend analysis

## 🚨 Troubleshooting

### Common Issues

#### SSL Certificate Errors
```
javax.net.ssl.SSLHandshakeException: PKIX path building failed
```
**Solution**: Use development profile or disable SSL verification:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Jackson Circular Reference
If you see infinite serialization loops:
- Ensure `@JsonManagedReference` and `@JsonBackReference` annotations are properly set
- The project already includes these fixes

#### Admin Panel Not Opening
- Ensure popup blockers are disabled
- Check browser console for JavaScript errors
- Verify `/admin` endpoint is accessible

### Debug Mode

Enable comprehensive debugging:

```yaml
logging:
  level:
    com.example.apimonitoring: DEBUG
    org.springframework.web.reactive.function.client: DEBUG
    reactor.netty.http.client: DEBUG
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Add tests for new functionality
5. Update documentation (README, JavaDoc)
6. Submit a pull request

### Development Standards

- Use meaningful commit messages
- Follow Java naming conventions
- Add proper error handling
- Include unit tests for new features
- Update README for new functionality

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For questions or issues:

1. Check the troubleshooting section above
2. Review existing issues in the repository
3. Create a new issue with:
   - Environment details (Java version, OS)
   - Steps to reproduce
   - Expected vs actual behavior
   - Relevant logs or error messages

## 🔄 Version History

### v1.2.0 (Latest)
- ➕ Admin panel with full CRUD operations
- 🔒 URL masking for security
- 📊 Enhanced response time metrics with trend analysis
- ⚙️ Environment-specific configurations (dev/prod profiles)
- 🛡️ Configurable SSL certificate validation
- 🎨 Improved dashboard UI with comprehensive metrics
- 📱 Mobile-responsive design improvements

### v1.1.0
- 📊 Basic response time tracking
- 🌐 RESTful API endpoints
- 📋 Simple dashboard interface

### v1.0.0
- 🎯 Initial release
- ✅ Basic health check functionality
- 💾 Database storage with H2