# 🚲 Community Shared Electric Bicycle & Micromobility Fleet Network

A full-stack fleet management platform for a community shared e-bike network:
GPS-tracked bikes, docking/charging stations, ride billing, purchasing, sales,
double-entry accounting and budget reporting — with a modern dark dashboard UI
and a complete, Postman-ready REST API.

Built on **Spring Boot 3 (Java 17) + Thymeleaf + Spring Data JPA + MySQL**.

---

## 1. Project structure

```
Community_Electric_Bicycle_Fleet_Network/
├── src/main/java/com/example/bicycle/
│   ├── controller/      REST controllers (full CRUD) + page controllers
│   ├── service/         Business logic (ride lifecycle, etc.)
│   ├── repository/      Spring Data JPA repositories
│   ├── model/           JPA entities
│   ├── dto/             Request payload records
│   ├── config/          Security config + DataInitializer (sample data)
│   └── BicycleApplication.java
├── src/main/resources/
│   ├── templates/       Thymeleaf pages (dark dashboard UI)
│   ├── static/css       style.css — the dashboard design system
│   ├── static/js        script.js — generic CRUD/table/modal engine
│   └── application.properties
├── frontend/            Standalone copy of the templates/css/js/images for
│                        quick reference — the app actually serves the pages
│                        from src/main/resources above (Thymeleaf requires that)
├── database/schema.sql  MySQL DDL reference (Hibernate creates this for you)
├── postman/             Bicycle_Fleet_API_Postman_Collection.json
├── pom.xml
└── README.md
```

> **Why isn't there a literal `backend/` folder?** Spring Boot / Maven require
> `src/main/java` and `src/main/resources` to live at the project root next to
> `pom.xml` in order to build. Everything under `src/` **is** the backend. The
> `frontend/` folder is a plain copy of the same HTML/CSS/JS for anyone who
> wants to browse the UI code without digging into the Maven layout — the
> running application always serves the copy inside `src/main/resources`.

---

## 2. Requirements

* JDK 17+
* Maven 3.9+ (or just use `./mvnw` if you add the wrapper / your IDE's bundled Maven)
* MySQL 8.x running locally (or update the URL to point at any reachable instance)
* Postman (optional, for testing the API collection)

---

## 3. Configure the database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bicycle_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

You do **not** need to run `database/schema.sql` by hand — with
`spring.jpa.hibernate.ddl-auto=update`, Hibernate creates/updates every table
automatically from the JPA entities the first time the app starts (and
`createDatabaseIfNotExist=true` creates the `bicycle_db` schema itself). The
SQL file is there purely as a reference / for manual setup if you prefer it.

On first run, `com.example.bicycle.config.DataInitializer` also inserts
realistic sample data (bikes, stations, riders, rides, purchase orders,
invoices, payments, accounts, budgets…) so the dashboard and every module has
something to show immediately, and so Postman requests have real IDs to work
against. It only seeds once (it checks that the `e_bike` table is empty), so
it's safe to restart the app.

---

## 4. Run the project

**From the command line:**
```bash
mvn spring-boot:run
```

**From an IDE (IntelliJ / Eclipse / VS Code):**
Import as a Maven project and run `BicycleApplication.java`.

The app starts on **http://localhost:8080**.

* `/` – Landing page
* `/login`, `/register` – Auth pages (demo admin: `admin@ebike.com` / `admin123`)
* `/dashboard` – KPI dashboard with live charts
* All other modules are in the sidebar: Bikes, Stations, Rides, Contacts,
  Products, Purchase Orders, Vendor Bills, Sales Orders, Invoices, Payments,
  Chart of Accounts, Journals, Ledger, Budgets, Budget Report, Profit & Loss,
  Balance Sheet.

Every page is a normal server-rendered Thymeleaf page that loads its data
client-side from the REST API below, so the UI and the API always agree and
you can safely drive everything from Postman too.

---

## 5. REST API reference

Base URL: `http://localhost:8080`

Every module below supports the standard 4 operations, following the
pattern `GET / POST / PUT{id} / DELETE{id}`:

| Module | Base path |
|---|---|
| Bikes | `/api/bikes` |
| Stations | `/api/stations` |
| Rides | `/api/rides` (+ `/api/rides/start`, `/api/rides/end`) |
| Riders / Customers | `/api/riders` |
| Vendors | `/api/vendors` |
| Corporate Sponsors | `/api/sponsors` |
| Products | `/api/products` |
| Purchase Orders | `/api/purchase-orders` |
| Vendor Bills | `/api/vendor-bills` |
| Sales Orders | `/api/sales-orders` |
| Customer Invoices | `/api/invoices` |
| Payments | `/api/payments` |
| Chart of Accounts | `/api/accounts` |
| Journals | `/api/journals` |
| Journal Entries | `/api/journal-entries` |
| Ledger | `/api/ledger` |
| Budgets | `/api/budgets` |
| Analytic Accounts | `/api/analytic-accounts` |
| Bike GPS log | `/api/bike-locations` |
| Users (accounts) | `/api/users` (read-only list + delete) |
| Auth | `/api/auth/register`, `/api/auth/login` |
| Reports | `/api/reports/dashboard`, `/api/reports/profit-loss`, `/api/reports/balance-sheet`, `/api/reports/budget` |

### Example — Bikes

```
GET    /api/bikes            → list all bikes
GET    /api/bikes/1          → get bike #1
POST   /api/bikes            → create a bike
PUT    /api/bikes/1          → update bike #1
DELETE /api/bikes/1          → delete bike #1
```

Sample POST body:
```json
{
  "bikeCode": "EB-006 (E-Bike X1)",
  "batteryPercent": 95,
  "status": "AVAILABLE",
  "latitude": 9.9252,
  "longitude": 78.1198
}
```

### Example — Start / End a ride

```
POST /api/rides/start
{ "riderId": 1, "bikeId": 5 }

POST /api/rides/end
{ "rideId": 7 }
```
`end` automatically computes `durationMinutes`, `fare` (₹0.50/minute) and
frees the bike back to `AVAILABLE`.

### Auth

```
POST /api/auth/register  { "name": "...", "email": "...", "password": "..." }
POST /api/auth/login     { "email": "...", "password": "..." }
```
Passwords are stored BCrypt-hashed via Spring Security's `PasswordEncoder`.

Every other module (Stations, Products, Purchase Orders, Vendor Bills, Sales
Orders, Invoices, Payments, Chart of Accounts, Journals, Ledger, Budgets…)
follows the exact same GET/POST/PUT/DELETE shape as Bikes above, using the
field names shown on each module's page in the UI.

---

## 6. Postman collection

Import **`postman/Bicycle_Fleet_API_Postman_Collection.json`** into Postman.
It ships with:

* A `base_url` collection variable (defaults to `http://localhost:8080`)
* One folder per module, each with **Get all / Get by ID / Create / Update / Delete**
* Ready-to-send sample JSON bodies for every POST/PUT request
* A dedicated `Rides` folder with `Start ride` / `End ride`
* A `Reports` folder for the dashboard/P&L/balance-sheet/budget aggregation endpoints
* An `Auth` folder for register/login

---

## 7. Tech stack

* **Backend:** Spring Boot 3.5, Spring Web, Spring Data JPA, Spring Security
  (BCrypt password hashing + permissive CORS/API access), Lombok, Bean Validation
* **Database:** MySQL 8 (auto DDL via Hibernate)
* **Frontend:** Thymeleaf + vanilla JS (no build step) + Chart.js (CDN) for the
  dashboard/report charts — a small reusable `initCrudPage()` engine in
  `script.js` drives every list/add/edit/delete table in the UI from the REST API
* **API testing:** Postman collection included

---

## 8. Notes / next steps

* This project intentionally keeps authentication simple (BCrypt password
  check, no JWT/session) to stay focused on the fleet/accounting domain — swap
  in Spring Security's session or JWT support if you need protected routes.
* Foreign keys (e.g. `vendorId`, `riderId`, `sponsorId`) are plain `Long`
  columns rather than JPA `@ManyToOne` relations, matching the original
  project's lightweight entity design — this keeps every module independently
  CRUD-able and easy to seed/test, while still being trivial to evolve into
  full relations later if you need cascading joins.
