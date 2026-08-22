# HR System

A modern, full-featured Human Resources management system built with Spring Boot 3.5.x and Java 21. Designed to streamline employee lifecycle management from onboarding to offboarding, with robust payroll processing, leave tracking, and team collaboration features.

## 🏢 **Overview**

The HR System provides a comprehensive suite of tools for managing employee data, teams, compensation, leaves, and bonuses. It features a RESTful API with JWT authentication, role-based access control, and integrates with MySQL databases via Flyway migrations.

---

## ✨ **Key Features**

### **👥 Employee Management**
- **Add Employees**: Full CRUD operations with validation for email format, date of birth, gender, salary minimum (500), department/team/expertise existence
- **Modify Employees**: Partial updates with selective field changes
- **Delete Employees**: Safe deletion that reassigns subordinates to the employee's manager
- **Employee Hierarchy**: View subordinates, direct reports, and recursive team hierarchies

### **💰 Compensation & Payroll**
- **Salary Processing**: Calculate gross/net salaries with tax (15%) and insurance (500)
- **Payroll Generation**: Spring Batch-based monthly payroll processing with automatic deductions for exceeded leaves
- **Salary Raises**: Apply percentage increases to employee salaries with before/after comparison
- **Bonus System**: Award bonuses that create salary adjustment records

### **� absence & Leave Management**
- **Leave Recording**: Track employee leaves (current year only)
- **Leave Deductions**: Automatic salary deductions for exceeded leaves based on years of experience

### **👥 Team & Organizational Structure**
- **Team Management**: Assign employees to teams, retrieve team member lists
- **Department Assignment**: Organize employees by department
- **Expertise Tracking**: Tag employees with skills/expertises
- **Manager-Subordinate Relationships**: Hierarchical organizational structure

### **🔐 Security & Authentication**
- **JWT-Based Authentication**: Token-based login with 1-hour expiration
- **Role-Based Access Control**: Secure endpoints with authentication provider
- **CORS Configuration**: Configurable cross-origin resource sharing
- **Exception Handling**: Global exception handler for consistent error responses

### **📊 Monitoring & Observability**
- **Prometheus Integration**: Spring Actuator endpoints exposed for Prometheus scraping
- **Health Metrics**: Application health, metrics, and performance monitoring

### **🐳 DevOps & Deployment**
- **Docker Support**: Multi-stage Dockerfile for containerized deployment
- **Flyway Migrations**: Database schema versioning and migration management
- **Gradle Builds**: Optimized build pipeline with Checkstyle and semantic-release

---

## 🛠 **Technology Stack**

| Layer | Technology |
|-------|-----------|
| **Framework** | Spring Boot 3.5.6, Spring Security, Spring Data JPA |
| **Language** | Java 21 |
| **Database** | MySQL 8.x (Flyway migrations) |
| **Build** | Gradle with Axion Release Plugin |
| **Testing** | JUnit 5, MockDBUnit, Testcontainers, DBUnit |
| **Authentication** | JWT (jjwt), BCrypt Password Encoding |
| **Monitoring** | Prometheus, Spring Actuator |
| **Containerization** | Docker |
| **CI/CD** | GitHub Actions, semantic-release |

---

## � API **Endpoints (Grouped by Feature)**

### **Authentication** (`/auth`)
- `POST /login` - Authenticate and receive JWT token

### **Employees** (`/employee`)
- `POST` - Add new employee
- `GET` - Get direct subordinates by manager ID
- `GET /{id}` - Get employee by ID
- `GET /{id}/salary` - Get employee salary (gross/net)
- `GET /{id}/subordinates` - Get all subordinates (recursive)
- `PATCH /{id}` - Modify employee (partial update)
- `DELETE /{id}` - Delete employee (reassigns subordinates)
- `POST /{Id}/leave` - Record employee leave
- `POST /{Id}/bonus` - Add bonus to employee
- `POST /{Id}/raise` - Apply salary raise to employee

### **Teams** (`/team`)
- `GET /{teamId}/members` - Get all members of a team

### **Payroll** (`/payment`)
- `POST` - Generate payroll (Spring Batch job)

---

## 📸 **Screenshots & API Documentation**

The project includes **Swagger UI** for interactive API exploration:

- **Swagger UI**: Available at `/swagger-ui.html` when running
- **OpenAPI Specs**: Available at `/v3/api-docs`

Generated documentation covers all endpoints with request/response models, error handling, and authentication requirements.

---

## 🚀 **Getting Started**

### **Prerequisites**
- Java 21 JDK
- MySQL 8.x server
- Gradle 8.x
- Docker (optional, for containerized deployment)

### **Configuration**
1. Copy `env.properties` and configure database credentials
2. Set MySQL root password and HR database
3. Ensure `allowedOrigins` in `application.properties` matches your frontend domain

### **Running the Application**
```bash
# Using Gradle
./gradlew bootRun

# Or using Docker (build first)
./gradlew bootJar
docker build -t hr-system .
docker run -p 8080:8080 hr-system
```

### **Accessing the System**
- **API Base URL**: `http://localhost:8080`
- **Health Check**: `GET /actuator/health`
- **Metrics**: `GET /actuator/prometheus`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

---

## 📦 **Deployment**

### **Docker**
```dockerfile
# Already configured in Dockerfile
# Builds a fat JAR and runs via: java -jar hr.jar
```

### **Production Considerations**
- Set appropriate `allowedOrigins` for CORS
- Use environment variables for secrets (JWT secret, DB credentials)
- Expose only needed Actuator endpoints
- Configure Flyway for schema migrations on startup

---

## 🧪 **Testing**

The project employs a comprehensive testing strategy:

- **Integration Tests**: DBUnit-based with XML dataset fixtures
- **MockMvc Tests**: Controller-level testing with Mockito mocks
- **Spring Batch Tests**: Payroll processing validation
- **Testcontainers**: MySQL database spinning for integration tests
- **Code Coverage**: JaCoCo plugin configured

Run tests:
```bash
./gradlew test
```

---

## 📁 **Project Structure**

```
src/main/java/com/orange/hr/
├── controller/        # REST API endpoints
├── service/           # Business logic services
├── service/impl/      # Service implementations
├── repository/        # Spring Data JPA repositories
├── entity/            # JPA entities (Employee, Team, Department, etc.)
├── dto/               # Data Transfer Objects for API
├── mapper/            # Entity-to-DTO conversion
├── security/          # JWT authentication setup
├── exceptions/        # Custom exception classes
├── validation/        # Validation annotations
└── payment/           # Payroll calculation logic
```

---

## 🛠 **Development**

### **Code Quality**
- **Checkstyle**: Enforced code formatting
- **Semantic Release**: Automated CHANGELOG and versioning via GitHub Actions
- **Lombok**: Reduces boilerplate code (getters, setters, builders)

### **Database Migrations**
Flyway migrations located in `src/main/resources/db/migration/`
- Common migrations shared across databases
- MySQL-specific migrations

### **Adding New Features**
1. Create DTO for request/response
2. Add entity mapping if needed
3. Extend EmployeeService interface
4. Implement in EmployeeServiceImpl
5. Add controller endpoint
6. Write integration tests with DBUnit datasets

---

## 👥 **Target Audience**

### **For HR Managers:**
- Intuitive employee onboarding/offboarding flow
- Leave tracking with automatic year validation
- Bonus and raise management with clear audit trails
- Team and department organization views

### **For Technical Managers:**
- Modern Spring Boot 3.5 with Java 21
- Scalable architecture with layered design
- Comprehensive test coverage (integration + unit)
- Production-ready Docker deployment
- Monitoring via Prometheus + Actuator
- Semantic-release enabled CI/CD pipeline

---

## 📜 **Version History**

See [CHANGELOG.md](CHANGELOG.md) for detailed release notes covering:
- Employee management features (add, modify, delete)
- Payroll and bonus systems
- Leave tracking and deductions
- Security and authentication enhancements
- Docker and CI/CD pipeline additions
- Bug fixes and performance improvements

---

## 🤝 **Contributing**

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/foo-bar`)
3. Commit your changes (`git commit -m 'feat: add new feature'`)
4. Push to branch (`git push origin feature/foo-bar`)
5. Open a Pull Request

The project uses **semantic-release** with conventional commits - commit messages follow the format: `type: description` (feat, fix, chore, docs, style, refactor, test, build, perf)

---

## 📧 **Contact**

Project maintained by Ahmed Eldera. 
GitHub: [Ahmed-Eldera/HR-System](https://github.com/Ahmed-Eldera/HR-System)