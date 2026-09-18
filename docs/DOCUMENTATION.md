# Study Room Booking System — Project Documentation

**Assignment 3 — Project Phase 1**
**Student:** MUTONI Patience
**Repository:** https://github.com/mutonipatience877/study-room-booking-system
**Video walkthrough:** _add your Google Vid share link here_

---

## 1. Abstract

Students at a university often struggle to find and reserve a quiet, equipped
study room during peak hours, especially around exam periods, because room
availability is currently tracked informally (physical sign-up sheets,
WhatsApp groups, or word of mouth). The **Study Room Booking System** is a
web application that lets students browse available study rooms and book a
room for a specific date and time slot, while giving administrators a simple
way to manage the room inventory. This phase of the project focuses on
designing the system's data model and implementing full Create, Read, Update
and Delete (CRUD) functionality for two of its core entities — **Study
Room** and **Booking** — using JavaServer Faces (JSF) for the presentation
layer and Hibernate for persistence, backed by a PostgreSQL database.

## 2. Problem Statement

Manual room-booking methods create several recurring problems:

- **Double-booking:** two students unknowingly reserve the same room at an
  overlapping time because there is no single source of truth.
- **No visibility:** students cannot see which rooms are free, how many
  people a room seats, or whether it has a projector/whiteboard before
  walking there.
- **No accountability:** there is no record of who booked what, when, or
  why, making it hard to resolve disputes or track room usage.
- **Manual administration:** staff cannot easily take a room out of service
  (e.g. for maintenance) without confusing students who still see it as
  bookable.

The system addresses these problems by centralizing room and booking data
in a relational database, with a web interface that enforces validation
rules before a booking is accepted.

## 3. Scope of the Project

**In scope (this phase):**
- Data model for students, study rooms and bookings.
- Full CRUD (Create, Read, Update, Delete) for **Study Room** and
  **Booking** through JSF-backed web pages.
- Field-level validation (required fields, format rules, business rules
  such as "end time must be after start time" and "booking date cannot be
  in the past").
- Basic styling (external, internal and inline CSS).

**Out of scope (future phases):**
- Student authentication/login and role-based access control (admin vs.
  student).
- Full CRUD screens for the `Student` entity (students are seeded and
  selected from a dropdown for this phase).
- Conflict detection that automatically rejects overlapping bookings for
  the same room.
- Email/SMS notifications and reporting/analytics dashboards.

## 4. AS-IS Model (current, manual process)

```
Student wants a room
        |
        v
Walks to the library / department notice board
        |
        v
Checks a paper sign-up sheet or asks staff verbally
        |
        v
 Is a slot free?
   |         |
  No        Yes
   |         |
   v         v
Tries       Writes name/time on
another     the paper sheet (no
location    validation, no central
   |        record)
   |         |
   v         v
   +----> Conflicts discovered only
          when two students show up
          for the same room/time
```

**Weaknesses of the AS-IS process:** no central record, no validation, no
way to check availability remotely, disputes are resolved by whoever
argues loudest, and staff cannot easily analyze room usage.

## 5. TO-BE Model (proposed system)

```
Student opens the Study Room Booking web app
        |
        v
Browses the list of Study Rooms (room #, building,
capacity, projector/whiteboard, status)
        |
        v
Selects "New Booking" -> fills booking form
(student, room, date, start/end time, purpose)
        |
        v
System validates the form:
  - required fields present
  - date not in the past, times well-formed
  - end time after start time (custom validator)
        |
        v
   Valid?
   |      |
  No     Yes
   |      |
   v      v
Show    Booking saved to the database
error   (Hibernate persists to PostgreSQL)
message      |
             v
     Booking appears in the Bookings list
     with status PENDING/CONFIRMED/etc.,
     visible to admin and student
```

**Improvements over AS-IS:** a single authoritative data store, immediate
validation feedback, remote visibility into room status and bookings, and
a foundation (the `Booking` entity) that a future phase can extend with
automatic conflict detection.

## 6. Business Requirements

| # | Requirement |
|---|---|
| BR1 | The system shall allow staff to register a study room with its room number, building, floor, capacity, and available equipment (projector, whiteboard). |
| BR2 | The system shall allow staff to update or remove a study room, and to mark a room `UNDER_MAINTENANCE` or `CLOSED`. |
| BR3 | The system shall allow a student to create a booking for a specific room, date, time range and purpose. |
| BR4 | The system shall allow a booking to be updated (e.g. rescheduled) or cancelled. |
| BR5 | The system shall reject a booking whose date is in the past. |
| BR6 | The system shall reject a booking whose end time is not after its start time. |
| BR7 | The system shall require a room number to follow a consistent format (e.g. `A101`) so rooms can be located predictably across buildings. |
| BR8 | The system shall list all study rooms and all bookings so students and staff can review current usage. |

## 7. Software Qualities Applied

| Quality | How it is applied in this system |
|---|---|
| **Usability** | Clear navigation bar on every page, labeled form fields, inline error messages next to the field that caused them, and status badges (colored labels) so room/booking state is understood at a glance. |
| **Reliability** | Bean Validation constraints on the entities (`@NotNull`, `@Size`, `@Min`/`@Max`) guarantee the database never receives incomplete or out-of-range data, regardless of which screen writes to it. |
| **Maintainability** | Clear layering: JSF pages (`webapp/*.xhtml`) → managed beans (`bean` package) → DAOs (`dao` package) → Hibernate-mapped entities (`model` package), so each concern can change independently. |
| **Data Integrity** | Foreign keys (`student_id`, `room_id`) enforced at the database level via Hibernate-managed relationships; cascading rules prevent orphaned bookings when a student or room is removed. |
| **Extensibility** | The `BookingStatus` and `RoomStatus` enums and the DAO layer make it straightforward to add new statuses or new query methods (e.g. "find bookings by date range") without touching the UI layer. |
| **Validation robustness** | Three complementary validation types are combined — standard JSF validators, Bean Validation annotations, and custom validator classes — so both simple and cross-field business rules are enforced (see Section 9). |

## 8. Initial Class Diagram (entities involved in the project)

```
 +----------------+          +------------------------+          +----------------+
 |    Student      |1        |*        Booking         |*        1|   StudyRoom    |
 +----------------+---------->--------------------------<---------+----------------+
 | id              |         | id                       |         | id             |
 | fullName        |         | bookingDate               |         | roomNumber     |
 | email           |         | startTime                 |         | building       |
 | studentNumber   |         | endTime                    |         | floor          |
 | phone           |         | purpose                    |         | capacity       |
 +----------------+         | status                       |         | hasProjector   |
                             +------------------------+         | hasWhiteboard  |
                                                                | status         |
                                                                +----------------+
```

- **Student — Booking:** one-to-many (a student can make many bookings).
- **StudyRoom — Booking:** one-to-many (a room can appear in many bookings).
- **Student — StudyRoom:** conceptually many-to-many ("a student books many
  rooms over time; a room is booked by many students"), but because each
  booking carries its own data (date, time, purpose, status) this N-N is
  **promoted to the `Booking` entity** instead of staying a plain join
  table — the same pattern used for Doctor/Patient/Appointment in the
  clinic exercise.

**Entities implemented with full CRUD in this phase:** `StudyRoom` and
`Booking` (chosen per assignment requirement #2). `Student` is a real,
persisted entity (needed for the genuine foreign key on `Booking`) but its
own CRUD screens are left for a later phase — it is seeded with sample data
and offered as a dropdown when creating/editing a booking.

## 9. Validation Strategy (three types, as required)

| Type | Where used | Example |
|---|---|---|
| **1. Standard/built-in JSF validators** | `building` field (`<f:validateLength>`), `startTime`/`endTime` (`required="true"`) | `rooms/form.xhtml`, `bookings/form.xhtml` |
| **2. Bean Validation (JSR-380 / Hibernate Validator annotations)** | `StudyRoom.floor`, `StudyRoom.capacity`, `Booking.bookingDate`, `Booking.purpose`, `Booking.student`, `Booking.studyRoom` | `model/StudyRoom.java`, `model/Booking.java` — applied automatically by JSF's default Bean Validator, no extra tag needed |
| **3. Custom validator classes** | Room number format (`A101` pattern), end time must be after start time | `validator/RoomNumberFormatValidator.java`, `validator/BookingTimeRangeValidator.java` |

## 10. CSS Strategy (three types, as required)

| Type | Where used |
|---|---|
| **External CSS** | `webapp/resources/css/styles.css`, linked via `<h:outputStylesheet name="css/styles.css"/>` on every page — layout, navigation, tables, buttons, badges. |
| **Internal CSS** | `<style>` block inside `<h:head>` of `index.xhtml` — the hero banner gradient specific to the welcome page. |
| **Inline CSS** | `style="..."` attributes directly on components, e.g. the red bold error messages (`<h:messages style="color:#dc2626; ...">`) on the room and booking forms. |

## 11. GitHub Repository

Public link: https://github.com/mutonipatience877/study-room-booking-system

## 12. Video Walkthrough

Google Vid link (5–10 min, screen + camera, explaining the project proposal
and the CRUD workflow for Study Room and Booking): **_add link here_**
