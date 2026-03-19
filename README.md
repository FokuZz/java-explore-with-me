# 📱 Explore With Me

## What is it?
A backend for an event discovery platform — find things to do and people to do them with.

Planning your free time is harder than it sounds: finding events, checking if friends are available, coordinating where to meet. This app brings it together.

Built as two independent microservices with separate databases:

### 💻 Main Service
Core business logic. The API is split into three access levels:
- **Public** — available to anyone without registration
- **Private** — for authenticated users
- **Admin** — for platform administrators

### 📜 Stats Service
Handles view tracking and statistics. Consists of three modules:
1. Stats service
2. Client library for sending and receiving stats
3. DTO library

_Application architecture:_
![Architecture diagram](https://github.com/FokuZz/java-explore-with-me/blob/main/info/ewm.png)

---

## ⭐ Features

**Public**
- Search events by date, category, and other filters
- Browse event categories
- View curated collections created by admins

**Authenticated users**
- Search and browse events
- Create and edit your own events
- Apply to participate in other users' events
- Review and manage incoming participation requests

**Admins**
- Full user, category, and event management
- Create and manage curated event collections
- Publish and moderate events

---

## ⚙️ Stack
Java 11 · Spring Boot · Hibernate · JDBC · PostgreSQL · Docker · Maven · JUnit · Mockito · Lombok

---

## 🚀 Running locally

1. Clone the repository
2. Default ports: `8080` (main service), `9090` (stats service)
3. Build:
```shell
docker compose build
```
4. Run:
```shell
docker compose up
```

---

## 🌐 API Reference
Full API specification [here](api).
