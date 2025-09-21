# Parking Lot Management System

## Project Overview
This is a comprehensive Parking Lot Management System built with a Spring Boot backend and a React frontend. It is designed to manage parking operations, including vehicle entry/exit, real-time status monitoring, and pricing rule management. The system supports different vehicle types, and a dedicated admin panel allows for the configuration of parking slots and pricing.

## Key Features:

**Vehicle Management:** Park and exit vehicles with automated ticket and receipt generation.

**Dynamic Pricing:** Configurable pricing rules based on vehicle type and duration.

**Real-time Status:** View total, occupied, and available slots in real-time.

**Admin Panel:** Admin-only access to add/remove slots and manage pricing rules.

**Authentication:** Secure login using OAuth 2.0 with Google.

**Extensible Design**: Utilizes the Strategy Pattern for slot allocation and global exception handling.


Technologies Used
## Backend (Spring Boot)
**Framework:** Spring Boot 3

**Security:** Spring Security (OAuth 2.0)

**Database:** PostgreSQL / H2 (for local development)

**ORM**: Spring Data JPA & Hibernate

**Build Tool:** Maven

**Testing:** JUnit 5, Mockito

## Frontend (React)
**Framework:** React 18

**State Management:** React Hooks

**Styling:** CSS Modules

**API Client:** Axios


## Prerequisites

* Java 17 or higher

* Node.js 18 or higher

* Maven

**A Google Cloud Account (for OAuth 2.0)**

**1. Backend Setup**

* **1.1** Database Configuration

This project uses PostgreSQL in production but is configured to use an in-memory H2 database for local development via Spring Profiles.

* **1.2 OAuth 2.0 (Google) Configuration**

Go to the Google Cloud Console and create a new project.

Navigate to APIs & Services > Credentials.

Create an OAuth 2.0 Client ID of type Web application.

Add the following URIs to the "Authorized redirect URIs" section:

http://localhost:8080/api/login/oauth2/code/google

Copy your Client ID and Client Secret.

Add them to your application.yml or as environment variables.

* **1.3 Running the Backend**


**Navigate to the project root directory (where pom.xml is located)**

./mvnw spring-boot:run

The backend will start on http://localhost:8080.

## 2. Frontend Setup
**2.1** Environment Variables
Create .env files in your frontend directory to manage different URLs.

frontend/.env

**Code snippet**

REACT_APP_API_URL=http://localhost:8080


**2.2 Running the Frontend**

**Navigate to the frontend directory**

cd frontend

**Install dependencies**

npm install

**Start the application**

npm start

The frontend will be available at http://localhost:3000.

# Landing Page

<img width="1635" height="966" alt="image" src="https://github.com/user-attachments/assets/bc341fad-2f4a-436c-b2c9-9535e7f15934" />

# Admin Page
<img width="1559" height="1002" alt="image" src="https://github.com/user-attachments/assets/2e604a0c-22e6-4587-8ca0-9212a2333461" />

# User Page
<img width="1542" height="998" alt="image" src="https://github.com/user-attachments/assets/48982940-45f2-47b2-993b-08a09b3e7a9c" />


