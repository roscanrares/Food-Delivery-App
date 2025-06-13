# Food-Delivery-App

A Java-based application for food delivery management, structured with a clear separation between models, data access, services, and database connectivity.


The project’s objective is to serve as a comprehensive exercise in mastering object-oriented programming (OOP) paradigms, allowing me to translate theoretical principles into concrete, maintainable code architectures. By modeling entities, relationships, and business logic according to OOP best practices, I am able to explore the power of abstraction, code reuse, and flexibility. Moreover, by connecting the application to a relational database, I gain practical experience in data persistence and real-world integration, bridging the gap between backend logic and data management.
## Project Structure

- **src/main/java/app**: Main application logic and entry points.
- **src/main/java/model**: Domain models (entities such as User, Order, Restaurant, Driver, etc.).
- **src/main/java/dao**: Data Access Objects (DAOs) handling CRUD operations for each entity.
- **src/main/java/service**: Business logic and service layer.
- **src/main/java/db**: Database connection helper class.
- **src/main/java/exception**: Custom exceptions for error handling.

## Database Connection

The application uses a direct JDBC connection to a PostgreSQL database. The connection is managed by the `DBConnection` class:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/fooddelivery";
private static final String USER = "postgres";
private static final String PASSWORD = "password";
public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
}
```

- **Database**: PostgreSQL
- **Default URL**: `jdbc:postgresql://localhost:5432/fooddelivery`
- **Default User**: `postgres`
- **Default Password**: `password`
- Update these credentials in `src/main/java/db/DBConnection.java` as needed for your environment.

## Features

- User registration and login
- Restaurant management
- Menu browsing
- Order placement and management
- Driver assignment and delivery tracking
- Admin and customer roles
- Data persistence via DAOs

## Technologies Used

- Java (100%)
- JDBC for database access
- PostgreSQL database
- Maven for dependency management (`pom.xml`)
- (Optional) Docker for environment setup (`docker-compose.yml`)

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven
- PostgreSQL running locally (or update the DB config for your setup)

### Installation & Setup

1. **Clone the repository:**
    ```bash
    git clone https://github.com/roscanrares/Food-Delivery-App.git
    ```
2. **Set up the PostgreSQL database:**
    - Create a database named `fooddelivery`
    - Adjust user/password as in `DBConnection.java` or set your own.
3. **(Optional) Use Docker Compose:**
    - A `docker-compose.yml` is provided for containerized DB setup.
4. **Build and run the application:**
    ```bash
    mvn clean install
    # Run the app as per your entry point in app/
    ```

### Usage

- Register/login as a user or admin.
- Browse restaurants and menus.
- Place, view, and manage orders.
- Admins can manage restaurants, menus, and drivers.

