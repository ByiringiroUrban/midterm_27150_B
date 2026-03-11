
## Names: Byiringiro Urbain Bobola
## ID: 27150

## One Healthline Connect  Spring Boot Mid Exam Project

## 1. Technology Stack

- Java (JDK 21)
- Spring Boot 4.x
- Spring Data JPA
- PostgreSQL 18.3
- H2 For testing only
- Maven

---

## 2. Database Setup 

1. **Create database**

```sql
CREATE DATABASE one_healthline_connect;
```

## 2. Running the Application

From the project root (`WebTechMidExam/WebTechMidExam`), run:

```bash
mvn spring-boot:run
```

The application will start on:

- **Base URL**: `http://localhost:8080`

---

## 4. Core Entities and Relationships

### 4.1 Main Tables (Entities)

- **Province** (`provinces`)
  - `id` (PK)
  - `code` (unique, e.g. `"KGL"`)
  - `name` (unique, e.g. `"Kigali City"`)

- **District** (`districts`)
  - `id` (PK)
  - `name`
  - `province_id` (FK → `provinces.id`)

- **AppUser** (`users`)
  - `id` (PK)
  - `full_name`
  - `email` (unique)
  - `phone` (unique)
  - `role` (`PATIENT`, `DOCTOR`, `ADMIN`)
  - `province_id` (FK → `provinces.id`)
  - `district_id` (FK → `districts.id`)

- **PatientProfile** (`patient_profiles`)
  - `id` (PK)
  - `user_id` (FK → `users.id`, **unique** → One-to-One)
  - `gender`
  - `blood_group`

- **Clinic** (`clinics`)
  - `id` (PK)
  - `name` (unique)
  - `province_id` (FK → `provinces.id`)
  - `district_id` (FK → `districts.id`)

- **Doctor** (`doctors`)
  - `id` (PK)
  - `full_name`
  - `email` (unique)
  - `clinic_id` (FK → `clinics.id`)

- **Specialty** (`specialties`)
  - `id` (PK)
  - `name` (unique)

- **DoctorSpecialty** (`doctor_specialties`)  join table (Many-to-Many)
  - `doctor_id` (FK → `doctors.id`)
  - `specialty_id` (FK → `specialties.id`)
  - Composite PK: (`doctor_id`, `specialty_id`)

- **Location** (`locations`)
  - `id` (PK)
  - `sector`
  - `cell`
  - `village`
  - `province_id` (FK → `provinces.id`)
  - `district_id` (FK → `districts.id`)

- **Appointment** (`appointments`)
  - `id` (PK)
  - `patient_user_id` (FK → `users.id`)
  - `doctor_id` (FK → `doctors.id`)
  - `scheduled_at` (datetime)

### 4.2 Relationship Summary

- **One-to-Many**
  - `Province (1) → District (many)` via `districts.province_id`
  - `Province (1) → AppUser (many)` via `users.province_id`
  - `Clinic (1) → Doctor (many)` via `doctors.clinic_id`

- **One-to-One**
  - `AppUser (1) ↔ PatientProfile (1)` via `patient_profiles.user_id` (unique FK)

- **Many-to-Many**
  - `Doctor (many) ↔ Specialty (many)` via join table `doctor_specialties(doctor_id, specialty_id)`

- **Other**
  - `Appointment` links **patient user** and **doctor** via FKs.
  - `Location` links a generic location to a `Province` and `District`.

---

## 5. Global Validation and Error Handling

### 5.1 Validation

DTOs such as:

- `CreateLocationRequest`
- `CreateUserRequest`
- `CreateDoctorRequest`

use `jakarta.validation` annotations like:

- `@NotBlank`
- `@NotNull`
- `@Email`

Controllers use `@Valid @RequestBody` to trigger validation on:

- `POST /api/locations`
- `POST /api/users`
- `POST /api/doctors`

### 5.2 Global Error Handling

`GlobalExceptionHandler` (with `@RestControllerAdvice`) catches:

- `MethodArgumentNotValidException` → returns JSON with validation errors.
- `IllegalArgumentException` → returns JSON with an `"error"` message.


---

## 6. API Testing 

Base URL (default):

```text
http://localhost:8080
```

### 6.0 Quick API List

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/provinces` | Create province |
| GET | `/api/provinces` | List all provinces |
| POST | `/api/districts` | Create district |
| GET | `/api/districts` | List all districts |
| POST | `/api/clinics` | Create clinic |
| GET | `/api/clinics` | List all clinics |
| GET | `/api/clinics/page` | List clinics (paginated) |
| POST | `/api/specialties` | Create specialty |
| GET | `/api/specialties` | List all specialties |
| POST | `/api/locations` | Create location |
| POST | `/api/users` | Create user (checks `existsBy` email/phone) |
| GET | `/api/users` | List all users |
| GET | `/api/users/page` | List users (paginated) |
| GET | `/api/users/by-province?code=KGL` | Users by province code |
| GET | `/api/users/by-province?name=Kigali City` | Users by province name |
| POST | `/api/doctors` | Create doctor (Many-to-Many with specialties) |
| GET | `/api/doctors` | List doctors (paginated + sorted) |
| POST | `/api/patient-profiles` | Create patient profile (One-to-One) |
| GET | `/api/patient-profiles` | List all patient profiles |
| GET | `/api/patient-profiles/by-user/{userId}` | Get profile by user id |
| POST | `/api/appointments` | Create appointment |
| GET | `/api/appointments` | List all appointments |
| GET | `/api/appointments/page` | List appointments (paginated) |
| GET | `/api/appointments/by-patient/{userId}` | Appointments by patient |
| GET | `/api/appointments/by-doctor/{doctorId}` | Appointments by doctor |

---

### Provinces

**1. Create Province**
- **POST** `/api/provinces`  
  Example: `http://localhost:8080/api/provinces`
```json
{
  "code": "KGL",
  "name": "Kigali City"
}
```
**2. List Provinces**
- **GET** `/api/provinces`  
  Example: `http://localhost:8080/api/provinces`

---

### Districts

**1. Create District** (requires existing `provinceId`)
- **POST** `/api/districts`  
  Example: `http://localhost:8080/api/districts`
```json
{
  "name": "Gasabo",
  "provinceId": 1
}
```

**2. List Districts**
- **GET** `/api/districts`  
  Example: `http://localhost:8080/api/districts`

---

### Clinics

**1. Create Clinic** (requires existing `provinceId`, `districtId`)
- **POST** `/api/clinics`  
  Example: `http://localhost:8080/api/clinics`
```json
{
  "name": "City Clinic",
  "provinceId": 1,
  "districtId": 1
}
```

**2. List Clinics**
- **GET** `/api/clinics`  
  Example: `http://localhost:8080/api/clinics`

**3. List Clinics (Paginated)**
- **GET** `/api/clinics/page?page=0&size=5&sortBy=name&direction=asc`  
  Example: `http://localhost:8080/api/clinics/page?page=0&size=5&sortBy=name&direction=asc`

---

### Specialties

**1. Create Specialty**
- **POST** `/api/specialties`  
  Example: `http://localhost:8080/api/specialties`
```json
{
  "name": "General Medicine"
}
```

**2. List Specialties**
- **GET** `/api/specialties`  
  Example: `http://localhost:8080/api/specialties`

---

### 6.1 Locations  Save Location 

**Endpoint**

- **POST** `/api/locations`  
  Example: `http://localhost:8080/api/locations`

**Sample Request (JSON)**

```json
{
  "sector": "Remera",
  "cell": "Rukiri",
  "village": "Village 1",
  "provinceId": 1,
  "districtId": 1
}
```

### 6.2 Users – `existsBy()` and Province Queries.

#### 6.2.1 Create User

- **POST** `/api/users`  
  Example: `http://localhost:8080/api/users`

**Sample Request (JSON)**

```json
{
  "fullName": "Urban Byiringiro",
  "email": "Byiringirourban@gmail.com",
  "phone": "0788854243",
  "provinceId": 1,
  "districtId": 1
}
```
#### 6.2.2 List All Users

- **GET** `/api/users`  
  Example: `http://localhost:8080/api/users`


#### 6.2.3 Get Users by Province Code

- **GET** `/api/users/by-province?code=KGL`  
  Example: `http://localhost:8080/api/users/by-province?code=KGL`


#### 6.2.4 Get Users by Province Name

- **GET** `/api/users/by-province?name=Kigali City`  
  Example: `http://localhost:8080/api/users/by-province?name=Kigali City`


#### 6.2.5 List Users (Paginated)

- **GET** `/api/users/page?page=0&size=5&sortBy=fullName&direction=asc`  
  Example: `http://localhost:8080/api/users/page?page=0&size=5&sortBy=fullName&direction=asc`

---

### 6.3 Doctors  Many-to-Many, Sorting, Pagination 

#### 6.3.1 Pre-requisites

Create these first via API: **Province** → **District** → **Clinic** → **Specialty**. Then use their IDs as `clinicId` and `specialtyIds`.

#### 6.3.2 Create Doctor 

- **POST** `/api/doctors`  
  Example: `http://localhost:8080/api/doctors`

**Sample Request (JSON)**

```json
{
  "fullName": "Dr. Gentil Mugisha",
  "email": "dr.gentilmugisha@gmail.com",
  "clinicId": 1,
  "specialtyIds": [1, 2]
}
```

### Patient Profiles (One-to-One with User)

**1. Create Patient Profile** (requires existing `userId`)
- **POST** `/api/patient-profiles`  
  Example: `http://localhost:8080/api/patient-profiles`
```json
{
  "userId": 1,
  "gender": "FEMALE",
  "bloodGroup": "O+"
}
```

**2. List Patient Profiles**
- **GET** `/api/patient-profiles`  
  Example: `http://localhost:8080/api/patient-profiles`

**3. Get Profile by User ID**
- **GET** `/api/patient-profiles/by-user/1`  
  Example: `http://localhost:8080/api/patient-profiles/by-user/1`

---

### Appointments

**1. Create Appointment** (requires existing `patientUserId`, `doctorId`)
- **POST** `/api/appointments`  
  Example: `http://localhost:8080/api/appointments`
```json
{
  "patientUserId": 1,
  "doctorId": 1,
  "scheduledAt": "2026-03-10T10:30:00"
}
```

**2. List Appointments**
- **GET** `/api/appointments`  
  Example: `http://localhost:8080/api/appointments`

**3. List Appointments (Paginated)**
- **GET** `/api/appointments/page?page=0&size=5&sortBy=scheduledAt&direction=asc`  
  Example: `http://localhost:8080/api/appointments/page?page=0&size=5&sortBy=scheduledAt&direction=asc`

**4. Appointments by Patient**
- **GET** `/api/appointments/by-patient/1`  
  Example: `http://localhost:8080/api/appointments/by-patient/1`

**5. Appointments by Doctor**
- **GET** `/api/appointments/by-doctor/1`  
  Example: `http://localhost:8080/api/appointments/by-doctor/1`

---

### 6.4 Sorting and Pagination

The **doctors list** endpoint demonstrates both:

- **Sorting** using Spring Data `Sort`.
- **Pagination** using `PageRequest` / `Pageable`.

**Endpoint**

- **GET** `/api/doctors`  
  Example: `http://localhost:8080/api/doctors?page=0&size=5&sortBy=fullName&direction=asc`

**Query parameters**

- `page` – page index (0-based, default `0`)
- `size` – page size (number of records per page, default `10`)
- `sortBy` – property name to sort by (default `"fullName"`)
- `direction` – `"asc"` or `"desc"` (default `"asc"`)



#### 6.4.3 All Pagination Endpoints (Quick Copy)

| Endpoint | Example URL |
|----------|-------------|
| Users | `http://localhost:8080/api/users/page?page=0&size=5&sortBy=fullName&direction=asc` |
| Clinics | `http://localhost:8080/api/clinics/page?page=0&size=5&sortBy=name&direction=asc` |
| Doctors | `http://localhost:8080/api/doctors?page=0&size=5&sortBy=fullName&direction=asc` |
| Appointments | `http://localhost:8080/api/appointments/page?page=0&size=5&sortBy=scheduledAt&direction=asc` |

---

## 7. Some Screenshoot 

