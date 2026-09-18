# Study Room Booking System

Assignment 3 — Project Phase 1. A JSF + Hibernate + PostgreSQL web app with
full CRUD for two entities: **Study Room** and **Booking**.

See [`docs/DOCUMENTATION.md`](docs/DOCUMENTATION.md) for the Abstract,
Problem Statement, Scope, AS-IS/TO-BE models, Business Requirements,
Software Qualities and the Initial Class Diagram.

## Tech stack

- Java 17 (compiled with JDK 21)
- JSF (Mojarra 2.3.9) — presentation layer
- Hibernate ORM 5.6 — persistence
- Hibernate Validator (Bean Validation) — one of the three validation types
- PostgreSQL — database
- Apache Tomcat 9 — servlet container
- Maven — build tool

## Prerequisites

- **Java 17+** (`java -version`)
- **Maven** (`mvn -v`)
- **PostgreSQL** running locally
- **Apache Tomcat 9** installed (download from https://tomcat.apache.org/download-90.cgi and unzip anywhere, e.g. `C:\tomcat9`)

## Step-by-step setup

### Step 1 — Create the database

Open `psql` (or pgAdmin) and run:

```sql
CREATE DATABASE studyroom_db;
```

### Step 2 — Configure your database credentials

Edit `src/main/resources/hibernate.cfg.xml` and set your own Postgres
username/password if different from the defaults:

```xml
<property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/studyroom_db</property>
<property name="hibernate.connection.username">postgres</property>
<property name="hibernate.connection.password">postgres</property>
```

`hibernate.hbm2ddl.auto=update` is already set, so Hibernate creates/updates
the `student`, `study_room` and `booking` tables automatically the first
time the app starts — no manual SQL needed.

### Step 3 — Build the WAR

From the project root (`study-room-booking-system/`):

```bash
mvn clean package
```

This produces `target/study-room-booking-system.war`.

### Step 4 — Deploy to Tomcat

Copy the WAR into Tomcat's `webapps` folder:

```bash
cp target/study-room-booking-system.war /path/to/tomcat9/webapps/
```

(On Windows PowerShell: `Copy-Item target\study-room-booking-system.war C:\tomcat9\webapps\`)

### Step 5 — Start Tomcat

```bash
/path/to/tomcat9/bin/startup.sh      # Linux/Mac
C:\tomcat9\bin\startup.bat           # Windows
```

Tomcat will auto-extract the WAR. Watch `logs/catalina.out` (or the
console on Windows) — on first startup, Hibernate logs the `create table`
statements and `DataSeedListener` inserts 3 sample students and 3 sample
study rooms.

### Step 6 — Open the app

```
http://localhost:8080/study-room-booking-system/
```

- **Study Rooms** → list, add, edit, delete rooms.
- **Bookings** → list, add, edit, delete bookings (pick one of the seeded
  students and rooms from the dropdowns).

Try triggering each validation type to see it in action:
- Leave "Building" empty → required-field error (Type 1).
- Set Capacity to `0` or `99` → Bean Validation range error (Type 2).
- Enter a room number like `office1` → custom format validator error (Type 3).
- Set an end time earlier than the start time on a booking → custom
  cross-field validator error (Type 3).

### Step 7 — Verify the generated tables

```bash
psql -U postgres -d studyroom_db -c "\dt"
psql -U postgres -d studyroom_db -c "\d study_room"
psql -U postgres -d studyroom_db -c "\d booking"
```

You should see `student`, `study_room` and `booking`, with `booking`
carrying the `student_id` and `room_id` foreign key columns.

### Step 8 — Push to GitHub and add the link to the documentation

```bash
git init
git add .
git commit -m "Study Room Booking System - CRUD for StudyRoom and Booking"
git branch -M main
git remote add origin https://github.com/<your-username>/study-room-booking-system.git
git push -u origin main
```

Then paste the repository URL into `docs/DOCUMENTATION.md` (Section 11)
and into your assignment submission.

### Step 9 — Record the video and add the link

Record a 5–10 minute screen + camera walkthrough (Google Vid) covering:
1. The project proposal (problem, scope, entities).
2. A live demo of the CRUD workflow for Study Room and Booking, including
   at least one validation error for each of the three types.

Paste the share link into `docs/DOCUMENTATION.md` (Section 12).

## Project structure

```
study-room-booking-system/
├── pom.xml
├── README.md
├── docs/DOCUMENTATION.md
└── src/main
    ├── java/com/studyroom/booking
    │   ├── model         (Student, StudyRoom, Booking, RoomStatus, BookingStatus)
    │   ├── dao            (StudentDAO, StudyRoomDAO, BookingDAO)
    │   ├── bean           (StudyRoomBean, BookingBean, StudentConverter, StudyRoomConverter)
    │   ├── validator      (RoomNumberFormatValidator, BookingTimeRangeValidator)
    │   ├── listener       (DataSeedListener)
    │   └── util           (HibernateUtil)
    ├── resources/hibernate.cfg.xml
    └── webapp
        ├── WEB-INF/web.xml, faces-config.xml
        ├── resources/css/styles.css   (external CSS)
        ├── index.xhtml                (internal CSS)
        ├── rooms/list.xhtml, form.xhtml
        └── bookings/list.xhtml, form.xhtml
```
