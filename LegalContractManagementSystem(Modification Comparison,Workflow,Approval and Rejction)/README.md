# Modification Comparison, Workflow, Approval & Rejection

This task implements the complete modification request and approval workflow in the Legal Contract Management System.

## Features

- Compare the **Original Contract/Clause Value** with the **Proposed Modified Value**
- Display **Modification Reason**
- Display **Requested By** and **Request Date**
- Track modification status:
  - PENDING
  - APPROVED
  - REJECTED
- Users can create and view their modification requests
- Approver can review modification requests
- Approver can compare original and proposed values
- Approver can **Approve or Reject** requests
- Add comments during approval
- Provide a **Rejection Reason** when rejecting
- Implement **Role-Based Authorization**
- Only authorized users can approve or reject modifications

## Workflow

**USER**

↓ Creates Modification Request

↓ Status = PENDING

**APPROVER**

↓ Reviews Request

↓ Compares Original vs Proposed Value

↙️　　　　　　　　　↘️

**APPROVE**　　　　 **REJECT**

↓　　　　　　　　　　 ↓

Status = APPROVED　 Status = REJECTED

↓　　　　　　　　　　 ↓

Add Comment　　　　 Add Rejection Reason

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

- `ModificationDashboard.html`
- `ModificationController.java`
- `ModificationService.java`
- `Modification.java`
- `ModificationRepository.java`
- `ApprovalDashboard.html`
- `ApprovalController.java`
- `ApprovalService.java`
- `Approval.java`
- `ApprovalRepository.java`
