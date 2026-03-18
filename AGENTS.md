# BorrowHub Project Guide (Monorepo)

**GitHub Repository:** [99lash/BorrowHub](https://github.com/99lash/BorrowHub)

This is the central guide for the **BorrowHub** project. This repository is structured as a **monorepo**, containing the Android mobile application, the Laravel backend API, and a functional prototype.

## Repository Structure

- **`mobile-app/`**: Android application (Java, MVVM + Repository Pattern, Room).
  - Use `mobile-app/GEMINI.md` for specific app guidance.
  - Architecture: `mobile-app/docs/ARCHITECTURE.md`.
- **`backend-api/`**: Laravel Backend API (MVC + Service-Repository Pattern, MySQL, Laravel Sanctum).
  - Architecture: `backend-api/docs/ARCHITECTURE.md`.
- **`borrowhub-prototype/`**: Web-based Prototype (React, Vite, Tailwind CSS).
  - Use `borrowhub-prototype/GEMINI.md` for design/UI guidance.
- **`docs/`**: Shared documentation.
- **`.github/`**: Repository-wide Issue and PR templates.

## Architectural Context

BorrowHub follows a **Network-First (with Local Caching)** approach:
1.  **Mobile App:** Built with **Java** using **MVVM + Repository Pattern**. Uses **Retrofit** to communicate with the API and **Room** for local caching.
2.  **Backend:** Built with **Laravel** using **MVC + Service-Repository Pattern**. Uses **MySQL** for persistence and **Laravel Sanctum** for secure API authentication.

## Instructions for Gemini CLI

- **Working on Mobile:** Focus on the `mobile-app/` directory. Ensure the `Repository` and `ApiService` layers match the backend definitions.
- **Working on Backend:** Focus on the `backend-api/` directory. Adhere strictly to the **Service-Repository Pattern** (do not write business logic or DB queries in controllers). Always use Form Requests for validation and API Resources for JSON responses. Ensure API versioning is applied (e.g., prefixing routes with `/api/v1/`).
- **Cross-Project Changes:** When adding a new feature (e.g., "Add Item"), implement the versioned API endpoint (e.g., `/api/v1/...`) first, then the mobile integration.
- **Design Reference:** Refer to `borrowhub-prototype/` for UI/UX specifications when implementing mobile screens.

## Development Workflow

1.  **Issue Creation:** Use the templates in `.github/ISSUE_TEMPLATE/`.
2.  **Pull Requests:** Use the template in `.github/PULL_REQUEST_TEMPLATE.md`.
3.  **Code Standards:** Refer to `docs/CONTRIBUTING.md` for shared coding conventions.

---
*BorrowHub — Making asset management simple and efficient.*
