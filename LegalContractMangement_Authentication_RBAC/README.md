# Legal Contract Management System – User Authentication and Role-Based Authorization

## Task: Implement Complete User Authentication and Role-Based Authorization

Implemented secure user authentication and Role-Based Access Control (RBAC) for the Legal Contract Management System.

### User Authentication

* Users can securely log in using their credentials.
* Passwords are encrypted using BCrypt Password Encoder.
* Passwords are not stored in plain text in the database.
* Authentication is required before accessing protected features and APIs.

### Role-Based Access Control (RBAC)

Implemented role-based authorization to control access according to the user's assigned role.

Roles can access features based on their permissions.

#### ADMIN

* Manage Users
* Manage Roles
* Manage Contracts
* View Approvals
* View Audit Logs

#### USER

* Access assigned contracts
* View documents
* Perform authorized contract-related operations

### Login Screen

Created a login screen where users can enter their credentials and securely access the system.

### Dashboard

Created a basic dashboard that is displayed after successful authentication.

The dashboard provides role-based navigation and shows features according to the logged-in user's role.

### Logout Functionality

Implemented logout functionality to securely end the user session.

After logout:

* Authentication credentials are removed.
* The user is redirected to the Login screen.
* Protected pages cannot be accessed without logging in again.

### Protected APIs and Features

* Protected APIs require proper authentication.
* Unauthorized users cannot access restricted resources.
* Role-based authorization prevents users from accessing features that are not assigned to their role.

### Technologies Used

* Java
* Spring Boot
* Spring Security
* Spring Data JPA / Hibernate
* PostgreSQL
* BCrypt Password Encoder
* HTML
* CSS
* JavaScript

## Task Status

**Completed – Secure user authentication, password encryption, Role-Based Access Control (RBAC), login screen, dashboard, logout functionality, role-based navigation, and protected APIs have been implemented successfully.**
