# Contributing to BorrowHub

Thank you for your interest in contributing to **BorrowHub**. This document outlines the guidelines, standards, and workflow that all contributors are expected to follow to maintain a clean, consistent, and collaborative codebase.

---

## Table of Contents

- [Contributing to BorrowHub](#contributing-to-borrowhub)
  - [Table of Contents](#table-of-contents)
  - [Code of Conduct](#code-of-conduct)
  - [Getting Started](#getting-started)
  - [Branching Strategy](#branching-strategy)
  - [Commit Message Convention](#commit-message-convention)
    - [Types](#types)
    - [Examples](#examples)
  - [Coding Standards](#coding-standards)
    - [General](#general)
    - [Naming Conventions](#naming-conventions)
    - [XML Layout Prefix Conventions](#xml-layout-prefix-conventions)
  - [MVVM Architecture Guidelines](#mvvm-architecture-guidelines)
    - [View (Activity / Fragment)](#view-activity--fragment)
    - [ViewModel](#viewmodel)
    - [Repository](#repository)
    - [Entity](#entity)
  - [Laravel Backend Architecture Guidelines](#laravel-backend-architecture-guidelines)
    - [Controllers](#controllers)
    - [Requests (`Form Requests`)](#requests-form-requests)
    - [Services](#services)
    - [Repositories](#repositories)
  - [Pull Request Process](#pull-request-process)
  - [Reporting Issues](#reporting-issues)

---

## Code of Conduct

All contributors are expected to maintain a respectful and professional environment. Be constructive in code reviews, open to feedback, and considerate of other contributors' time and effort.

---

## Getting Started

1. Fork the repository to your own GitHub account.
2. Clone your fork locally:
   ```bash
   git clone https://github.com/your-username/borrowhub.git
   cd borrowhub
   ```
3. Set the upstream remote:
   ```bash
   git remote add upstream https://github.com/original-repo-owner/borrowhub.git
   ```
4. Open the project in Android Studio and sync Gradle dependencies.
5. Make sure the project builds and runs successfully before making any changes.

---

## Branching Strategy

BorrowHub uses the following branch naming convention:

| Branch Type | Pattern | Example |
|---|---|---|
| Feature | `feature/<short-description>` | `feature/borrow-request-form` |
| Bug Fix | `fix/<short-description>` | `fix/return-date-null-crash` |
| Improvement | `improve/<short-description>` | `improve/inventory-list-performance` |
| Hotfix | `hotfix/<short-description>` | `hotfix/login-auth-error` |

Always branch off from `master` (or the designated `dev` branch if applicable):

```bash
git checkout master
git pull upstream master
git checkout -b feature/your-feature-name
```

---

## Commit Message Convention

Use clear, descriptive commit messages following this format:

```
<type>: <short summary>
```

### Types

| Type | When to Use |
|---|---|
| `feat` | A new feature |
| `fix` | A bug fix |
| `refactor` | Code restructuring without behavior change |
| `style` | Formatting, naming, no logic change |
| `docs` | Documentation updates |
| `test` | Adding or updating tests |
| `chore` | Build process, dependency updates |

### Examples

```
feat: add borrow request approval screen
fix: resolve null pointer on item return flow
refactor: move data fetching logic to repository layer
docs: update README with architecture diagram
```

> For more detailed conventional commits, refer to **[conventionalcommits.org](https://www.conventionalcommits.org/en/v1.0.0/)**.

---

## Coding Standards

### General

- Use **Java** as the primary language.
- Keep functions short and focused — one function, one responsibility.
- Avoid hardcoded strings; use `strings.xml` for all user-facing text.
- Remove unused imports, variables, and commented-out code before submitting a PR.

### Naming Conventions

| Element | Convention | Example |
|---|---|---|
| Classes | PascalCase | `BorrowViewModel` |
| Functions / Variables | camelCase | `fetchBorrowRecords()` |
| Constants | UPPER_SNAKE_CASE | `MAX_BORROW_DAYS` |
| XML Layout Files | snake_case | `fragment_borrow_list.xml` |
| XML IDs | snake_case | `tv_item_name`, `btn_submit` |

### XML Layout Prefix Conventions

| Prefix | View Type |
|---|---|
| `tv_` | TextView |
| `btn_` | Button |
| `et_` | EditText |
| `rv_` | RecyclerView |
| `iv_` | ImageView |
| `pb_` | ProgressBar |

---

## MVVM Architecture Guidelines

BorrowHub strictly follows the **MVVM (Model-View-ViewModel)** pattern. All contributors must adhere to the following rules:

### View (Activity / Fragment)
- Observes `LiveData` from the ViewModel.
- Does **not** contain business logic or direct data access.
- Passes user interactions (clicks, inputs) to the ViewModel via method calls.

### ViewModel
- Holds and exposes UI state via `LiveData` or `StateFlow`.
- Contains presentation logic only.
- Does **not** hold references to `Context`, `Activity`, or `Fragment`.
- Calls Repository methods to request or modify data.

### Repository
- Acts as the single source of truth for all data.
- Decides whether to use local (Room/SQLite) or remote (Firebase/API) data.
- Does **not** contain UI-related logic.

### Entity
- Annotated data classes representing database entities (e.g., `BorrowRecord`, `Item`, `User`).
- Also acts as a model.

> Do not bypass the architecture by calling data sources directly from the View or ViewModel. Always route through the Repository.

---

## Laravel Backend Architecture Guidelines

The Laravel API strictly follows the **Service-Repository Pattern** on top of MVC to maintain clean controllers and separated logic:

### Controllers
- Act only as traffic directors.
- Do **not** contain any business logic or database queries (`Model::where()`).
- Always return standard JSON responses via API Resources.

### Requests (`Form Requests`)
- Handle all input validation. Validation must not occur inside the Controller.

### Services
- Contain all business rules and domain logic.
- Throw custom `Exceptions` if logic constraints fail.

### Repositories
- Act as the only layer that interacts with the database (Eloquent Models).
- Keep queries isolated and easy to mock for testing.

> Read the full backend architectural setup at [`backend-api/docs/ARCHITECTURE.md`](../backend-api/docs/ARCHITECTURE.md).

---

## Pull Request Process

1. Ensure your branch is up to date with the base branch before opening a PR:
   ```bash
   git fetch upstream
   git rebase upstream/master
   ```

2. Make sure the project **builds without errors** and all existing functionality is working.

3. Open a Pull Request against the `master` (or `dev`) branch and use our **[Pull Request Template](PR_TEMPLATE.md)**. Ensure you include:
   - A clear and descriptive PR title
   - A summary of the changes made
   - Reference to the related issue (if applicable), e.g., `Closes #12`

4. Request a review from at least one team member.

5. Address all review comments before the PR is merged.

6. Do **not** merge your own Pull Request without approval.

---

## Reporting Issues

When reporting a bug or requesting a feature, please include:

- A clear title and description
- Steps to reproduce (for bugs)
- Expected vs. actual behavior
- Screenshots or logs if applicable
- Device model and Android version (for bugs)

Use the GitHub Issues tab to submit reports.

---

For any questions about contributing, reach out to the project maintainers.