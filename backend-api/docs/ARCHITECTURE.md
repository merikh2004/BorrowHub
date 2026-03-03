# Laravel Backend Architecture Guide
### PHP + MySQL Integration

---

## Overview

The **backend-api** serves as the central engine for BorrowHub. It handles data persistence in **MySQL**, enforces business logic, and provides RESTful endpoints for the Android application using a strict **Service-Repository Pattern** on top of Laravel's MVC.

## End-to-End Data Flow

To ensure separation of concerns, every request follows a strict lifecycle:

```text
[ Mobile App ]
      │
      v (HTTP Request)
[ Routes (api.php) ]
      │
      v
[ Middleware ] (Authentication & Role Authorization)
      │
      v
[ Controller ] <─── [ Form Request ] (Input Validation)
      │
      v
[ Service Layer ] (Business Logic & Exception Throwing)
      │
      v
[ Repository Layer ] (Database Queries / Eloquent)
      │
      v
[ Model (MySQL) ] (Returns Data)
      │
      v
[ API Resource ] (Formats JSON Response)
      │
      v (JSON Response via Base Controller)
[ Mobile App ]
```

1.  **Request Layer:** The Android app sends requests to Laravel routes. Middleware (e.g., Sanctum) checks authentication.
2.  **Validation:** Custom Form Requests validate incoming data before it hits the Controller.
3.  **Controller Layer:** Acts purely as a traffic director. It receives validated data, passes it to a Service, and returns a formatted Resource.
4.  **Service Layer:** Contains all business logic (e.g., checking item availability, calculating dates).
5.  **Repository Layer:** Handles all database interactions. Controllers and Services must *never* use Eloquent models directly for querying.
6.  **Resource Layer:** Transforms models into standardized JSON structures.

## Directory Structure

The following structure is enforced for the backend application:

```text
app/
├── Exceptions/       # Custom business exceptions (e.g., ItemUnavailableException)
├── Http/
│   ├── Controllers/  # Thin controllers extending a Base Controller
│   ├── Requests/     # Validation logic
│   └── Resources/    # JSON formatting/transformers
├── Models/           # Eloquent models representing database tables
├── Repositories/     # Database access classes and interfaces
└── Services/         # Core business logic
routes/
└── api.php           # API endpoint definitions
```

## Key Components

### 1. Routes (`routes/api.php`)
Defines the entry points for the mobile app (e.g., `/api/login`, `/api/items`, `/api/borrow`).

### 2. Form Requests (`app/Http/Requests/`)
Handles all input validation. If validation fails, it automatically returns a `422 Unprocessable Entity` response.

### 3. Controllers (`app/Http/Controllers/`)
Handles HTTP requests. **Must not contain business logic.** It delegates work to the Service layer and returns data using API Resources.

### 4. Services (`app/Services/`)
Executes the core rules of BorrowHub. If a rule is violated, it throws a Custom Exception.

### 5. Repositories (`app/Repositories/`)
Encapsulates all database queries (`Model::where()`, `Model::create()`, etc.). Interfaces should be used to allow easy mocking during testing.

### 6. Models & Migrations (`app/Models/` & `database/migrations/`)
Defines the relational schema in MySQL.

## Database Schema (Relational)

| Table | Purpose |
|---|---|
| `users` | Store Admin and CSD/MIS Staff accounts. |
| `items` | Inventory of borrowable assets. |
| `borrow_records` | History and active borrowing transactions. |
| `categories` | Categorization of equipment. |

## Development Rules

1.  **Thin Controllers:** Never write business logic or database queries inside a Controller. Use Services and Repositories.
2.  **Always Validate:** Use Laravel's Form Requests for robust input validation. Never validate inside the Controller.
3.  **Standardized Responses:** All JSON responses must use the Base Controller's utility methods (e.g., `$this->successResponse()`, `$this->errorResponse()`) to ensure a consistent format.
4.  **API Resources:** Always return data using API Resources (Transformers) to hide database columns (like `created_at` or internal IDs) and format the output correctly for the Mobile App.
5.  **Security:** Use Laravel Sanctum for API Token authentication. Tokens are passed via the `Authorization: Bearer` header.
6.  **Relationships:** Utilize Eloquent Relationships (HasMany, BelongsTo) within Repositories for efficient data retrieval.
