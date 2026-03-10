# Entity Relationship Diagram — BorrowHub API

This diagram visualizes the relational structure of the BorrowHub database.

```mermaid
erDiagram
    USERS ||--o{ BORROW_RECORDS : processes
    USERS ||--o{ ACTIVITY_LOGS : performs
    
    COURSES ||--o{ STUDENTS : contains
    
    STUDENTS ||--o{ BORROW_RECORDS : initiates
    
    CATEGORIES ||--o{ ITEMS : classifies
    
    BORROW_RECORDS ||--|{ BORROW_RECORD_ITEMS : includes
    ITEMS ||--o{ BORROW_RECORD_ITEMS : "is part of"

    USERS {
        bigint id PK
        string name
        string username
        string password
        enum role
    }

    STUDENTS {
        bigint id PK
        string student_number UK
        string name
        bigint course_id FK
    }

    COURSES {
        bigint id PK
        string name
    }

    CATEGORIES {
        bigint id PK
        string name
    }

    ITEMS {
        bigint id PK
        bigint category_id FK
        string name
        int total_quantity
        int available_quantity
        enum status
    }

    BORROW_RECORDS {
        bigint id PK
        bigint student_id FK
        bigint staff_id FK
        string collateral
        datetime borrowed_at
        datetime due_at
        datetime returned_at
        enum status
    }

    BORROW_RECORD_ITEMS {
        bigint id PK
        bigint borrow_record_id FK
        bigint item_id FK
        int quantity
    }

    ACTIVITY_LOGS {
        bigint id PK
        bigint actor_id FK
        string performed_by
        string target_user_id
        string target_user_name
        string action
        text details
        enum type
    }
```

## Relationship Key
- **One-to-Many (`||--o{`)**:
    - A **User** can process many **Borrow Records**.
    - A **Student** can have many **Borrow Records**.
    - A **Category** can contain many **Items**.
- **Many-to-Many (`||--|{` via Pivot)**:
    - **Borrow Records** and **Items** are connected through the `borrow_record_items` pivot table, allowing a single transaction to include multiple pieces of equipment.
