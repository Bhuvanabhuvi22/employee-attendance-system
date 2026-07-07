# Employee Attendance Monitor

A full-stack version of your attendance app: the original HTML/CSS/JS frontend
now talks to a real Spring Boot backend, which stores records in a SQLite file
so data survives page refreshes and server restarts.

## Project structure

```
employee-attendance-system/
├── backend/     Spring Boot (Java 17+, Maven) REST API
└── frontend/    Your original HTML/CSS/JS, updated to call the API
```

## Running the backend

Requirements: Java 17 or newer and Maven installed.

```bash
cd backend
mvn spring-boot:run
```

This starts the API on **http://localhost:8081**. A file called
`attendance.db` (SQLite) will be created automatically in the `backend`
folder the first time you run it — that's your database, no setup needed.

## Running the frontend

Just open `frontend/index.html` in your browser (or serve it with any static
server, e.g. VS Code's "Live Server" extension). It calls the backend at
`http://localhost:8081/api/attendance`.

Make sure the backend is running first, or you'll see a "could not reach the
server" alert.

## API endpoints

| Method | Endpoint                     | Description                          |
|--------|-------------------------------|---------------------------------------|
| GET    | `/api/attendance`             | List all attendance records          |
| POST   | `/api/attendance/checkin`     | `{ "empCode": "...", "empName": "..." }` |
| POST   | `/api/attendance/checkout`    | `{ "empCode": "..." }`               |

### Business rules enforced server-side
- An employee can't check in twice while already checked in (409 Conflict).
- An employee can't check out without an active check-in (400 Bad Request).
- Timestamps are generated on the server, not the browser, so they're trustworthy.

## Notes / next steps you might want
- Add employee login / JWT auth if this needs to be secured.
- Add a "date" filter so the table doesn't grow forever across days.
- Swap SQLite for MySQL/PostgreSQL later — only `application.properties`
  and the Maven dependency need to change, the rest of the code stays the same.
