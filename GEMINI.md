# BorrowHub Project Guide (Monorepo)

This is the central guide for the **BorrowHub** project. This repository is structured as a **monorepo**, containing both the Android mobile application and the Laravel backend API.

## Repository Structure

- **`mobile-app/`**: Android application (Java, MVVM, Room).
  - Use `mobile-app/GEMINI.md` for specific app guidance.
  - Architecture: `mobile-app/docs/ARCHITECTURE.md`.
- **`backend-api/`**: Laravel Backend API (PHP, MySQL, Service-Repository Pattern).
  - Architecture: `backend-api/docs/ARCHITECTURE.md`.
- **`docs/`**: Shared documentation.
  - Contribution guidelines: `docs/CONTRIBUTING.md`.
- **`.github/`**: Repository-wide Issue and PR templates.

## Architectural Context

BorrowHub follows a **Network-First (with Local Caching)** approach:
1.  **Mobile App:** Uses **Retrofit** to communicate with the Laravel API.
2.  **Local Storage:** Uses **Room** in the Android app as a cache for offline support.
3.  **Backend:** Laravel manages central data persistence in **MySQL** and handles authentication/authorization.

## Instructions for Gemini CLI

- **Working on Mobile:** Focus on the `mobile-app/` directory. Ensure the `Repository` and `ApiService` layers match the backend definitions.
- **Working on Backend:** Focus on the `backend-api/` directory. Adhere strictly to the **Service-Repository Pattern** (do not write business logic or DB queries in controllers). Always use Form Requests for validation and API Resources for JSON responses.
- **Cross-Project Changes:** When adding a new feature (e.g., "Add Item"), implement the API endpoint first, then the mobile integration.

## Development Workflow

1.  **Issue Creation:** Use the templates in `.github/ISSUE_TEMPLATE/`.
2.  **Pull Requests:** Use the template in `.github/PULL_REQUEST_TEMPLATE.md`.
3.  **Code Standards:** Refer to `docs/CONTRIBUTING.md` for shared coding conventions.

---
*BorrowHub — Making asset management simple and efficient.*
