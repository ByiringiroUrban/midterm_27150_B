## One Healthline Connect – Spring Boot Exam Project

This project demonstrates **Spring Boot**, **Spring Data JPA**, and **PostgreSQL** for an online health appointment platform called **One Healthline Connect**.

It is designed to cover the following exam requirements:

- **ERD with at least 5 tables**
- **Save `Location`**
- **Sorting and Pagination**
- **Many-to-Many, One-to-Many, One-to-One relationships**
- **`existsBy()` usage**
- **Retrieve users by province code or name**

In addition, it includes:

- **Validation** with `jakarta.validation`
- **Seed data** for provinces, districts, and specialties
- **Global error handling** with a `@RestControllerAdvice`

---

## 1. Technology Stack

- Java (JDK 21)
- Spring Boot 4.x
- Spring Data JPA
- PostgreSQL 18.x
- H2 (tests only)
- Maven (you can use `mvn` directly)

---

## 2. Database Setup (PostgreSQL)

The main configuration is in `src/main/resources/application.properties`:

```properties
spring.application.name=WebTechMidExam

spring.datasource.url=jdbc:postgresql://localhost:5432/one_healthline_connect
spring.datasource.username=postgres        # change if needed
spring.datasource.password=YOUR_PASSWORD   # change if needed

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Steps

1. **Create database**

```sql
CREATE DATABASE one_healthline_connect;
```

2. **Grant privileges to your user** (if needed)

```sql
GRANT ALL PRIVILEGES ON DATABASE one_healthline_connect TO postgres;
```

3. Adjust `spring.datasource.username` and `spring.datasource.password` in `application.properties` if your local PostgreSQL user is different.

---

## 3. Running the Application

From the project root (`WebTechMidExam/WebTechMidExam`), run:

```bash
mvn spring-boot:run
```

The application will start on:

- **Base URL**: `http://localhost:8080`

On startup, the `SeedData` configuration inserts some sample **provinces**, **districts**, and **specialties** into the database.

---

## 4. Core Entities and Relationships (ERD Summary)

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

- **DoctorSpecialty** (`doctor_specialties`) – join table (Many-to-Many)
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

### 4.3 How to Draw the ERD for the Exam

When drawing in tools like draw.io, Lucidchart, or pen & paper:

1. Draw each table above as a box with its attributes.
2. Mark **primary keys (PK)** and **foreign keys (FK)** clearly.
3. Use crow’s feet notation:
   - `Province 1 ---∞ District`
   - `AppUser 1 ---1 PatientProfile`
   - `Doctor ∞---∞ Specialty` with middle join table `doctor_specialties`.
4. Add small notes near each relation explaining:
   - Cardinality (1-1, 1-many, many-many).
   - Which column is the FK in the child table.

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

This makes errors easy to read during Postman testing and viva.

---

## 6. API Testing Guide (by Exam Requirement)

Base URL (default):

```text
http://localhost:8080
```

### 6.0 Quick API List

- **Locations**
  - **POST** `/api/locations` – create a new location linked to a province + district.
- **Users**
  - **POST** `/api/users` – create a new user (checks `existsByEmail/Phone`).
  - **GET** `/api/users` – list all users.
  - **GET** `/api/users/by-province?code=KGL` – users in a province by code.
  - **GET** `/api/users/by-province?name=Kigali City` – users in a province by name.
- **Doctors**
  - **POST** `/api/doctors` – create a doctor and link with specialties (Many-to-Many).
  - **GET** `/api/doctors` – list doctors with **pagination + sorting**.

For provinces, districts, clinics and specialties you can query the DB directly in SQL \(e.g. `SELECT * FROM provinces;`\) to confirm what the app has written.

---

### 6.1 Locations – Save Location (Requirement #2)

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

**What happens (logic for viva):**

- Controller receives `CreateLocationRequest`.
- JPA fetches `Province` and `District` by ID.
- A `Location` entity is created, linked to those entities.
- `locationRepository.save(location)` persists the row with foreign keys.

If you send invalid data or non-existing ids, you will get a **400 Bad Request** with a JSON error from the global exception handler.

---

### 6.2 Users – `existsBy()` and Province Queries (Requirements #7 & #8)

#### 6.2.1 Create User

- **POST** `/api/users`  
  Example: `http://localhost:8080/api/users`

**Sample Request (JSON)**

```json
{
  "fullName": "Alice Patient",
  "email": "alice@example.com",
  "phone": "0788000000",
  "provinceId": 1,
  "districtId": 1
}
```

**What happens:**

- `UserRepository.existsByEmailIgnoreCase(email)` and `UserRepository.existsByPhone(phone)` are called.
- If any returns `true`, a `400` error is thrown: email or phone already exists.
- Otherwise, the user is saved with `province_id` and `district_id` as foreign keys.

#### 6.2.2 List All Users

- **GET** `/api/users`  
  Example: `http://localhost:8080/api/users`

No body required. Returns a JSON array of users.

#### 6.2.3 Get Users by Province Code

- **GET** `/api/users/by-province?code=KGL`  
  Example: `http://localhost:8080/api/users/by-province?code=KGL`

Returns all users whose `province.code = 'KGL'` (case-insensitive).

#### 6.2.4 Get Users by Province Name

- **GET** `/api/users/by-province?name=Kigali City`  
  Example: `http://localhost:8080/api/users/by-province?name=Kigali City`

Returns all users whose `province.name = 'Kigali City'` (case-insensitive).

**Repository logic (for viva):**

- `List<AppUser> findByProvince_CodeIgnoreCase(String code);`
- `List<AppUser> findByProvince_NameIgnoreCase(String name);`

Spring Data JPA uses the nested property path (`province.code`, `province.name`) to generate the correct SQL joins automatically.

---

### 6.3 Doctors – Many-to-Many, Sorting, Pagination (Requirements #3 & #4)

#### 6.3.1 Pre-requisites

- `Province`, `District`, and `Specialty` rows are auto-seeded by `SeedData`.
- You need at least one `Clinic` in the DB (insert once via SQL):

```sql
INSERT INTO clinics(name, province_id, district_id)
VALUES ('City Clinic', 1, 1)
RETURNING id;
```

Use the returned `id` as `clinicId` below.

#### 6.3.2 Create Doctor (Many-to-Many with Specialty)

- **POST** `/api/doctors`  
  Example: `http://localhost:8080/api/doctors`

**Sample Request (JSON)**

```json
{
  "fullName": "Dr. John Doe",
  "email": "dr.john@example.com",
  "clinicId": 1,
  "specialtyIds": [1, 2]
}
```

**What happens:**

- Controller loads the `Clinic` entity by `clinicId`.
- Loads all `Specialty` entities by the given `specialtyIds`.
- Sets them on the `Doctor` entity and saves it.
- JPA automatically fills the **`doctor_specialties`** join table, implementing the many-to-many relationship.

---

### 6.4 Sorting and Pagination (Requirement #3 – Detailed Example)

The **doctors list** endpoint demonstrates both:

- **Sorting** using Spring Data `Sort`.
- **Pagination** using `PageRequest` / `Pageable`.

**Endpoint**

- **GET** `/api/doctors`

**Query parameters**

- `page` – page index (0-based, default `0`)
- `size` – page size (number of records per page, default `10`)
- `sortBy` – property name to sort by (default `"fullName"`)
- `direction` – `"asc"` or `"desc"` (default `"asc"`)

**Example:**

```text
GET /api/doctors?page=0&size=5&sortBy=fullName&direction=asc
```

#### 6.4.1 Step-by-step: How to See Pagination Clearly

1. **Create more than 5 doctors** (e.g. 8–10) by calling **POST** `/api/doctors` several times with different names.

2. Call the first page:

   ```text
   GET /api/doctors?page=0&size=5&sortBy=fullName&direction=asc
   ```

   In the JSON response, look at:

   - `content` → array with **5 doctor records** (first page).
   - `totalElements` → total number of doctors (e.g. 9).
   - `totalPages` → how many pages exist (e.g. 2).
   - `number` → current page index (`0`).
   - `size` → page size (`5`).

3. Call the second page:

   ```text
   GET /api/doctors?page=1&size=5&sortBy=fullName&direction=asc
   ```

   - `number` is now `1`.
   - `content` contains the **remaining doctors** (at most 5).

4. To see **sorting**, change direction:

   ```text
   GET /api/doctors?page=0&size=5&sortBy=fullName&direction=desc
   ```

   - Now doctors in `content` are ordered by `fullName` in **reverse alphabetical** order.

#### 6.4.2 How the Code Works (for viva)

Inside the controller:

```java
Sort sort = "desc".equalsIgnoreCase(direction)
        ? Sort.by(sortBy).descending()
        : Sort.by(sortBy).ascending();

Pageable pageable = PageRequest.of(page, size, sort);
Page<Doctor> result = doctorRepository.findAll(pageable);
```

- `PageRequest.of(page, size, sort)` builds a `Pageable` object with limit/offset and sort info.
- `doctorRepository.findAll(pageable)` generates SQL with `ORDER BY` and `LIMIT/OFFSET`.
- Returning `Page<Doctor>` gives both the **data** and the **metadata** (`totalPages`, `totalElements`, etc.).

---

## 7. Running Tests (H2 In-Memory DB)

For tests, the project uses **H2** automatically via `src/test/resources/application.properties`.

Run:

```bash
mvn test
```

This does not require PostgreSQL to be running and is useful to quickly check that the context and repositories are configured correctly.

---

## 8. Viva Preparation Notes (Cheat Sheet)

- **ERD**: explain each table, PK, FK, and the 3 relationship types \(1-1, 1-many, many-many\).
- **Saving Location**: FKs to `Province` and `District` and how JPA maps them.
- **Sorting + Pagination**: mention `Pageable`, `PageRequest`, `Sort`, and why pagination improves performance.
- **Many-to-Many**: show `doctor_specialties` join table and mappings in `Doctor` and `Specialty`.
- **One-to-Many**: `Province → District`, `Clinic → Doctor`, `Province → AppUser`.
- **One-to-One**: `AppUser ↔ PatientProfile` via unique FK.
- **`existsBy()`**: used in `UserRepository` to check email/phone before inserting.
- **Users by province**: derived query methods with nested property paths (`province.code`, `province.name`).


