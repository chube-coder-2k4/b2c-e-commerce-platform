# 🛍️ Mini E-Commerce Platform (Spring Boot Monolith)

> **Full-featured backend project** built for showcasing enterprise-level backend development using **Spring Boot 3**, **PostgreSQL**, **Redis**, **RabbitMQ**, **Cloudinary**, and **Docker**.  
> Designed as a real-world **E-Commerce system** that includes authentication, payments, caching, event-driven communication, and deploy-ready setup.

---

## 🚀 Features Overview

### 🧩 Core Features
- **Authentication & Authorization**
  - JWT (Access + Refresh token)
  - Login via Google & GitHub (OAuth2)
  - Forgot password, OTP email verification
- **User Management**
  - Register, login, role-based authorization (`@PreAuthorize`)
  - Audit tracking (created_by, updated_by)
- **Product Management**
  - CRUD with DTO & Mapper
  - Image upload via **Cloudinary**
  - Pagination, sorting, and filtering
- **Shopping Cart & Orders**
  - Add-to-cart, checkout flow, order tracking
  - Stock reservation & transaction consistency
- **Payments**
  - Integrated **VNPAY Sandbox**
  - Async callback verification & event publishing
- **Mail Notifications**
  - HTML email sender (order confirmation, OTP)
  - Event listener triggered mails
- **Redis Caching**
  - Cache product lists & categories
  - TTL & eviction strategies
- **RabbitMQ / Kafka**
  - Event-driven communication (order events, email jobs)
- **Scheduler**
  - Cron/fixed-rate jobs (e.g., clean expired orders)
- **WebSocket**
  - Realtime order notifications for Admin Dashboard
- **I18N**
  - Vietnamese 🇻🇳 + English 🇬🇧 resource bundles
- **API Docs**
  - Swagger UI + Bearer JWT auth
- **Healthcheck & Monitoring**
  - Actuator endpoints `/health`, `/info`, `/metrics`
- **Logging**
  - Centralized `SLF4J` logging
  - Audit logs & async mail events
- **Dockerized**
  - Ready-to-run with `docker-compose` (Postgres, Redis, RabbitMQ, App)
- **Profiles**
  - `dev`, `test`, `prod` environments (YAML-based config)

---

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-------------|
| **Backend** | Spring Boot 3, Spring Data JPA, Spring Security |
| **Database** | PostgreSQL + Flyway migrations |
| **Cache** | Redis |
| **Message Broker** | RabbitMQ (or Kafka optional) |
| **Storage** | Cloudinary (images), AWS S3 optional |
| **Auth** | JWT + OAuth2 (Google, GitHub) |
| **Testing** | JUnit, Mockito |
| **Documentation** | Swagger / OpenAPI |
| **Deployment** | Docker, Docker Compose |
| **Mailing** | Spring MailSender (SMTP / Gmail) |
| **Monitoring** | Spring Boot Actuator |
| **Realtime** | WebSocket (STOMP protocol) |

---

## 🗂️ Project Structure

