# Spring Boot Scheduled Tasks with ThreadPoolTaskScheduler

## 🚀 Overview
This project demonstrates the implementation of scheduled tasks using `ThreadPoolTaskScheduler` in a `Spring Boot 3.4.2` application. The system efficiently manages scheduled jobs using a configurable thread pool, ensuring optimal task execution without blocking the main application thread.

---

## ✨Tech Stack
The technology used in this project are:
- `Spring Data JPA with Hibernate` – Simplifies database interactions
- `PostgreSQL` – Used for persisting user and application data
- `Lombok` – Reduces boilerplate code
- `ThreadPoolTaskScheduler` – Manages scheduled tasks with multi-threading support
---

## 📋 Project Structure
The project is organized into the following package structure:
```bash
- `config`
- `entity`
- `repository`
- `scheduler`
- `service`
  \---impl
```

### Explanation of Each Package
- **config**: Configures ThreadPoolTaskScheduler (pool size, thread name prefix, etc.).
- **entity**: Contains JPA entity classes representing database tables.
- **repository**: Provides database access functionality using Spring Data JPA.
- **scheduler**: Defines scheduled tasks executed by ThreadPoolTaskScheduler.
- **service**: Defines business logic and application functionality.
    - **impl**: Implements the service interfaces.
---

## 📂 Environment Configuration
Configuration values are stored in `.env.development` and referenced in `application.properties`.

Example `.env.development` file content:
```properties
# application
APP_PORT=8081
SPRING_PROFILES_ACTIVE=development

# postgres
SPRING_DATASOURCE_PORT=5432
SPRING_DATASOURCE_USERNAME=myusername
SPRING_DATASOURCE_PASSWORD=mypassword
SPRING_DATASOURCE_DB=mydb
SPRING_DATASOURCE_SCHEMA=myschema

# business policy
BUSINESS_POLICY_ALLOWED_INACTIVE_DAYS=3
BUSINESS_POLICY_EXPIRED_ACCOUNT_RETENTION_DAYS=30
BUSINESS_POLICY_EXPIRED_CREDENTIALS_RETENTION_DAYS=30

# taskScheduler
TASK_SCHEDULER_POOL_SIZE=5
TASK_SCHEDULER_THREAD_NAME_PREFIX=thread-
TASK_SCHEDULER_REMOVE_ON_CANCEL_POLICY=false
TASK_SCHEDULER_WAIT_FOR_TASKS_TO_COMPLETE_ON_SHUTDOWN=true
TASK_SCHEDULER_AWAIT_TERMINATION_SECONDS=10
TASK_SCHEDULER_THREAD_PRIORITY=5
TASK_SCHEDULER_CONTINUE_EXISTING_PERIODIC_TASKS_AFTER_SHUTDOWN_POLICY=false
TASK_SCHED_SCHEDULER_EXECUTE_EXISTING_DELAYED_TASKS_AFTER_SHUTDOWN_POLICY=true
TASK_SCHEDULER_DAEMON=false

# cron configuration
## daily
SCHEDULER_DAILY_TASK_SET_ACCOUNT_EXPIRATION_DATE=0 5 0 * * ?
SCHEDULER_DAILY_TASK_SET_ACCOUNTS_TO_EXPIRED=0 10 0 * * ?
SCHEDULER_DAILY_TASK_SET_CREDENTIALS_TO_EXPIRED=0 15 0 * * ?

## weekly
SCHEDULER_WEEKLY_TASK_SEND_EMAIL_TO_USERS_WITH_EXPIRED_ACCOUNTS=0 5 9 * * MON
SCHEDULER_WEEKLY_TASK_SEND_EMAIL_TO_USERS_WITH_EXPIRED_CREDENTIALS=0 10 9 * * MON

## monthly
SCHEDULER_MONTHLY_TASK_CLEANUP_EXPIRED_ACCOUNTS=0 5 1 28 * ?
SCHEDULER_MONTHLY_TASK_CLEANUP_EXPIRED_CREDENTIALS=0 20 1 28 * ?
```

Example `application.properties` file content:
```properties
spring.application.name=task-scheduler
server.port=${APP_PORT}
spring.profiles.active=${SPRING_PROFILES_ACTIVE}

## datasource 
spring.datasource.url=jdbc:postgresql://localhost:${SPRING_DATASOURCE_PORT}/${SPRING_DATASOURCE_DB}?currentSchema=${SPRING_DATASOURCE_SCHEMA}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

## hibernate 
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=true

## business policy
business.policy.allowed-inactive-days=${BUSINESS_POLICY_ALLOWED_INACTIVE_DAYS}
business.policy.expired-account-retention-days=${BUSINESS_POLICY_EXPIRED_ACCOUNT_RETENTION_DAYS}
business.policy.expired-credentials-retention-days=${BUSINESS_POLICY_EXPIRED_CREDENTIALS_RETENTION_DAYS}

## taskScheduler configuration
spring.task.scheduling.pool.size=${TASK_SCHEDULER_POOL_SIZE}
spring.task.scheduling.thread-name-prefix=${TASK_SCHEDULER_THREAD_NAME_PREFIX}
spring.task.scheduling.remove-on-cancel-policy=${TASK_SCHEDULER_REMOVE_ON_CANCEL_POLICY}
spring.task.scheduling.wait-for-tasks-to-complete-on-shutdown=${TASK_SCHEDULER_WAIT_FOR_TASKS_TO_COMPLETE_ON_SHUTDOWN}
spring.task.scheduling.await-termination-seconds=${TASK_SCHEDULER_AWAIT_TERMINATION_SECONDS}
spring.task.scheduling.thread-priority=${TASK_SCHEDULER_THREAD_PRIORITY}
spring.task.scheduling.continue-existing-periodic-tasks-after-shutdown-policy=${TASK_SCHEDULER_CONTINUE_EXISTING_PERIODIC_TASKS_AFTER_SHUTDOWN_POLICY}
spring.task.scheduling.execute-existing-delayed-tasks-after-shutdown-policy=${TASK_SCHED_SCHEDULER_EXECUTE_EXISTING_DELAYED_TASKS_AFTER_SHUTDOWN_POLICY}
spring.task.scheduling.daemon=${TASK_SCHEDULER_DAEMON}

## cron configuration
### daily task
scheduler.daily-task.set-account-expiration-date=${SCHEDULER_DAILY_TASK_SET_ACCOUNT_EXPIRATION_DATE}
scheduler.daily-task.set-accounts-to-expired=${SCHEDULER_DAILY_TASK_SET_ACCOUNTS_TO_EXPIRED}
scheduler.daily-task.set-credentials-to-expired=${SCHEDULER_DAILY_TASK_SET_CREDENTIALS_TO_EXPIRED}

## weekly task
scheduler.weekly-task.send-email-to-users-with-expired-accounts=${SCHEDULER_WEEKLY_TASK_SEND_EMAIL_TO_USERS_WITH_EXPIRED_ACCOUNTS}
scheduler.weekly-task.send-email-to-users-with-expired-credentials=${SCHEDULER_WEEKLY_TASK_SEND_EMAIL_TO_USERS_WITH_EXPIRED_CREDENTIALS}

## monthly task
scheduler.monthly-task.cleanup-expired-accounts=${SCHEDULER_MONTHLY_TASK_CLEANUP_EXPIRED_ACCOUNTS}
scheduler.monthly-task.cleanup-expired-credentials=${SCHEDULER_MONTHLY_TASK_CLEANUP_EXPIRED_CREDENTIALS}
```
---

## 💾 Database Schema (DDL – PostgreSQL)
The project uses PostgreSQL as its database, with a structured schema to store the data efficiently. Below is the DDL (Data Definition Language) used to create the database schema.

### Create Schema Employees
Database schema.
```sql
CREATE SCHEMA myschema;

-- create table users
CREATE TABLE IF NOT EXISTS myschema.users
(
	id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
	username character varying(20) COLLATE pg_catalog."default" NOT NULL,
    password character varying(150) COLLATE pg_catalog."default" NOT NULL,
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    firstname character varying(20) COLLATE pg_catalog."default" NOT NULL,
    lastname character varying(20) COLLATE pg_catalog."default",
    is_enabled boolean NOT NULL DEFAULT false,
    is_account_non_expired boolean NOT NULL DEFAULT false,
    is_account_non_locked boolean NOT NULL DEFAULT false,
    is_credentials_non_expired boolean NOT NULL DEFAULT false,
    is_deleted boolean NOT NULL DEFAULT false,
	account_expiration_date timestamp with time zone,
    credentials_expiration_date timestamp with time zone,
	last_login timestamp with time zone,
	user_type character varying(15) COLLATE pg_catalog."default" NOT NULL,
	created_by character varying(20) NOT NULL,
	created_date timestamp with time zone NOT NULL DEFAULT now(),
	updated_by character varying(20) NOT NULL,
	updated_date timestamp with time zone NOT NULL DEFAULT now(),
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_unique_username UNIQUE (username),
	CONSTRAINT users_unique_email UNIQUE (email)
);

-- feed data users
INSERT INTO myschema.users (username, "password", email, firstname, lastname, is_enabled, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_deleted, account_expiration_date, credentials_expiration_date, last_login, user_type, created_by, created_date, updated_by, updated_date) VALUES
('superadmin', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'superadmin@youremail.com', 'Super', 'Admin', true, true, true, true, false, '2025-04-23 21:52:38.000', '2025-02-28 01:58:35.835', '2025-01-09 13:53:54.000', 'USER_ACCOUNT', 'system', '2024-09-04 03:42:58.847', 'system', '2024-11-28 01:58:35.835'),
('budigun', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'budigun@youremail.com', 'Budi', 'Gunawan', true, true, true, true, false, '2025-04-23 21:52:38.000', '2025-02-28 01:58:35.835', '2025-01-09 13:53:54.000', 'USER_ACCOUNT', 'superadmin', '2024-09-04 03:42:58.847', 'superadmin', '2024-11-28 01:58:35.835'),
('johndoe', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'johndoe@youremail.com', 'John', 'Doe', true, true, true, true, false, '2026-06-15 12:45:00.000', '2025-12-30 08:00:00.000', '2025-02-20 14:00:00.000', 'USER_ACCOUNT', 'superadmin', '2024-10-01 10:00:00.000', 'superadmin', '2024-12-15 09:00:00.000'),
('janedoe', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'janedoe@youremail.com', 'Jane', 'Doe', true, true, true, true, false, '2026-05-10 08:30:00.000', '2025-11-20 12:00:00.000', '2025-03-05 09:30:00.000', 'USER_ACCOUNT', 'superadmin', '2024-08-20 15:20:00.000', 'superadmin', '2024-12-05 10:10:00.000'),
('alicewong', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'alicewong@youremail.com', 'Alice', 'Wong', true, true, true, true, false, '2025-07-01 14:20:00.000', '2025-12-10 10:10:10.000', '2025-04-22 18:45:00.000', 'USER_ACCOUNT', 'superadmin', '2024-09-10 17:30:00.000', 'superadmin', '2024-11-30 11:45:00.000'),
('robertbrown', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'robertbrown@email.com', 'Robert', 'Brown', true, true, true, true, false, '2025-09-12 23:59:59.000', '2026-01-01 06:30:00.000', '2025-01-15 21:15:00.000', 'USER_ACCOUNT', 'superadmin', '2024-07-05 05:00:00.000', 'superadmin', '2024-12-22 12:45:00.000'),
('emilyclark', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'emilyclark@youremail.com', 'Emily', 'Clark', true, true, true, true, false, '2025-10-25 16:00:00.000', '2025-09-28 11:30:00.000', '2025-05-14 07:00:00.000', 'USER_ACCOUNT', 'superadmin', '2024-06-30 14:10:00.000', 'superadmin', '2024-11-18 18:20:00.000'),
('davidsmith', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'davidsmith@youremail.com', 'David', 'Smith', true, true, true, true, false, '2025-03-14 19:00:00.000', '2025-04-05 09:45:00.000', '2024-12-10 20:30:00.000', 'USER_ACCOUNT', 'superadmin', '2024-05-25 08:00:00.000', 'superadmin', '2024-10-29 13:15:00.000'),
('michaeljohnson', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'michaeljohnson@youremail.com', 'Michael', 'Johnson', true, true, true, true, false, '2026-02-20 05:30:00.000', '2025-08-15 22:00:00.000', '2025-06-01 12:00:00.000', 'USER_ACCOUNT', 'superadmin', '2024-07-14 03:45:00.000', 'superadmin', '2024-11-10 06:30:00.000'),
('sarahlee', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'sarahlee@youremail.com', 'Sarah', 'Lee', true, true, true, true, false, '2025-12-09 08:45:00.000', '2025-07-11 14:30:00.000', '2025-02-25 17:20:00.000', 'USER_ACCOUNT', 'superadmin', '2024-06-11 21:10:00.000', 'superadmin', '2024-10-05 11:55:00.000'),
('chrisadams', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'chrisadams@youremail.com', 'Chris', 'Adams', true, true, true, true, false, '2026-01-01 13:40:00.000', '2025-10-05 19:00:00.000', '2025-04-30 15:10:00.000', 'USER_ACCOUNT', 'superadmin', '2024-08-02 07:30:00.000', 'superadmin', '2024-11-30 16:20:00.000');
```
---

## 🛠 Installation & Setup
A step by step series of examples that tell you how to get a development env running.
1. Clone the repository
```bash
git clone https://github.com/yoanesber/Spring-Boot-ThreadPoolTaskScheduler.git
```

2. Set up PostgreSQL
- Run the provided DDL script to set up the database schema
- Configure the connection in `.env.development` file:
```properties
# postgres
SPRING_DATASOURCE_PORT=5432
SPRING_DATASOURCE_USERNAME=myusername
SPRING_DATASOURCE_PASSWORD=mypassword
SPRING_DATASOURCE_DB=mydb
SPRING_DATASOURCE_SCHEMA=myschema
```

4. Run the application locally
- Make sure PostgreSQL is running, then execute: 
```bash
mvn spring-boot:run
```
---

This project follows best practices in Spring Boot development, ensuring efficiency and maintainability.