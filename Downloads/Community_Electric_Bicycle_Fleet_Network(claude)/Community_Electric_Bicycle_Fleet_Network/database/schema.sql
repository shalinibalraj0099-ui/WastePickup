-- =============================================================================
-- Community Shared Electric Bicycle & Micromobility Fleet Network
-- MySQL schema
-- =============================================================================
-- You normally do NOT need to run the CREATE TABLE statements below by hand:
-- Spring Boot + Hibernate (spring.jpa.hibernate.ddl-auto=update) will create
-- and evolve every table automatically from the JPA entities in
-- src/main/java/com/example/bicycle/model the first time the app starts,
-- as long as the database itself exists (createDatabaseIfNotExist=true in
-- application.properties also takes care of that).
--
-- This file is provided so you can:
--   1) create the schema up front / offline, or
--   2) inspect the expected table shapes, or
--   3) reset your database quickly during development.
-- =============================================================================

CREATE DATABASE IF NOT EXISTS bicycle_db;
USE bicycle_db;

-- ---------------------------------------------------------------------------
-- Login accounts
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  role VARCHAR(50) DEFAULT 'RIDER'
);

-- ---------------------------------------------------------------------------
-- Fleet
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS e_bike (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  bike_code VARCHAR(255) UNIQUE,
  battery_percent INT DEFAULT 100,
  status VARCHAR(50) DEFAULT 'AVAILABLE',
  latitude DOUBLE,
  longitude DOUBLE
);

CREATE TABLE IF NOT EXISTS dock_station (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  location VARCHAR(255),
  total_slots INT,
  available_slots INT,
  charging_slots INT
);

CREATE TABLE IF NOT EXISTS bike_location (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  bike_id BIGINT,
  latitude DOUBLE,
  longitude DOUBLE,
  recorded_at DATETIME
);

-- ---------------------------------------------------------------------------
-- People
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS rider (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255),
  phone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS vendor (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255),
  phone VARCHAR(50),
  address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS corporate_sponsor (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  company_name VARCHAR(255),
  contact_name VARCHAR(255),
  email VARCHAR(255)
);

-- ---------------------------------------------------------------------------
-- Rides
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ride (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  rider_id BIGINT,
  bike_id BIGINT,
  start_time DATETIME,
  end_time DATETIME,
  duration_minutes BIGINT,
  fare DOUBLE,
  status VARCHAR(50) DEFAULT 'ACTIVE'
);

-- ---------------------------------------------------------------------------
-- Products / purchasing / sales
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  type VARCHAR(50),
  unit_price DOUBLE,
  unit VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS purchase_order (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  vendor_id BIGINT,
  product_name VARCHAR(255),
  quantity INT,
  total_amount DOUBLE,
  status VARCHAR(50) DEFAULT 'CREATED',
  order_date DATE
);

CREATE TABLE IF NOT EXISTS vendor_bill (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  purchase_order_id BIGINT,
  vendor_id BIGINT,
  amount DOUBLE,
  status VARCHAR(50) DEFAULT 'UNPAID',
  bill_date DATE
);

CREATE TABLE IF NOT EXISTS sales_order (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  sponsor_id BIGINT,
  product_name VARCHAR(255),
  quantity INT,
  total_amount DOUBLE,
  status VARCHAR(50) DEFAULT 'CREATED',
  order_date DATE
);

CREATE TABLE IF NOT EXISTS customer_invoice (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  sales_order_id BIGINT,
  sponsor_id BIGINT,
  amount DOUBLE,
  status VARCHAR(50) DEFAULT 'UNPAID',
  invoice_date DATE
);

-- ---------------------------------------------------------------------------
-- Payments & accounting
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS payment (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  payment_type VARCHAR(50),
  reference_type VARCHAR(50),
  reference_id BIGINT,
  amount DOUBLE,
  method VARCHAR(50),
  payment_date DATE
);

CREATE TABLE IF NOT EXISTS account (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(50) UNIQUE,
  name VARCHAR(255),
  category VARCHAR(50),
  balance DOUBLE DEFAULT 0
);

CREATE TABLE IF NOT EXISTS journal (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  type VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS journal_entry (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  journal_id BIGINT,
  description VARCHAR(255),
  debit DOUBLE DEFAULT 0,
  credit DOUBLE DEFAULT 0,
  entry_date DATE
);

CREATE TABLE IF NOT EXISTS ledger_entry (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_id BIGINT,
  description VARCHAR(255),
  debit DOUBLE DEFAULT 0,
  credit DOUBLE DEFAULT 0,
  entry_date DATE
);

-- ---------------------------------------------------------------------------
-- Budgeting
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS analytic_account (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS budget (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  analytic_account_id BIGINT,
  allocated_amount DOUBLE,
  actual_amount DOUBLE,
  period VARCHAR(50)
);

-- =============================================================================
-- Sample / dummy data is inserted automatically by the application itself
-- (com.example.bicycle.config.DataInitializer) the first time it starts
-- against an empty database, so every module has records to explore and every
-- Postman request has real IDs to test against.
-- =============================================================================
