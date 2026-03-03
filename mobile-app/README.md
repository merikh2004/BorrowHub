# BorrowHub
### Borrowing Management System — Computer Studies Department

> An Android application designed to help **University MIS and CSD Staff** efficiently manage and monitor borrowing transactions within the Computer Studies Department.

---

## About the App

**BorrowHub** is a mobile-based Borrowing Management System built for university use. It streamlines the process of lending and tracking equipment, devices, and other assets managed by the Computer Studies Department (CSD), with oversight from the Management Information System (MIS) staff.

---

## Features

- **Borrow Request Management** — Submit, review, and approve borrowing requests
- **Item Tracking** — Monitor the status and availability of borrowable items in real time
- **Return Monitoring** — Track return dates and flag overdue items
- **Borrower Records** — Maintain a log of all borrowers and transaction history
- **Item Inventory** — Manage the list of available CSD assets and equipment
- **Notifications & Alerts** — Receive reminders for upcoming and overdue returns
- **Reports & Logs** — View and export borrowing history and summaries
- **Role-Based Access** — Separate access levels for MIS Staff and CSD Staff

---

## Target Users

| Role | Description |
|---|---|
| **Admin** | System administrators who oversee the borrowing system and manage overall records |
| **CSD/MIS Staff** | Department staff who process borrow requests and manage CSD equipment |

---

## Tech Stack

| Component | Technology |
|---|---|
| Platform | Android (Mobile) |
| Architecture | MVVM (Model-View-ViewModel) |
| Language | Java |
| Database | Room (SQLite) |
| UI Framework | XML Layouts |
| Jetpack Components | ViewModel, LiveData, Room, View Binding, Navigation |

---

## Architecture

BorrowHub uses **MVVM** with a Repository pattern where the **Entity acts as the single Domain Model** across all layers to simplify data flow.

```
+--------------------------------------------------+
|                      VIEW                        |
|        (Activities / Fragments / XML)            |
|   Observes LiveData . Sends user events          |
+--------------------+-----------------------------+
                     |  observes / calls
+--------------------v-----------------------------+
|                  VIEWMODEL                       |
|     Holds UI state . Processes business logic    |
|     Exposes LiveData . Survives config changes   |
+--------------------+-----------------------------+
                     |  requests data
+--------------------v-----------------------------+
|                 REPOSITORY                       |
|     Single source of truth for data             |
|     Returns LiveData<Entity> directly           |
+--------------------+-----------------------------+
                     |  queries
+--------------------v-----------------------------+
|              DATA LAYER (Room)                   |
|   DAO interfaces . Entities . AppDatabase        |
+--------------------------------------------------+
```

For the complete architecture breakdown including DAO design, data flow diagrams, package structure, and layer responsibilities, see [ARCHITECTURE.md](./docs/ARCHITECTURE.md).

---

## Getting Started

### Prerequisites

- Android Studio (latest stable version)
- Android SDK (API Level 26 or higher)
- Java Development Kit (JDK 11+)
- Git

### Setup & Installation

1. **Fork the repository**
   - Fork the repository to your own GitHub account.

2. **Clone your fork locally**:
   ```bash
    git clone https://github.com/your-username/BorrowHub.git
    cd BorrowHub
   ```
3. Set the upstream remote:
   ```bash
    git remote add upstream https://github.com/original-repo-owner/BorrowHub.git
   ```

4. **Open in Android Studio**
   - Launch Android Studio
   - Select **Open an Existing Project**
   - Navigate to the cloned folder

5. **Configure dependencies**
   - Sync Gradle files
   - Ensure all required SDKs are installed

6. **Run the application**
   - Connect an Android device or start an emulator
   - Click **Run** or press `Shift + F10`

> Make sure the project builds and runs successfully before making any changes.

---

## User Roles & Permissions

### Admin
- Oversight of the entire borrowing system
- Management of all system records and logs
- Configuration of global application settings

### CSD/MIS Staff
- Process and approve borrow requests
- Manage CSD equipment inventory and status
- Monitor active borrowings and return dates
- View and generate transaction reports

---

## Documentation

- [ARCHITECTURE.md](./docs/ARCHITECTURE.md) — Full MVVM architecture guide, DAO breakdown, data flow diagrams, and layer responsibilities
- [CONTRIBUTING.md](../docs/CONTRIBUTING.md) — Shared development workflow and coding standards
- [PULL_REQUEST_TEMPLATE.md](../.github/PULL_REQUEST_TEMPLATE.md) — Standard template to be used when submitting Pull Requests

---

> *BorrowHub — Making asset management simple and efficient.*