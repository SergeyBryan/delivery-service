# delivery-service

This project consists of two modules:

1. **API**: Contains the OpenAPI specifications and code generation for request bodies, response bodies, and APIs using Maven OpenAPI Generator.
2. **Service**: Implements business logic and utilizes the classes generated in the API module. It handles the management of products and employees in a store environment.

### Table of Contents

- [Introduction](#introduction)
- [Modules](#modules)
  - [API Module](#api-module)
  - [Service Module](#service-module)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Dependencies](#dependencies)
- [License](#license)

## Introduction

The Store Manager project provides a platform for managing products and employees for a store. The API module defines the OpenAPI specification for the system, while the Service module implements the actual business logic for managing the store's resources. 

### Key Features:
- Product and employee management
- Reactive web service using Spring WebFlux
- Integration with PostgreSQL and Elasticsearch for storage and search functionalities
- OpenAPI integration for generating API documentation and client-server communication
- Unit and integration tests with TestContainers

## Modules

### API Module

The **API** module is responsible for the OpenAPI specification, which includes the definitions of the API endpoints and the data models (request/response bodies). It uses the OpenAPI Generator plugin to generate code for these API definitions.

**Key Points**:
- The module generates API code using the OpenAPI specification stored in `aggregated-api.yaml`.
- It generates API interfaces and model classes, annotated with Lombok for easy object manipulation.
- The module excludes the default Netty starter in favor of a Tomcat server for HTTP support.

**Dependencies**:
- `spring-boot-starter-webflux` for reactive web support.
- `spring-boot-starter-security` for security features.
- `spring-boot-starter-test` for testing purposes.
- `swagger-annotations` for API documentation generation.

### Service Module

The **Service** module is where all the business logic resides. It uses the API module for the data models and interfaces. It integrates with PostgreSQL and Elasticsearch for data management and provides RESTful endpoints for interacting with the store's data.

**Key Points**:
- Reactive web service using Spring WebFlux.
- Postgres and Elasticsearch support for storing and searching product and employee data.
- The module uses `springdoc-openapi` for automatic OpenAPI documentation generation.

**Dependencies**:
- `spring-boot-starter-data-r2dbc` for reactive PostgreSQL support.
- `spring-boot-starter-data-elasticsearch` for Elasticsearch integration.
- `mapstruct` for object-to-object mapping between DTOs and entities.
- `testcontainers` for testing with isolated PostgreSQL and Elasticsearch containers.

## Technologies Used

- **Java 17**: The project is built using Java 17, utilizing the latest features of the language.
- **Spring Boot 3.2.4**: The core framework for the application.
- **PostgreSQL**: For managing relational data related to products and employees.
- **Elasticsearch**: For advanced search functionality on product data.
- **OpenAPI**: For API documentation and client-server communication.
- **Swagger**: For annotating and generating API documentation.
- **TestContainers**: For creating isolated environments for testing with PostgreSQL and Elasticsearch.

## Installation

### Prerequisites:
- Java 17
- Maven 3.8+
- Docker (for using TestContainers during testing)
- PostgreSQL (for Service module)
- Elasticsearch (for Service module)

### Steps:
1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/store-manager.git
    cd store-manager
    ```

2. Build the modules:
    ```bash
    mvn clean install
    ```

3. Start PostgreSQL and Elasticsearch using Docker (optional, for local development):
    ```bash
    docker-compose up -d
    ```

4. Run the application (Service module):
    ```bash
    mvn spring-boot:run -pl service
    ```

5. The API will be available at `http://localhost:8080`.

## Usage

Once the application is up and running, you can interact with the API endpoints for managing products and employees.

- **GET** `/api/products`: Fetch all products
- **POST** `/api/products`: Create a new product
- **GET** `/api/employees`: Fetch all employees
- **POST** `/api/employees`: Add a new employee

Swagger UI is also available at `http://localhost:8080/swagger-ui/` for API documentation and easy testing of endpoints.

## Configuration

The following configuration options are available:

- **PostgreSQL**: Configuration for connecting to PostgreSQL is provided in the `application.yml` file (inside the Service module).
- **Elasticsearch**: Configuration for connecting to Elasticsearch is also provided in `application.yml`.

Example `application.yml`:

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/storedb
    username: user
    password: password

  data:
    elasticsearch:
      cluster-name: store-cluster
      cluster-nodes: localhost:9200
```

## Dependencies

### API Module:
- `spring-boot-starter-webflux`: For building reactive web applications.
- `spring-boot-starter-tomcat`: For embedding Tomcat as the HTTP server.
- `swagger-annotations`: For Swagger integration.
- `lombok`: For reducing boilerplate code with annotations like `@Getter`, `@Setter`, etc.

### Service Module:
- `spring-boot-starter-data-r2dbc`: For reactive PostgreSQL support.
- `spring-boot-starter-data-elasticsearch`: For integrating Elasticsearch.
- `mapstruct`: For object mapping.
- `testcontainers`: For creating isolated environments for PostgreSQL and Elasticsearch during testing.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
```

### Notes:
- I've used the provided pom files to identify the dependencies for each module and structured the `README` accordingly.
- I've assumed that the project is a typical Spring Boot application using reactive WebFlux, PostgreSQL, and Elasticsearch, with OpenAPI generating the API documentation.
- You may want to add further configuration details (such as specific environment variables or secrets) depending on how your application is set up.

Let me know if you need any adjustments or further information!
