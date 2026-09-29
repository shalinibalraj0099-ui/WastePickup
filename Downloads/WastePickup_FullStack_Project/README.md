# WastePickup Full Stack Project

Backend:
- Spring Boot 3
- Spring Data JPA
- MySQL
- Validation
- REST APIs

Frontend:
- Thymeleaf templates with a JavaScript dashboard

Features:
- Zone pickup schedules
- Household management
- Pickup logs
- Segregation score tracking
- Rolling average score
- Reminder flagging
- Zone dashboard
- SMS reminders for flagged households (Twilio configuration required)

To enable SMS delivery, set `TWILIO_ACCOUNT_SID`, `TWILIO_AUTH_TOKEN`, and `TWILIO_FROM_NUMBER` in the backend process environment. Household phone numbers must use international format, for example `+14155552671`.