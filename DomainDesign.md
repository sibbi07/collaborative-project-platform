# Domain Design

## User

### Purpose
Represents a person using the study platform.

### Attributes
- id
- username
- email
- password

### Relationships
- A User can belong to multiple Projects through Membership.
- A User can be assigned multiple Tasks.
- A User can have multiple Memberships.

### Rules
- A User's email should be unique.
- A User's username should be unique.

---

## Project

### Purpose
Represents a study project that users collaborate on and that contains Tasks.

### Attributes
- id
- name
- description
- deadline
- createdAt
- updatedAt

### Relationships
- A Project can have multiple Users through Membership.
- A Project contains multiple Tasks.
- A Project has exactly one owner, who is also a member.

### Rules
- A Project must have at least one member.
- A Project must have exactly one owner.
- Every Task belonging to a Project must belong to exactly one Project.

---

## Task

### Purpose
Represents an individual piece of work within a Project.

### Attributes
- id
- title
- status
- deadline
- createdAt
- updatedAt

### Relationships
- A Task belongs to exactly one Project.
- A Task can be assigned to zero or one User.

### Rules
- A Task must belong to a Project.
- A Task can have at most one assignee.
- The title must not be blank.
- The title must not exceed 100 characters.

---

## Membership

### Purpose
Represents a User's membership in a Project.

Membership is used to represent the many-to-many relationship between Users and Projects and allows information to be stored about that relationship.

### Attributes
- id
- role
- joinedAt

### Relationships
- A Membership belongs to exactly one User.
- A Membership belongs to exactly one Project.

### Rules
- A User can have multiple Memberships.
- A Project can have multiple Memberships.
- A Project must have at least one member.
- One Membership for a Project represents the owner.

---

# Domain Model

## Entities

### User
- id
- username
- email
- password

### Project
- id
- name
- description
- deadline
- createdAt
- updatedAt

### Task
- id
- title
- status
- deadline
- createdAt
- updatedAt

### Membership
- id
- role
- joinedAt

---

## Relationships

### User ↔ Project
Many-to-many relationship through Membership.

- One User can belong to many Projects.
- One Project can have many Users.
- Membership represents the relationship between them.
- Membership stores the User's role within the Project.

### Project → Task
One-to-many relationship.

- One Project can contain many Tasks.
- Every Task belongs to exactly one Project.

### User → Task
One-to-many relationship from the User's perspective.

- One User can be assigned many Tasks.
- A Task can be assigned to zero or one User.

### Membership → User
Many-to-one relationship.

- Many Memberships can belong to one User.
- Each Membership belongs to exactly one User.

### Membership → Project
Many-to-one relationship.

- Many Memberships can belong to one Project.
- Each Membership belongs to exactly one Project.

---

# Domain Rules

1. Every Project must have at least one member.
2. Every Project must have exactly one owner.
3. The Project owner must also be a Project member.
4. Every Task must belong to exactly one Project.
5. A Task can have zero or one assignee.
6. A User can belong to multiple Projects.
7. A Project can have multiple Users.
8. Task titles cannot be blank.
9. Task titles cannot exceed 100 characters.

---

# Out of Scope for Initial Implementation

The following are potential future features but are not part of the initial domain model:

- Teams
- Comments
- Notifications
- File uploads
- Study Sessions
- Advanced authentication and security features
