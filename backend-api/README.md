# BorrowHub Backend API
### Laravel (PHP) + MySQL Integration

The **backend-api** is the engine that powers the BorrowHub system. It manages data, handles authentication, and provides the RESTful API consumed by the mobile application.

## Prerequisites

- PHP 8.1 or higher
- Composer
- MySQL 8.0 or higher
- A local server (e.g., Apache, Nginx, or `php artisan serve`)

## Setup & Installation

1.  **Navigate to the backend directory:**
    ```bash
    cd backend-api
    ```
2.  **Install dependencies:**
    ```bash
    composer install
    ```
3.  **Configure environment:**
    - Copy `.env.example` to `.env`.
    - Set your MySQL database credentials in `.env`.
    - Generate an app key: `php artisan key:generate`.
4.  **Run migrations:**
    ```bash
    php artisan migrate
    ```
5.  **Run the server:**
    ```bash
    php artisan serve
    ```

## API Versioning

This backend API uses versioning. All API endpoints must be prefixed with `/api/v1/` (e.g., `/api/v1/login`, `/api/v1/items`). This structure ensures backwards compatibility for future mobile app updates.

## Documentation

- **[ARCHITECTURE.md](./docs/ARCHITECTURE.md):** Overview of the API structure, the **Service-Repository Pattern**, database schema, and strict development standards. Ensure you read this before making any code changes.
- **[CONTRIBUTING.md](../docs/CONTRIBUTING.md):** Shared guidelines for contributing to the BorrowHub project.

---
*BorrowHub — Making asset management simple and efficient.*
