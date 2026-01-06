# 📘 URL Shortener – API Documentation

## 0. How to Read This Document

This document is organized to help different readers quickly find what they need.

### Intended Audience

* **Frontend Developers** – Endpoint definitions, request/response examples, error scenarios
* **Backend Developers** – Authorization rules, ownership checks, data contracts
* **Reviewers / Interviewers** – Authentication flow, redirection logic, verdict handling

### What This Document Covers

* Public and protected REST APIs
* Authentication and authorization rules
* Request and response formats
* Error handling conventions
* URL safety verdict behavior

### API Grouping Convention

1. **Auth APIs** – User registration and login
2. **URL Management APIs** – Create and manage short URLs
3. **Redirection & Tracking APIs** – Hash-based redirection and analytics capture
4. **Analytics APIs** – Reporting and insights

---

## 1. Overview

This document describes the REST APIs exposed by the **URL Shortener** backend service. These APIs allow authenticated users to create and manage short URLs, while enabling secure redirection and click analytics tracking.

---

## 2. Base Information

### Base URL

```
http://localhost:8080
```

### Content Type

All APIs consume and produce JSON unless stated otherwise.

```
Content-Type: application/json
```

---

## 3. Authentication & Authorization

### Authentication Mechanism

* Uses **JWT (JSON Web Token)** for stateless authentication
* Token is issued after successful login
* Token validity: **10 minutes**

### Authorization Header

All protected APIs must include the JWT token:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 4. Standard Error Response Format

```json
{
  "timestamp": "2026-01-06T12:45:30",
  "status": 404,
  "message": "Resource not found"
}
```

---

## 5. Auth APIs

### 5.1 Generate OTP

Used to send an OTP to the user's email for signup verification.

**Endpoint**

```
POST /verify/generate-otp
```

**Authentication**: Not required

**Request Body**

```json
{
  "entityEmail": "ankit@example.com",
  "entityName": "Ankit"
}
```

**Success Response (200 OK)**

```json
{
  "data": "OTP sent successfully to the registered email",
  "message": "SUCCESS",
  "timestamp": "2026-01-06T12:30:00"
}
```

---

### 5.2 User Signup

Registers a new user. Requires a valid OTP sent via the Generate OTP endpoint.

**Endpoint**

```
POST /auth/signup
```

**Authentication**: Not required

**Request Body**

```json
{
  "username": "ankit",
  "email": "ankit@example.com",
  "password": "strongPassword123",
  "otp": "483921"
}
```

**Success Response (201 Created)**

```json
{
  "data": "User registered successfully",
  "message": "SUCCESS",
  "timestamp": "2026-01-06T12:35:00"
}
```

---

### 5.3 User Login

Authenticates a user and returns a JWT token.

**Endpoint**

```
POST /auth/login
```

**Authentication**: Not required

**Request Body**

```json
{
  "username": "ankit",
  "password": "strongPassword123"
}
```

**Success Response (200 OK)**

```json
{
  "data": "<JWT_TOKEN>",
  "message": "Login successful",
  "timestamp": "2026-01-06T12:40:00"
}
```

---

## 6. URL Management APIs

🔐 **Security**: All endpoints in this section require a valid JWT token.

---

### 6.1 Create Short URL

Creates a shortened URL and associates it with the authenticated user.

**Endpoint**

```
POST /link/create
```

**Authentication**: Required

**Request Body**

```json
{
  "actualUrl": "https://example.com/very/long/url"
}
```

**Success Response (200 OK)**

```json
{
  "message": "Short link created successfully",
  "status": "SUCCESS",
  "shortUrl": "https://sho.rt/aZ91Qe"
}
```

---

### 6.2 Get All Links of Logged-in User

Retrieves all links owned by the authenticated user.

**Endpoint**

```
GET /link/get-all-links
```

**Authentication**: Required

**Query Parameters**

* `type` (optional, default: all) – Filter links by type or status

**Success Response (200 OK)**

```json
{
  "data": [
    {
      "id": "1024",
      "actualUrl": "https://example.com",
      "hashedKey": "aZ91Qe",
      "status": "SAFE",
      "clickCnt": 12,
      "reportCnt": 0,
      "creationTime": "2026-01-05T10:15:30Z"
    }
  ],
  "message": "Links fetched successfully",
  "timestamp": "2026-01-06T21:45:00Z"
}
```

---

### 6.3 Get Single Link by ID or Hash

**Endpoint**

```
GET /link/get-link
```

**Authentication**: Required

**Query Parameters**

* `by` – id or hash
* `value` – Corresponding identifier

**Success Response (200 OK)**

```json
{
  "id": "1024",
  "actualUrl": "https://example.com",
  "hashedKey": "aZ91Qe",
  "status": "SAFE",
  "clickCnt": 12,
  "reportCnt": 0,
  "creationTime": "2026-01-05T10:15:30Z"
}
```

---

### 6.4 Delete All Links of Logged-in User

**Endpoint**

```
DELETE /link/delete-all-links
```

**Authentication**: Required

**Query Parameters**

* `type` (optional, default: all) – Filter which links to delete

**Success Response (200 OK)**

```
All links deleted successfully
```

---

### 6.5 Delete Link by ID or Hash

**Endpoint**

```
DELETE /link/delete-link
```

**Authentication**: Required

**Query Parameters**

* `by` – id or hash
* `value` – Corresponding identifier

**Success Response (200 OK)**

```json
{
  "id": 1024,
  "actualUrl": "https://example.com",
  "hashedKey": "aZ91Qe",
  "ownerUserName": "vishal",
  "status": "SAFE",
  "clickCount": 12,
  "reportCount": 0
}
```

---

## 7. Redirection & Tracking APIs

These APIs handle the public-facing redirection logic and safety checks.

* Hash-based redirection: `/{hash}`
* Safety verdict enforcement: SAFE / SUSPICIOUS / MALICIOUS
* Analytics capture: Automatic click logging upon redirection

---

## 7.1 Analytics – Click Tracking API

This API is used to capture **client-side analytics data** when a user clicks on a shortened URL. It is triggered automatically from an intermediate HTML page (`track.html`) before redirecting the user to the original long URL.

### Purpose

* Capture device and browser metadata
* Enable click analytics without delaying redirection
* Send analytics data asynchronously for further processing

---

### Endpoint

```
POST /api/track
```

### Authentication

Not required (public endpoint)

> This endpoint is intentionally kept public because it is triggered during redirection. No sensitive user data is accepted.

---

### Request Body – TrackPayloadDto

```json
{
  "shortHash": "aZ91Qe",
  "screenWidth": 1920,
  "viewportWidth": 1200,
  "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
  "timezone": "Asia/Kolkata"
}
```

### Field Description

| Field         | Description                                              |
| ------------- | -------------------------------------------------------- |
| shortHash     | Identifies which short URL was clicked                   |
| screenWidth   | Used to classify device type (mobile / tablet / desktop) |
| viewportWidth | Actual browser viewport width                            |
| userAgent     | Browser and operating system details                     |
| timezone      | User's local timezone (approximate location signal)      |

---

### Response

This endpoint does not return a response body.

```
204 No Content
```

---

### Processing Behavior

* The request is handled in a **fire-and-forget** manner
* Analytics data is forwarded asynchronously (Kafka)
* Redirection is **not blocked** even if analytics processing fails

---

### Notes

* Triggered only via `track.html`
* Uses `fetch(..., keepalive: true)` to ensure delivery during page unload
* Designed for high throughput and low latency

---

## 8. Sequence Diagrams (Text-Based)

This section describes the **end-to-end request flow** of the system in a step-by-step manner. These textual sequence diagrams help understand how different components interact during redirection and analytics capture.

---

### 8.1 Normal Redirection Flow (SAFE Verdict)

```
1. User clicks the short URL
2. Browser sends GET /url.shortener/{hash}
3. RedirectionController validates hash and fetches verdict
4. Verdict = SAFE
5. Backend returns track.html
6. Browser collects device & browser metadata
7. Browser sends POST /api/track (fire-and-forget)
8. Backend forwards analytics data to Kafka
9. Browser redirects user to the original long URL
```

---

### 8.2 Suspicious / Unverified / Pending Reverification Flow

```
1. User clicks the short URL
2. Browser sends GET /url.shortener/{hash}
3. Backend evaluates verdict
4. Verdict = SUSPICIOUS / UNVERIFIED / PENDING_REVERIFICATION
5. Backend returns suspicious-warning.html
6. User explicitly clicks "Proceed Anyway"
7. Browser sends GET /api/confirm/{shortCode}
8. Backend revalidates the link
9. Backend returns track.html
10. Browser sends POST /api/track
11. Backend forwards analytics data to Kafka
12. Browser redirects user to the long URL
```

---

### 8.3 Malicious Link Flow

```
1. User clicks the short URL
2. Browser sends GET /url.shortener/{hash}
3. Backend evaluates verdict
4. Verdict = MALICIOUS
5. Backend returns malicious-warning.html
6. Redirection is blocked permanently
```

---

### 8.4 Invalid or Non-Existent Link Flow

```
1. User clicks the short URL
2. Browser sends GET /url.shortener/{hash}
3. Backend fails to resolve the hash
4. Backend returns error.html (404)
5. No analytics captured
```

---

### 8.5 Report Abuse Flow (Community Safety)

```
1. User encounters a suspicious short URL
2. User chooses to report the link
3. Browser sends POST /report-abuse with ReportLinkRequestDto
4. Backend validates request fields
5. Backend validates OTP (email match, unused, not expired)
6. Backend checks if reporter has already reported this link
7. If duplicate report detected → request rejected
8. If valid → report is accepted and stored
9. reportCount for the link is incremented
10. If reportCount >= 3
11. Verdict is updated to PENDING_REVERIFICATION
12. Re-verification process is scheduled asynchronously
```

---

### Design Notes for Report Abuse Flow

* OTP validation ensures verified and accountable reporting
* Duplicate report prevention avoids artificial verdict escalation
* Verdict transition is system-driven, not user-controlled
* Reporting does not immediately block access unless verdict changes

```

---

### Design Notes

- Analytics capture is intentionally **decoupled** from redirection
- User experience is never blocked by analytics failures
- All verdict-based flows converge to a single tracking mechanism (`track.html`) except MALICIOUS
- Kafka enables asynchronous and scalable analytics processing

---

## 9. Verdict Definitions

The system assigns a **verdict** to every short link based on automated analysis and user reports. This verdict determines how redirection is handled.

| Verdict | Meaning | Redirection Behavior |
|------|--------|---------------------|
| SAFE | Link is verified and considered safe | Redirects immediately |
| SUSPICIOUS | Link has risk signals or reports | User confirmation required |
| UNVERIFIED | External services (e.g., VirusTotal) could not determine safety (e.g., new or unknown domain) | User confirmation required |
| PENDING_REVERIFICATION | Link received more than 3 user reports and is awaiting re-analysis | User confirmation required |
| MALICIOUS | Link is confirmed malicious | Redirection is completely blocked |

---

### Notes on Verdict Lifecycle

- **UNVERIFIED** verdicts typically occur for newly registered domains or insufficient reputation data
- **PENDING_REVERIFICATION** is a system-controlled state triggered by user reports
- **SUSPICIOUS**, **UNVERIFIED**, and **PENDING_REVERIFICATION** share the same user-facing flow but are internally distinct for future automation and moderation
- **MALICIOUS** links are never redirected under any circumstance

---

## 9. Report Link (Abuse Reporting) Module

This module allows **end users (clickers)** to report a shortened URL if they believe it is unsafe, misleading, or abusive. It acts as a **community-driven safety feedback loop** and directly influences the link's verdict lifecycle.

---

### 9.1 Purpose

- Enable users to report suspicious or abusive links
- Collect human feedback in addition to automated analysis
- Escalate links for re-verification based on report volume
- Prevent silent abuse of newly created or unverified links

---

### 9.2 Report Abuse API

**Endpoint**
```

POST /report-abuse

````

**Authentication**

Not required (public endpoint)

> This API is public because it is intended to be used by any end user who encounters a suspicious link.

---

### 9.3 Request Body – ReportLinkRequestDto

```json
{
  "reporterName": "John Doe",
  "reporterEmail": "john@example.com",
  "linkToReport": "https://sho.rt/aZ91Qe",
  "cause": ["Phishing", "Scam"],
  "description": "The link redirects to a fake login page",
  "otp": "483921"
}
````

---

### Field Description

| Field         | Description                                           |
| ------------- | ----------------------------------------------------- |
| reporterName  | Name of the person reporting the link                 |
| reporterEmail | Email address of the reporter                         |
| linkToReport  | The short URL being reported                          |
| cause         | One or more reasons for reporting abuse               |
| description   | Optional additional context provided by the reporter  |
| otp           | One-time password used to verify the reporter's email |

---

### OTP Validation Rules

The report-abuse flow uses the **same OTP mechanism as the signup flow**, with the following rules:

* OTP must be generated for the same email (`reporterEmail`)
* OTP must be **unused**, **correct**, and **not expired**
* OTP is invalidated immediately after successful report submission

This ensures:

* Email ownership verification
* Prevention of anonymous or spam abuse reports

---

### 9.4 Duplicate Report Prevention

* A user who has already reported a specific short URL **cannot report the same URL again**
* Re-reporting is blocked until the link verification or re-verification process is completed

This rule prevents:

* Report spamming
* Artificial escalation of verdicts

---

### 9.5 Impact on Verdict Lifecycle

The Report Link module directly affects the link's verdict state:

```
SAFE / UNVERIFIED / SUSPICIOUS
        ↓ (User reports link)
Increment reportCount
        ↓
If reportCount >= 3
        ↓
Verdict → PENDING_REVERIFICATION
```

---

### 9.6 System Behavior After Reporting

* The report is accepted and acknowledged immediately
* The link remains accessible based on its current verdict
* Re-verification is handled asynchronously (manual or automated in future)
* Reporters may be notified once a final verdict is reached

---

## 10. User Account Management APIs

These APIs allow an authenticated user to manage their own account credentials and lifecycle. All endpoints in this section require a valid JWT Bearer token.

🔐 **Authentication Required**

```
Authorization: Bearer <JWT_TOKEN>
```

---

### 10.1 Update User Credentials

Allows the logged-in user to update their account credentials such as password or email.

**Endpoint**

```
PUT /user/update-user-credentials
```

**Authentication**

Required (JWT)

**Important Notes**

* The username is resolved from the JWT (SecurityContext)
* Any `userName` provided in the request body is ignored
* Only the authenticated user can update their own credentials

**Request Body – UserCredentialUpdateRequestDto**

```json
{
  "userName": "vishal123",
  "password": "NewStrongPassword@123",
  "email": "new-email@example.com"
}
```

**Success Response (200 OK)**

```
No Content
```

**Failure Responses**

* `400 Bad Request` – Invalid update request or validation failure

---

### 10.2 Delete User Account

Permanently deletes the account of the currently authenticated user.

**Endpoint**

```
DELETE /user/delete-user
```

**Authentication**

Required (JWT)

**Important Notes**

* Operation is irreversible
* All user-owned resources (links, analytics, reports) may also be removed

**Success Response (200 OK)**

```
No Content
```

**Failure Responses**

* `404 Not Found` – User does not exist

---

## 11. Error Scenarios Summary

* **401 Unauthorized** – Missing or invalid JWT / expired OTP
* **403 Forbidden** – Accessing a resource owned by another user
* **404 Not Found** – Link or user does not exist
* **409 Conflict** – Duplicate username or email during signup

---

**Author**: Vishal Sharma
**Project**: URL Shortener with Analytics & Safety
