# FinWise API Endpoints

This document provides a comprehensive list of all API endpoints available in the FinWise backend application.

## Overview

The FinWise backend API is organized into the following main functional areas:

1. **User Management** - Endpoints for user authentication, registration, and profile management
2. **Family Profile Management** - Endpoints for managing family profiles
3. **Marriage Plan Management** - Endpoints for managing marriage plans
4. **Child Management** - Endpoints for managing children information
5. **Education Plan Management** - Endpoints for managing education plans
6. **Economic Indicator Management** - Endpoints for managing economic indicators
7. **Education Cost Reference Management** - Endpoints for managing education cost references
8. **Financial Transaction Management** - Endpoints for managing financial transactions
9. **Investment Management** - Endpoints for managing investments
10. **Investment Option Management** - Endpoints for managing investment options
11. **Marriage Cost Reference Management** - Endpoints for managing marriage cost references
12. **Marriage Expense Category Management** - Endpoints for managing marriage expense categories
13. **Notification Management** - Endpoints for managing notifications
14. **Report Management** - Endpoints for managing reports
15. **Savings Plan Management** - Endpoints for managing savings plans

All API endpoints follow RESTful conventions and return JSON responses. Most endpoints require authentication.

## User Management

### UserController (`/api`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/user` | Get currently authenticated user |
| GET | `/api/user/{id}` | Get user by ID |
| POST | `/api/user` | Create a new user |
| GET | `/api/auth/user` | Get current authenticated user with OAuth2 details |
| POST | `/api/auth/logout` | Logout the current user |

## Family Profile Management

### FamilyProfileController (`/api/familyProfile`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/familyProfile/{id}` | Get family profile by ID |
| POST | `/api/familyProfile` | Create a new family profile |
| PUT | `/api/familyProfile/{id}` | Update a family profile |
| DELETE | `/api/familyProfile/{id}` | Delete a family profile |
| PATCH | `/api/familyProfile/{id}` | Partially update a family profile |
| POST | `/api/familyProfile/assign-user/{familyprofileid}/familyprofile{userid}` | Assign a user to a family profile |
| GET | `/api/familyProfile` | Get family profile by current user ID |

## Marriage Plan Management

### MarriagePlanController (`/api/marriage-plans`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/marriage-plans/{familyProfileId}` | Create a new marriage plan for a family profile |
| GET | `/api/marriage-plans/{id}` | Get a marriage plan by ID |
| GET | `/api/marriage-plans/family/{familyProfileId}` | Get all marriage plans for a family profile |
| PUT | `/api/marriage-plans/{id}` | Update a marriage plan |
| DELETE | `/api/marriage-plans/{id}` | Delete a marriage plan |
| PATCH | `/api/marriage-plans/{id}` | Partially update a marriage plan |

## Child Management

### ChildController (`/api/children`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/children` | Create a new child |
| GET | `/api/children` | Get all children |
| GET | `/api/children/{id}` | Get a child by ID |
| PUT | `/api/children/{id}` | Update a child |
| DELETE | `/api/children/{id}` | Delete a child |
| PATCH | `/api/children/{id}` | Partially update a child |

## Education Plan Management

### EducationPlanController (`/api/education-plans`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/education-plans/{familyProfileId}` | Create a new education plan for a family profile |
| GET | `/api/education-plans/{id}` | Get an education plan by ID |
| GET | `/api/education-plans/child/{childId}` | Get all education plans for a child |
| PUT | `/api/education-plans/{id}` | Update an education plan |
| DELETE | `/api/education-plans/{id}` | Delete an education plan |
| PATCH | `/api/education-plans/{id}` | Partially update an education plan |

## Economic Indicator Management

### EconomicIndicatorController (`/api/economic-indicators`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/economic-indicators` | Create a new economic indicator |
| GET | `/api/economic-indicators/{id}` | Get an economic indicator by ID |
| GET | `/api/economic-indicators` | Get all economic indicators |
| PUT | `/api/economic-indicators/{id}` | Update an economic indicator |
| DELETE | `/api/economic-indicators/{id}` | Delete an economic indicator |
| PATCH | `/api/economic-indicators/{id}` | Partially update an economic indicator |

## Education Cost Reference Management

### EducationCostReferenceController (`/api/education-cost-references`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/education-cost-references` | Create a new education cost reference |
| GET | `/api/education-cost-references/{id}` | Get an education cost reference by ID |
| GET | `/api/education-cost-references` | Get all education cost references |
| GET | `/api/education-cost-references/location/{location}` | Get education cost references by location |
| PUT | `/api/education-cost-references/{id}` | Update an education cost reference |
| DELETE | `/api/education-cost-references/{id}` | Delete an education cost reference |
| PATCH | `/api/education-cost-references/{id}` | Partially update an education cost reference |

## Financial Transaction Management

### FinancialTransactionController (`/api/financial-transactions`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/financial-transactions/{familyProfileId}` | Create a new financial transaction for a family profile |
| GET | `/api/financial-transactions/{id}` | Get a financial transaction by ID |
| GET | `/api/financial-transactions/family/{familyProfileId}` | Get all financial transactions for a family profile |
| GET | `/api/financial-transactions/family/{familyProfileId}/recent` | Get recent financial transactions for a family profile |
| PUT | `/api/financial-transactions/{id}` | Update a financial transaction |
| DELETE | `/api/financial-transactions/{id}` | Delete a financial transaction |
| PATCH | `/api/financial-transactions/{id}` | Partially update a financial transaction |

## Investment Management

### InvestmentController (`/api/investments`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/investments/{familyProfileId}` | Create a new investment for a family profile |
| GET | `/api/investments/{id}` | Get an investment by ID |
| GET | `/api/investments/family/{familyProfileId}` | Get all investments for a family profile |
| GET | `/api/investments/savings-plan/{savingsPlanId}` | Get investments by savings plan ID |
| PUT | `/api/investments/{id}` | Update an investment |
| DELETE | `/api/investments/{id}` | Delete an investment |
| PATCH | `/api/investments/{id}` | Partially update an investment |

## Investment Option Management

### InvestmentOptionController (`/api/investment-options`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/investment-options` | Create a new investment option |
| GET | `/api/investment-options/{id}` | Get an investment option by ID |
| GET | `/api/investment-options` | Get all investment options |
| GET | `/api/investment-options/risk-level/{riskLevel}` | Get investment options by risk level |
| PUT | `/api/investment-options/{id}` | Update an investment option |
| DELETE | `/api/investment-options/{id}` | Delete an investment option |
| PATCH | `/api/investment-options/{id}` | Partially update an investment option |

## Marriage Cost Reference Management

### MarriageCostReferenceController (`/api/marriage-cost-references`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/marriage-cost-references` | Create a new marriage cost reference |
| GET | `/api/marriage-cost-references/{id}` | Get a marriage cost reference by ID |
| GET | `/api/marriage-cost-references` | Get all marriage cost references |
| GET | `/api/marriage-cost-references/location/{location}` | Get marriage cost references by location |
| PUT | `/api/marriage-cost-references/{id}` | Update a marriage cost reference |
| DELETE | `/api/marriage-cost-references/{id}` | Delete a marriage cost reference |
| PATCH | `/api/marriage-cost-references/{id}` | Partially update a marriage cost reference |

## Marriage Expense Category Management

### MarriageExpenseCategoryController (`/api/marriage-expense-categories`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/marriage-expense-categories/{marriagePlanId}` | Create a new marriage expense category for a marriage plan |
| GET | `/api/marriage-expense-categories/{id}` | Get a marriage expense category by ID |
| GET | `/api/marriage-expense-categories/marriage-plan/{marriagePlanId}` | Get all marriage expense categories for a marriage plan |
| PUT | `/api/marriage-expense-categories/{id}` | Update a marriage expense category |
| DELETE | `/api/marriage-expense-categories/{id}` | Delete a marriage expense category |
| PATCH | `/api/marriage-expense-categories/{id}` | Partially update a marriage expense category |

## Notification Management

### NotificationController (`/api/notifications`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/notifications/user/{userId}` | Get all notifications for a user |
| POST | `/api/notifications` | Create a new notification |
| PATCH | `/api/notifications/{id}/read` | Mark a notification as read |

## Report Management

### ReportController (`/api/reports`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/reports/{familyProfileId}` | Create a new report for a family profile |
| GET | `/api/reports/{id}` | Get a report by ID |
| GET | `/api/reports/family/{familyProfileId}` | Get all reports for a family profile |
| GET | `/api/reports/family/{familyProfileId}/type/{reportType}` | Get reports by type for a family profile |
| PUT | `/api/reports/{id}` | Update a report |
| DELETE | `/api/reports/{id}` | Delete a report |
| PATCH | `/api/reports/{id}` | Partially update a report |

## Savings Plan Management

### SavingsPlanController (`/api/savings-plans`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/savings-plans/{familyProfileId}` | Create a new savings plan for a family profile |
| GET | `/api/savings-plans/{id}` | Get a savings plan by ID |
| GET | `/api/savings-plans/family/{familyProfileId}` | Get all savings plans for a family profile |
| PUT | `/api/savings-plans/{id}` | Update a savings plan |
| DELETE | `/api/savings-plans/{id}` | Delete a savings plan |
| PATCH | `/api/savings-plans/{id}` | Partially update a savings plan |
