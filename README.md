# Employee Management System - Spring Boot Application

## Overview
This Spring Boot application is a comprehensive employee management system designed to demonstrate practical implementation of RESTful APIs with JPA repositories. It models three core entities: Departments, Employees, and Projects, with appropriate relationships between them.

## Features
- CRUD operations for Departments and Employees
- Many-to-One relationship between Employees and Departments
- Many-to-Many relationship between Employees and Projects
- Custom repository queries for advanced searching
- Comprehensive error handling
- RESTful API design with proper HTTP status codes
- Data validation and integrity checks

## Technologies Used
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Hibernate
- MySQL Database
- Maven
- RESTful Web Services


## Getting Started

### Prerequisites
- Java 17 JDK
- MySQL Server
- Maven

### Installation
1. Clone the repository:
```bash
git clone https://github.com/yourusername/sampleapp.git
cd sampleapp
```

2. Configure the database:
```bash
mysql -u root -p
CREATE DATABASE employee_db;
```

3. Update database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_db
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

4. Build and run the application:
```bash
mvn spring-boot:run
```

## API Endpoints

### Department Endpoints
| Method | Endpoint               | Description                     | Status Codes |
|--------|------------------------|---------------------------------|--------------|
| GET    | `/dept`                | Get all departments             | 200 OK       |
| GET    | `/dept/{id}`           | Get department by ID            | 200 OK, 404 Not Found |
| POST   | `/dept`                | Create new department           | 201 Created, 409 Conflict |
| PUT    | `/dept/{id}`           | Update department               | 200 OK, 404 Not Found |
| DELETE | `/dept/{id}`           | Delete department               | 200 OK, 404 Not Found |
| GET    | `/dept/names`          | Get all department names        | 200 OK       |
| GET    | `/dept/names/{nm}`     | Search departments by name      | 200 OK, 404 Not Found |

### Employee Endpoints
| Method | Endpoint               | Description                     | Status Codes |
|--------|------------------------|---------------------------------|--------------|
| GET    | `/emp`                 | Get all employees               | 200 OK       |
| GET    | `/emp/{id}`            | Get employee by ID              | 200 OK, 404 Not Found |
| POST   | `/emp`                 | Create new employee             | 201 Created, 409 Conflict |
| PUT    | `/emp/{id}`            | Update employee                 | 200 OK, 404 Not Found |
| DELETE | `/emp/{id}`            | Delete employee                 | 200 OK, 404 Not Found |
| GET    | `/emp/salary-range`    | Find employees in salary range  | 200 OK       |

## Custom Repository Queries

### Employee Repository
```java
public interface EmployeeRepo extends JpaRepository<Employee, String> {
    
    // Find employees within a salary range
    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN ?1 AND ?2")
    List<Employee> findSalaryRange(double minSalary, double maxSalary);
    
    // Find employees by department ID
    @Query("SELECT e FROM Employee e WHERE e.department.id = ?1")
    List<Employee> findByDepartmentId(int deptId);
}
```

### Department Repository
```java
public interface DepartmentRepo extends JpaRepository<Department, Integer> {
    
    // Get department names only
    @Query("SELECT d.name FROM Department d")
    List<String> getDeptNames();
    
    // Search departments by name (case-insensitive)
    @Query("SELECT d FROM Department d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Department> searchname(String name);
}
```

## Service Layer Highlights

### Department Service
```java
@Service
public class DepartmentService {
    
    public Department updateDept(int id, Department updatedDepartment) {
        Department existingDept = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        
        // Update only allowed fields
        existingDept.setName(updatedDepartment.getName());
        
        return repo.save(existingDept);
    }
    
    public List<Department> searchDeptName(String name) {
        List<Department> results = repo.searchname(name);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No departments found with name: " + name);
        }
        return results;
    }
}
```

### Employee Service
```java
@Service
public class EmployeeService {
    
    public String addEmp(Employee employee) {
        // Check for duplicate employee ID
        if (repo.findById(employee.getEmpNo()).isPresent()) {
            throw new DuplicateKeyException("Employee ID already exists");
        }
        
        // Validate department exists
        if (employee.getDepartment() != null && 
            !departmentRepo.existsById(employee.getDepartment().getId())) {
            throw new EntityNotFoundException("Department not found");
        }
        
        repo.save(employee);
        return "New Employee added successfully";
    }
    
    public List<Employee> getEmployeesInSalaryRange(double min, double max) {
        return repo.findSalaryRange(min, max);
    }
}
```

## Controller Examples

### Department Controller
```java
@RestController
@RequestMapping("/dept")
public class DepartmentController {
    
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable int id) {
        Department dept = service.getDept(id);
        return ResponseEntity.ok(dept);
    }
    
    @PostMapping
    public ResponseEntity<String> createDepartment(@RequestBody Department department) {
        String result = service.addDept(department);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
```

### Employee Controller
```java
@RestController
@RequestMapping("/emp")
public class EmployeeController {
    
    @GetMapping("/salary-range")
    public ResponseEntity<List<Employee>> getEmployeesInSalaryRange(
            @RequestParam double min, 
            @RequestParam double max) {
        List<Employee> employees = service.getEmployeesInSalaryRange(min, max);
        return ResponseEntity.ok(employees);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable String id,
            @RequestBody Employee updatedEmployee) {
        Employee employee = service.updateEmp(id, updatedEmployee);
        return ResponseEntity.ok(employee);
    }
}
```

## Exception Handling
The application uses a global exception handler to manage errors:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicateKey(DuplicateKeyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
```

## Testing the Application

### Sample Requests

**Create Department:**
```http
POST /dept
Content-Type: application/json

{
  "id": 101,
  "name": "Engineering",
  "established": "2023-01-15"
}
```

**Create Employee:**
```http
POST /emp
Content-Type: application/json

{
  "empNo": "E1001",
  "name": "John Doe",
  "age": 30,
  "salary": 65000,
  "gender": "Male",
  "department": {
    "id": 101
  }
}
```

**Find Employees by Salary Range:**
```http
GET /emp/salary-range?min=50000&max=70000
```

**Search Departments by Name:**
```http
GET /dept/names/eng
```

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.