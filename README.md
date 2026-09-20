# 🥚 Nest — Corporate Innovation Platform API

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" />
  <img src="https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/MongoDB-NoSQL-47A248?style=for-the-badge&logo=mongodb" />
  <img src="https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker" />
</p>

> REST API for **Nest**, GAB Group's corporate innovation platform. Manages the full innovation lifecycle — from idea capture to ROI dashboard — with role-based access control for Operators, Managers and Leaders.

---

## 📖 About

**Nest** is a corporate innovation management platform.

The name "Nest" comes from **ninho** (nest) — the place where the eagle creates, protects and develops. The platform organizes the entire innovation journey into 4 sequential phases:

```
💡 CAPTURE  ──►  🥚 HATCH  ──►  🪽 FLIGHT  ──►  📈 IMPACT
```

| Phase | Description |
|---|---|
| **Capture** | Operators register operational pain points and ideas |
| **Hatch** | Managers evaluate, prioritize and approve ideas |
| **Flight** | Approved ideas become structured projects in execution |
| **Impact** | Leaders visualize ROI, savings and operational results |

---

## 🏗️ Architecture

```
nest-gab-api/
├── src/main/java/br/com/gabnest/nest_gab_api/
│   ├── config/                  # Security and app configuration
│   │   └── SecurityConfig.java
│   ├── controller/              # REST Controllers
│   │   ├── AuthController.java
│   │   ├── GuidelineController.java
│   │   ├── IdeaController.java
│   │   ├── ProjectController.java
│   │   └── DashboardController.java
│   ├── dto/                     # Data Transfer Objects
│   │   ├── auth/
│   │   ├── guideline/
│   │   ├── idea/
│   │   ├── project/
│   │   └── user/
│   ├── model/                   # MongoDB Documents
│   │   ├── enums/
│   │   │   ├── UserRole.java
│   │   │   ├── IdeaStatus.java
│   │   │   ├── ProjectStatus.java
│   │   │   └── ProjectStage.java
│   │   ├── User.java
│   │   ├── StrategicGuideline.java
│   │   ├── GuidelineHistory.java
│   │   ├── Idea.java
│   │   └── Project.java
│   ├── repository/              # Spring Data MongoDB Repositories
│   ├── security/                # JWT Filter
│   │   └── JwtAuthFilter.java
│   └── service/                 # Business Logic
│       ├── AuthService.java
│       ├── JwtService.java
│       ├── UserDetailsServiceImpl.java
│       ├── GuidelineService.java
│       ├── IdeaService.java
│       ├── ProjectService.java
│       └── DashboardService.java
└── src/main/resources/
    ├── application.properties
    └── (MongoDB collections are created by the application at runtime)
```

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Language |
| Spring Boot | 4.0.6 | Framework |
| Spring Security | 7.x | Authentication & Authorization |
| Spring Data MongoDB | 4.x | MongoDB persistence |
| MongoDB | 7.x | Database |
| JJWT | 0.12.6 | JWT Token Generation |
| Lombok | latest | Boilerplate Reduction |
| Docker | latest | Containerization |

---

## ⚙️ Prerequisites

- [Java 21+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/seu-usuario/nest-gab-api.git
cd nest-gab-api
```

### 2. Start the MongoDB Database

```bash
docker compose up -d
```

This starts the `mongo-db` service with persistent storage. Use `mongosh` or MongoDB Compass to confirm the database is reachable.

### 3. Run the application

```bash
mvn spring-boot:run
```

The application creates the required MongoDB collections and seeds the 3 default users on startup.

### 4. Verify

```bash
curl http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"carlos.operador@aguiabranca.com.br","password":"nest123"}'
```

---

## 🐳 Docker Compose

```yaml
services:
  mongo-db:
    image: mongo:7
    container_name: nest-mongo
    environment:
      MONGO_INITDB_ROOT_USERNAME: nest
      MONGO_INITDB_ROOT_PASSWORD: nest123
    ports:
      - "27017:27017"
    volumes:
      - mongo-data:/data/db

volumes:
  mongo-data:
```

### Useful Docker commands

```bash
# Start containers
docker compose up -d

# Stop containers (keeps data)
docker compose down

# Stop and remove all data (full reset)
docker compose down -v

# View MongoDB logs
docker logs -f nest-mongo

# Open a Mongo shell
docker exec -it nest-mongo mongosh -u nest -p nest123 --authenticationDatabase admin
```

---

## 🗄️ Database

### Data Model

```
users
  id, name, email, password, role (stored as string), active, createdAt, updatedAt

strategic_guidelines
  id, title, content, category, campaign, active,
  createdById, createdAt, updatedAt

guideline_history
  id, guidelineId, date, category, campaign, contentSnapshot

ideas
  id, title, description, status (stored as string), priority,
  submittedById, reviewedById, reviewedAt, guidelineId,
  createdAt, updatedAt

projects
  id, title, description, status (stored as string), stage (stored as string),
  investment, expectedReturn, actualReturn, productivityGain,
  startDate, endDate, createdById, ideaId, guidelineId,
  createdAt, updatedAt
```

> Relationships are stored as plain String IDs instead of `@DBRef` to keep queries simple and avoid N+1-style lookups.

> `guideline_history` is append-only: every create/update of a strategic guideline should persist a new snapshot record.

### Seed Users

| Name | Email | Password | Role |
|---|---|---|---|
| Carlos Operador | carlos.operador@aguiabranca.com.br | nest123 | OPERATOR |
| Ana Gestora | ana.gestora@aguiabranca.com.br | nest123 | MANAGER |
| Roberto Lider | roberto.lider@aguiabranca.com.br | nest123 | LEADER |

---

## 🔐 Authentication

The API uses **JWT (JSON Web Token)** for stateless authentication.

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "carlos.operador@aguiabranca.com.br",
  "password": "nest123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": "66f1e9c4b8a2d3a1f0c12345",
  "name": "Carlos Operador",
  "role": "OPERATOR"
}
```

### Register

```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "Novo Colaborador",
  "email": "novo@aguiabranca.com.br",
  "password": "senha123"
}
```

> New users are always registered with `OPERATOR` role by default.

### Using the token

Include the token in all subsequent requests:

```http
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

---

## 📡 API Endpoints

### Role-based Access Control

| Endpoint | OPERATOR | MANAGER | LEADER |
|---|---|---|---|
| `GET /api/guidelines` | ✅ | ✅ | ✅ |
| `POST /api/guidelines` | ❌ | ❌ | ✅ |
| `PUT /api/guidelines/{id}` | ❌ | ❌ | ✅ |
| `DELETE /api/guidelines/{id}` | ❌ | ❌ | ✅ |
| `GET /api/guidelines/{id}/history` | ❌ | ❌ | ✅ |
| `POST /api/ideas` | ✅ | ❌ | ❌ |
| `GET /api/ideas/my` | ✅ | ❌ | ❌ |
| `PUT /api/ideas/{id}` | ✅ | ❌ | ❌ |
| `DELETE /api/ideas/{id}` | ✅ | ❌ | ❌ |
| `GET /api/ideas` | ❌ | ✅ | ❌ |
| `PATCH /api/ideas/{id}/review` | ❌ | ✅ | ❌ |
| `POST /api/projects` | ❌ | ✅ | ❌ |
| `PUT /api/projects/{id}` | ❌ | ✅ | ❌ |
| `GET /api/projects` | ❌ | ✅ | ✅ |
| `GET /api/dashboard` | ❌ | ❌ | ✅ |

### Strategic Guidelines

```
GET    /api/guidelines          # List active guidelines
GET    /api/guidelines/{id}     # Get guideline by id
POST   /api/guidelines          # Create guideline (LEADER)
PUT    /api/guidelines/{id}     # Update guideline (LEADER)
DELETE /api/guidelines/{id}     # Soft delete guideline (LEADER)
GET    /api/guidelines/{id}/history # Append-only history (LEADER)
```

### Ideas

```
POST   /api/ideas               # Submit idea (OPERATOR)
GET    /api/ideas               # List all ideas (MANAGER)
GET    /api/ideas?status=       # Filter by status (MANAGER)
GET    /api/ideas/my            # List my ideas (OPERATOR)
GET    /api/ideas/overview      # Read-only overview (all authenticated roles)
GET    /api/ideas/{id}          # Get idea by id
PUT    /api/ideas/{id}          # Update own pending idea (OPERATOR)
DELETE /api/ideas/{id}          # Delete own pending idea (OPERATOR)
PATCH  /api/ideas/{id}/review   # Review idea (MANAGER)
```

**Idea Status Flow:**
```
PENDING  ──►  PRIORITIZED  ──►  APPROVED
                              └──►  REJECTED
```

### Projects

```
POST   /api/projects            # Create project (MANAGER)
GET    /api/projects            # List all projects (MANAGER, LEADER)
GET    /api/projects/overview   # Read-only project overview (all authenticated roles)
GET    /api/projects/{id}       # Get project by id (MANAGER, LEADER)
PUT    /api/projects/{id}       # Update project (MANAGER)
```

**Project Status:** `PLANNING` → `IN_PROGRESS` → `COMPLETED` / `CANCELLED`

**Project Stage:** `IDEATION` → `VALIDATION` → `PLANNING` → `EXECUTION` → `MONITORING` → `COMPLETED`

### Dashboard

```
GET    /api/dashboard           # ROI summary and metrics (LEADER)
GET    /api/dashboard/by-guideline # Aggregated metrics by guideline (LEADER)
GET    /api/dashboard/by-project   # Aggregated metrics by project (LEADER)
```

**Dashboard response includes:** Total ROI (%), total savings, completed projects count, ideas implemented count, and project summaries. Project summaries include `ideaId` and `guidelineId`.

**Grouped response fields:** `id`, `projectCount`, `totalInvestment`, `totalExpectedReturn`, `totalActualReturn` and `totalProductivityGain`.

---

## 🧪 Testing with Postman

Import the collection file `nest_gab_api.postman_collection.json` available in the repository root.

The collection includes:
- Pre-configured requests for all endpoints
- Automatic token saving after login via test scripts
- Collection variables for `token_operator`, `token_manager` and `token_leader`
- Access control validation requests (expected to return 403)

**Recommended test flow:**
```
1. Auth → Login Operator    (token saved automatically)
2. Auth → Login Manager     (token saved automatically)
3. Auth → Login Leader      (token saved automatically)
4. Run all business endpoints
5. Run Access Control Validation folder (all should return 403)
```

---

## 📝 Configuration

`src/main/resources/application.properties`

```properties
spring.application.name=nest-gab-api

# Database
spring.data.mongodb.uri=mongodb://nest:nest123@localhost:27017/nest_gab_api?authSource=admin

# JWT
jwt.secret=nest-gab-super-secret-key-that-is-at-least-256-bits-long-for-hs256
jwt.expiration=86400000
```

---

## 👥 User Roles

| Role | Description | Permissions |
|---|---|---|
| `OPERATOR` | Front-line employees | Submit ideas, track own ideas, read guidelines |
| `MANAGER` | Coordinators and managers | Evaluate ideas, manage projects, read guidelines |
| `LEADER` | Company leadership | Manage guidelines, view all projects, access dashboard |

---

## 🔗 Related Repositories

| Repository | Description |
|---|---|
| `nest-gab-api` | This repository — Spring Boot REST API |
| `nest-app` | Android app (Kotlin + Jetpack Compose) |

---

<p align="center"><i>Nest — Where ideas are born 🥚</i></p>
