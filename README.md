# Freelance Management System

A RESTful API backend for managing the full lifecycle of freelance work — projects, tasks, clients, and time tracking. Built with Java 17, Spring Boot, Spring Security (JWT), and MySQL.

---

## Features

- **Client Management** — Create and manage client profiles with contact details and project history
- **Project Tracking** — Assign projects to clients, set deadlines, and monitor status
- **Task Management** — Break projects into tasks, assign priorities, and track completion
- **Time Tracking** — Log time entries per task; calculate billable hours per project or client
- **JWT Authentication** — Secure all endpoints with role-based access (Admin / Freelancer)
- **RESTful Design** — Clean, consistent API following REST conventions

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Security | Spring Security + JWT |
| Database | MySQL |
| ORM | JPA / Hibernate |
| Build | Gradle |

---

## Getting Started

### Prerequisites

- Java 17+
- MySQL 8+
- Gradle

### Setup

```bash
git clone https://github.com/vusal466/freelance-management-system.git
cd freelance-management-system
```

Configure your database connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/freelance_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Run the application:

```bash
./gradlew bootRun
```

API will be available at `http://localhost:8080`

---

## API Endpoints

### Auth

```
POST /api/auth/register     - Register new user
POST /api/auth/login        - Login and receive JWT token
```

### Clients

```
GET    /api/clients          - List all clients
POST   /api/clients          - Create a new client
GET    /api/clients/{id}     - Get client details
PUT    /api/clients/{id}     - Update client
DELETE /api/clients/{id}     - Delete client
```

### Projects

```
GET    /api/projects                  - List all projects
POST   /api/projects                  - Create a new project
GET    /api/projects/{id}             - Get project details
PUT    /api/projects/{id}             - Update project
GET    /api/projects/{id}/tasks       - List tasks for a project
```

### Tasks

```
POST   /api/tasks                     - Create task
PUT    /api/tasks/{id}                - Update task status/details
DELETE /api/tasks/{id}                - Delete task
```

### Time Entries

```
POST   /api/time-entries              - Log a time entry
GET    /api/time-entries/project/{id} - Get time logs for a project
GET    /api/time-entries/summary      - Get billable hours summary
```

---

## Data Model

```
Client
  └── Projects (1:N)
        └── Tasks (1:N)
              └── Time Entries (1:N)
```

---

## Security

All endpoints (except `/api/auth/**`) require a valid JWT token in the `Authorization` header:

```
Authorization: Bearer <your_token>
```

---

## Future Improvements

- [ ] Invoice generation from billable hours
- [ ] Email notifications for project deadlines
- [ ] Dashboard statistics endpoint
- [ ] Docker support

---

## Author

**Vusal Cafarli** — Java Backend Developer
- GitHub: [@vusal466](https://github.com/vusal466)
