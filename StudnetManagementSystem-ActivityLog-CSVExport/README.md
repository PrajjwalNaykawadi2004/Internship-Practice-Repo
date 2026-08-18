Student Management System – Activity Log & CSV Export Module

This project is a Student Management System developed using Java Spring Boot, Spring Security, REST APIs, SQL Server, HTML, CSS and JavaScript.

The Activity Log & CSV Export Module includes the following features:

• Audit Log Database Table
  - Stores user activities and system actions.
  - Maintains details such as username, action, entity, entity ID, description, timestamp and request information.

• Automatic Activity Logging
  - Login
  - Logout
  - Registration
  - Create
  - Update
  - Delete
  - Password Change

• Audit Log REST API
  - API to fetch activity logs.
  - Supports retrieving and managing audit log data through REST endpoints.

• Role-Based Access Control
  - Restricts Activity Logs access based on user roles.
  - Admin users can access activity log information according to the configured permissions.

• Activity Logs Frontend
  - Dedicated Activity Logs page.
  - Displays all activities in a structured table.
  - Shows user, action, entity, description and date/time information.

• Search and Filters
  - Search activity logs.
  - Filter logs based on relevant criteria such as action, user or date.

• Pagination
  - Displays activity logs page by page.
  - Improves performance and usability when a large number of logs are available.

• CSV Export
  - Allows users to export activity logs into a CSV file.
  - Exported CSV contains only the currently filtered log data.

• Exception Handling and Validation
  - Handles API errors properly.
  - Validates input data and provides appropriate error responses.

• API Testing
  - All Activity Log APIs are tested using Postman.

Technologies Used:
Java, Spring Boot, Spring Security, REST API, SQL Server, HTML, CSS, JavaScript and Postman.
