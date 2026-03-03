# BorrowHub Monorepo

Welcome to the **BorrowHub** project! This is a monorepo that contains both the mobile application and the backend service.

## Project Structure

- **[mobile-app/](./mobile-app)**: Android Mobile Application (Java, Room, MVVM).
- **[backend-api/](./backend-api)**: Laravel Backend Service (PHP, MySQL, Service-Repository Pattern).
- **[docs/](./docs)**: Shared project documentation and contribution guidelines.

## Getting Started

### Mobile Application
For instructions on building and running the Android app, see [mobile-app/README.md](./mobile-app/README.md).

### Backend Service
The backend service is built using Laravel and MySQL. Instructions for setup and deployment can be found in [backend-api/README.md](./backend-api/README.md).

## Shared Resources

- **[CONTRIBUTING.md](./docs/CONTRIBUTING.md)**: Guidelines for contributing to either project.
- **[.github/](./ .github)**: Standard Issue and Pull Request templates for the entire repository.

## Architecture

BorrowHub uses a **Network-First (with Local Cache)** architecture. The mobile app communicates with the Laravel API to sync data with a central MySQL database, while using Room for offline support.

---
*BorrowHub — Making asset management simple and efficient.*
