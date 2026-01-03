# URL Shortener with Analytics & Safety Checks

A backend-driven URL shortener that generates short links, applies safety verdicts, tracks click analytics, and redirects users securely.


## Features

- Generate short URLs for long links
- Redirect users based on link safety verdict (SAFE / SUSPICIOUS / MALICIOUS)
- Intermediate tracking page before redirection
- Collect device and browser analytics on each click
- Click count tracking per short URL
- Email notifications for important actions
- Stateless JWT-based authentication securing all APIs with short-lived tokens (10-minute validity)

## High-Level Architecture
This architecture prioritizes user safety, analytics accuracy, and non-blocking redirection.

The redirection and tracking flow works as follows:

1. User clicks on a shortened URL
2. Spring MVC controller receives the request
3. Link safety verdict is evaluated
    - SAFE → normal redirection flow
    - SUSPICIOUS → user confirmation page
    - MALICIOUS → redirection blocked
4. Before redirecting, an intermediate HTML page is served
5. JavaScript collects device and browser information
6. Analytics data is sent to the backend tracking API
7. User is redirected to the original long URL

### Security & Authentication

All backend APIs are secured using stateless JWT-based authentication.  
JWT tokens are issued after successful login and must be sent as a Bearer token in each request.  
Tokens have a validity of 10 minutes, after which re-authentication is required.  
This approach avoids server-side sessions and improves overall security.

## Configuration Management

The default application configuration acts as a blueprint and uses environment variables for all environment-specific values.  
Separate YAML configurations are used for local development, allowing other developers to reuse the project by supplying their own environment settings without changing the codebase.


## Click Analytics & Tracking

Click analytics are collected on the client side using JavaScript.

The following data is captured:
- Screen width and height
- Browser viewport dimensions
- User-Agent string
- Timezone information

Tracking is designed as best-effort:
- If analytics collection fails, redirection still proceeds
- User experience is never blocked due to tracking issues

## Tech Stack

- Java 17
- Spring Boot
- Spring MVC
- Spring Security (JWT-based authentication)
- REST APIs
- JavaScript (client-side analytics)
- Maven
- Git & GitHub


## How to Run

1. Clone the repository
2. Configure application properties
3. Run the Spring Boot application
4. Access the application via browser or API endpoints

## Future Improvements

- Integrate Geo-IP for location-based analytics
- Add Redis caching for high-traffic click counters
- Build an analytics dashboard for link owners
- Introduce API rate limiting to prevent abuse

