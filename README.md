# Ascendion Library REST API

## Overview

This is a simple RESTful API built with **Java 17** and **Spring Boot** to manage a library. It provides endpoints for registering borrowers and books, borrowing and returning books, and viewing all available books.

### Key Highlights

- Multiple copies of the same ISBN can be registered with unique IDs.
- Only one borrower can borrow a given book copy at a time.
- Clear validation and centralized error handling.
- Auto-generated Swagger UI documentation.
- Container-ready for Docker and Kubernetes deployment.
- ☁Adheres to key [Twelve-Factor App](https://12factor.net/) principles.

---

## Features

- Register borrowers with unique emails.
- Register multiple copies of books (even with the same ISBN).
- View all books with current borrowing status.
- Borrow and return specific book copies by ID.
- Centralized, meaningful error responses.
- Swagger UI for interactive API exploration.

---

## Configuration

You can configure the application via `application.properties` or environment variables.

### `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/librarydb
spring.datasource.username=postgres
spring.datasource.password=your_password
server.port=8080
```

### Or using environment variables:

- `DB_URL`
- `DB_USER`
- `DB_PASS`
- `PORT` (optional)

---

## Build and Run Locally

### Rub with Maven:

```bash
mvn spring-boot:run
```

### Build with Maven:

```bash
mvn clean package
```

### Run the JAR:

```bash
java -jar target/library-1.0.0.jar
```

### Access Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## API Usage

| Endpoint                                  | Method | Description                 |
|-------------------------------------------|--------|-----------------------------|
| `/api/borrowers`                          | GET    | List all borrowers          |
| `/api/borrowers`                          | POST   | Register a new borrower     |
| `/api/borrowers/{borrowerId}`             | GET    | Get spesific borrower       |
| `/api/books`                              | POST   | Register a new book copy    |
| `/api/books`                              | GET    | List all books              |
| `/api/books/{bookId}/borrow/{borrowerId}` | POST   | Borrow a specific book copy |
| `/api/books/{bookId}/return/{borrowerId}` | POST   | Return a borrowed book      |

---

## Example Requests

### ➕ Register Borrower

```http
POST /api/borrowers
Content-Type: application/json

{
  "name": "Khairunnisa",
  "email": "khairunnisa@ascendion.com"
}
```

### Register Book

```http
POST /api/books
Content-Type: application/json

{
  "isbn": "9780132350884",
  "title": "Clean Code",
  "author": "Khairunnisa"
}
```

### Borrow Book

```http
POST /api/books/1/borrow/1
```

---

## Error Handling

- **400 Bad Request**: Invalid data, already borrowed book, etc.
- **404 Not Found**: Book or borrower not found.
- **409 Conflict**: Borrower with same email already exists.

#### Example Response:

```json
{
  "timestamp": "2025-05-21T15:30:00",
  "message": "Book is already borrowed"
}
```

---

## Assumptions

- Each physical book copy has a unique ID.
- Books with the same ISBN have the same title and author.
- Borrower emails must be unique.
- No authentication or authorization is implemented.
- Configurable via environment variables for 12-Factor compliance.
- Logs go to stdout (container-friendly).
- Supports fast startup and graceful shutdown.

---

## Docker Usage

### Build Docker image:

```bash
docker build -t library .
```

### Run Docker container:

```bash
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host:5432/librarydb \
  -e DB_USER=your_user \
  -e DB_PASS=your_password \
  library
```

---

## Twelve-Factor App Compliance

This application:

- Stores config in environment variables.
- Treats DB as a backing service.
- Is stateless and horizontally scalable.
- Uses declarative build/run processes.
- Supports development/production parity via containers.

---

## Testing

Run unit and integration tests using:

```bash
mvn test
```

---

