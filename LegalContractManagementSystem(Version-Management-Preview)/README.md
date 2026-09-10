# Version Management

This task implements Version Management in the Legal Contract Management System to maintain the complete history of contract changes.

## Features

- Automatically create a **new contract version** when a modification request is approved
- Maintain the **current version** of the contract
- Preserve all **previous versions** of the contract
- Maintain version numbers for each contract
- Store a **change summary** for every new version
- Track the user who created the version
- Maintain the creation date and time
- Provide **Version History** to view different versions of a contract
- Allow users to view changes across different contract versions

## Version Workflow

**Contract Version 1**

↓  

Modification Request

↓

Modification Reviewed by ContractManager

↓

**Modification Approved**

↓

**New Contract Version Created**

↓

**Version 2 becomes the Current Version**

↓

Previous Version remains available in **Version History**

## Version History

The Version History interface displays:

- Version ID
- Version Number
- Contract
- Change Summary
- Status
- Created By
- Created Date

## Technologies Used

- Java
- Spring Boot
- Spring Security
- JPA / Hibernate
- PostgreSQL
- HTML
- CSS
- JavaScript

## Main Files

- `VersionDashboard.html
- `VersionController.java
- `VersionService.java
- `Version.java
- `VersionRepository.java
- `ApprovalService.java

## Expected Result
The system maintains the current contract version along with all previous versions, allowing users to view 
the complete version history and track how a contract has changed over time.

The system maintains the current contract version along with all previous versions, allowing users to view the complete version history and track how a contract has changed over time.
