# BorrowHub — MVVM Architecture Guide
### Room (SQLite) + Java Implementation

---

## Overview

BorrowHub uses the **MVVM (Model-View-ViewModel)** pattern combined with a **Repository pattern**.

For this project, we have opted for a **streamlined approach** where the **Entity Layer also acts as the Domain Model**. This means the classes mapped to our Room database (`data/local/entity/`) are the exact same classes passed up to the ViewModel and View. This minimizes boilerplate code (like Mappers) and speeds up development while maintaining a clean separation of concerns.

---

## Full Architecture Diagram

```
+---------------------------------------------------------------+
|                          UI LAYER                             |
|                                                               |
|   Activity / Fragment                                         |
|   - Observes LiveData                                         |
|   - Sends user events to ViewModel                            |
|   - Renders UI using Entity objects                           |
+----------------------------+----------------------------------+
                             |
                    calls methods on
                             |
+----------------------------v----------------------------------+
|                       VIEWMODEL LAYER                         |
|                                                               |
|   BorrowViewModel / InventoryViewModel / AuthViewModel        |
|   - Holds LiveData<T> for UI state                            |
|   - Calls Repository methods                                  |
|   - No Android Context, no DB access                          |
+----------------------------+----------------------------------+
                             |
                    calls methods on
                             |
+----------------------------v----------------------------------+
|                     REPOSITORY LAYER                          |
|                                                               |
|   BorrowRepository / ItemRepository / UserRepository          |
|   - Single source of truth                                    |
|   - Calls DAO for database operations                         |
|   - Returns LiveData<Entity> directly to ViewModel            |
+----------------------------+----------------------------------+
                             |
                    calls queries on
                             |
+----------------------------v----------------------------------+
|                       DATA LAYER                              |
|                                                               |
|   +----------------------+   +-----------------------------+  |
|   |         DAO          |   |          ENTITY             |  |
|   |                      |   |          (Also Model)       |  |
|   |  UserDao             |   |  UserEntity                 |  |
|   |  ItemDao             |   |  ItemEntity                 |  |
|   |  BorrowRecordDao     |   |  BorrowRecordEntity         |  |
|   |                      |   |                             |  |
|   |  Interfaces with     |   |  Direct mapping to          |  |
|   |  @Query annotations  |   |  Room database tables       |  |
|   +----------------------+   +-----------------------------+  |
|                                                               |
|   AppDatabase (SQLite)                                        |
+---------------------------------------------------------------+
```

---

## The Entity Layer (Database Mapping & Model)

Entities live in `data/local/entity/`. They are annotated with Room annotations and their structure matches the database table exactly. 

*Note: Even though fields like `password_hash` might exist in a `UserEntity`, the View should simply ignore rendering them. The ViewModel handles the logic of verifying them.*

```
UserEntity
+------------------+------------------+
| Field            | DB Column        |
+------------------+------------------+
| int id           | user_id (PK)     |
| String username  | username         |
| String password  | password (hashed)|
| String role      | role             |
| long createdAt   | created_at       |
+------------------+------------------+

BorrowRecordEntity
+----------------------+----------------------+
| Field                | DB Column            |
+----------------------+----------------------+
| int id               | record_id (PK)       |
| int userId           | user_id (FK)         |
| int itemId           | item_id (FK)         |
| long borrowDate      | borrow_date          |
| long expectedReturn  | expected_return_date |
| long actualReturn    | actual_return_date   |
| String status        | status               |
+----------------------+----------------------+

ItemEntity
+------------------+------------------+
| Field            | DB Column        |
+------------------+------------------+
| int id           | item_id (PK)     |
| String name      | item_name        |
| String category  | category         |
| String condition | condition        |
| boolean available| is_available     |
+------------------+------------------+
```

---

## DAO — Database Access Object

DAOs live in `data/local/dao/`. They are **interfaces** annotated with `@Dao` that define all database operations. The Repository calls these — nothing else does.

```
UserDao
+------------------------------------------+---------------------------+
| Method                                   | SQL Operation             |
+------------------------------------------+---------------------------+
| insertUser(UserEntity)                   | INSERT                    |
| getUserById(int id) : LiveData<UserEntity>| SELECT WHERE id = ?       |
| getUserByUsername(String) : UserEntity   | SELECT WHERE username = ? |
| updateUser(UserEntity)                   | UPDATE                    |
| deleteUser(UserEntity)                   | DELETE                    |
+------------------------------------------+---------------------------+

BorrowRecordDao
+---------------------------------------------------+---------------------------+
| Method                                            | SQL Operation             |
+---------------------------------------------------+---------------------------+
| insertRecord(BorrowRecordEntity)                  | INSERT                    |
| getAllRecords() : LiveData<List<BorrowRecordEntity>>| SELECT *                 |
| getRecordsByUser(int userId)                      | SELECT WHERE user_id = ?  |
| getActiveRecords()                                | SELECT WHERE status = ... |
| getOverdueRecords()                               | SELECT WHERE return < NOW |
| updateRecord(BorrowRecordEntity)                  | UPDATE                    |
| getRecordById(int id) : LiveData<BorrowRecordEntity>| SELECT WHERE id = ?      |
+---------------------------------------------------+---------------------------+
```

---

## Repository — Single Source of Truth

The Repository acts as a mediator between the DAO and the ViewModel. It ensures that the ViewModel does not directly communicate with the database.

```
Data flow for "Get Borrow Record":

ViewModel calls:
    borrowRepository.getBorrowRecord(recordId)
         |
         v
Repository calls:
    borrowRecordDao.getRecordById(recordId)
         |
         v
Repository returns:
    LiveData<BorrowRecordEntity> directly to ViewModel
         |
         v
ViewModel exposes:
    LiveData<BorrowRecordEntity> borrowRecord
         |
         v
Fragment observes and renders the screen.
```

---

## Project Structure

```
java/com/example/borrowhub/
|
|-- data/
|   |-- local/
|   |   |-- AppDatabase.java        <- RoomDatabase singleton
|   |   |-- entity/                 <- Room Entities (Also your Models)
|   |   |   |-- UserEntity.java
|   |   |   |-- ItemEntity.java
|   |   |   `-- BorrowRecordEntity.java
|   |   `-- dao/                    <- DAOs (query interfaces)
|   |       |-- UserDao.java
|   |       |-- ItemDao.java
|   |       `-- BorrowRecordDao.java
|
|-- repository/                     <- Repositories (data coordination)
|   |-- UserRepository.java
|   |-- ItemRepository.java
|   `-- BorrowRepository.java
|
|-- viewmodel/                      <- ViewModels
|   |-- AuthViewModel.java
|   |-- BorrowViewModel.java
|   `-- InventoryViewModel.java
|
|-- view/                           <- Activities & Fragments (UI layer)
|   |-- auth/
|   |   `-- LoginActivity.java
|   |-- borrow/
|   |   |-- BorrowListFragment.java
|   |   `-- BorrowDetailFragment.java
|   `-- inventory/
|       `-- InventoryFragment.java
|
|-- adapter/                        <- RecyclerView Adapters
`-- utils/
    |-- DateUtils.java
```

---

## Summary: When to Use What

| Class Type | Location | Used By | Contains |
|---|---|---|---|
| `*Entity` | `data/local/entity/` | DAO, Repository, ViewModel, View | Room annotations, DB column fields |
| `*Dao` | `data/local/dao/` | Repository only | @Query, @Insert, @Update, @Delete |
| `*Repository` | `repository/` | ViewModel only | Calls DAO |
| `*ViewModel` | `viewmodel/` | View (Activity/Fragment) | LiveData, calls Repository |

---

## Key Rules

1. The **View never touches a DAO directly** — it goes through the ViewModel -> Repository.
2. The **ViewModel never touches a DAO directly** — always goes through the Repository.
3. The **DAO returns Entities**.
4. The **Entities** are passed all the way up to the View for rendering.
5. Use `LiveData` from Room DAOs so that the UI automatically updates when the database changes.
