# Entitlement Service

High-performance data access service for the CCAAS Platform 3-layer entitlement architecture.

## Overview

The Entitlement Service provides optimized database operations for the 3-layer entitlement system:

- **Layer 1**: Role Layout Templates - Base UI layouts per role
- **Layer 2**: AD Group Granular Overrides - Market-specific customizations based on AD groups
- **Layer 3**: User Preference Cache - Pre-computed final UI configurations for instant loading

## Technology Stack

- **Java 21** with Virtual Threads
- **Spring Boot 3.x** with Jakarta EE
- **PostgreSQL** with JSONB support
- **Caffeine** in-memory caching
- **HikariCP** connection pooling
- **Maven** build tool

## Features

### High Performance
- User layout retrieval < 5ms (cache hit)
- AD group override lookup < 10ms for 10 groups
- Bulk cache warming < 100ms per user
- Optimized connection pooling

### Caching Strategy
- 4-hour cache expiry for user preferences
- 1-hour cache expiry for role templates
- 30-minute cache expiry for AD group overrides
- Automatic cache cleanup every hour

### Audit Trail
- Complete audit trail for all layout computations
- Performance metrics and monitoring
- Cache hit ratio tracking
- Slow query identification

## API Endpoints

### User Preferences (Layer 3)
- `GET /api/v1/data/user-preferences/{userId}` - Get cached user layout
- `POST /api/v1/data/user-preferences` - Create/update user preferences
- `DELETE /api/v1/data/user-preferences/{userId}` - Delete user preferences
- `DELETE /api/v1/data/user-preferences/expired` - Cleanup expired cache
- `GET /api/v1/data/user-preferences/stats` - Cache statistics

### Role Templates (Layer 1)
- `GET /api/v1/data/role-templates` - Get all active role templates
- `GET /api/v1/data/role-templates/{roleId}` - Get specific role template
- `POST /api/v1/data/role-templates` - Create new role template
- `PUT /api/v1/data/role-templates/{roleId}` - Update role template
- `DELETE /api/v1/data/role-templates/{roleId}` - Delete role template
- `GET /api/v1/data/role-templates/market/{market}` - Get templates by market

### AD Group Overrides (Layer 2)
- `GET /api/v1/data/ad-group-overrides` - Get all active overrides
- `GET /api/v1/data/ad-group-overrides/{groupHash}` - Get specific override
- `POST /api/v1/data/ad-group-overrides` - Create new override
- `PUT /api/v1/data/ad-group-overrides/{groupHash}` - Update override
- `DELETE /api/v1/data/ad-group-overrides/{groupHash}` - Delete override
- `POST /api/v1/data/ad-group-overrides/bulk-lookup` - Bulk lookup by DNs

### Audit Operations
- `GET /api/v1/data/audit/computation/{userId}` - Get user audit trail
- `GET /api/v1/data/audit/computation/performance-stats` - Performance statistics
- `GET /api/v1/data/audit/computation/cache-hit-ratio` - Cache hit ratio
- `GET /api/v1/data/audit/computation/slowest` - Slowest computations

## Configuration

### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ccaas_platform
    username: ccaas_user
    password: ccaas_password
    hikari:
      minimum-idle: 10
      maximum-pool-size: 50
      connection-timeout: 2000
```

### Cache Configuration
```yaml
entitlement:
  cache:
    user-preferences-ttl-hours: 4
    role-templates-ttl-hours: 1
    ad-group-overrides-ttl-minutes: 30
```

## Running the Service

### Prerequisites
- Java 21
- PostgreSQL 14+ with JSONB support
- Maven 3.8+

### Development
```bash
# Start PostgreSQL (using Docker)
docker run -d --name ccaas-postgres -p 5434:5432 \
  -e POSTGRES_DB=ccaas_platform \
  -e POSTGRES_USER=ccaas_user \
  -e POSTGRES_PASSWORD=ccaas_password \
  postgres:14

# Run the service
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production
```bash
# Build the application
mvn clean package

# Run with production profile
java -jar target/entitlement-service-1.0.0.jar --spring.profiles.active=prod
```

## Performance Monitoring

The service includes comprehensive performance monitoring:

- **Actuator endpoints**: `/actuator/health`, `/actuator/metrics`, `/actuator/cache`
- **Cache statistics**: Hit ratios, entry counts, expiry rates
- **Database metrics**: Connection pool status, query performance
- **Audit trail**: Complete computation history with timing

## Dependencies

- Database Schema (IND-205) must be deployed
- PostgreSQL with JSONB support
- Java 21 runtime environment

## API Documentation

Once running, the service provides:
- Health check: `GET /entitlement/actuator/health`
- Metrics: `GET /entitlement/actuator/metrics`
- Cache info: `GET /entitlement/actuator/cache`

## License

Part of the CCAAS Platform - Internal Use Only
