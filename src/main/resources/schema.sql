-- Drop tables to ensure clean state on each run
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS parking_slot;
DROP TABLE IF EXISTS vehicle;
DROP TABLE IF EXISTS user_account;
DROP TABLE IF EXISTS pricing_rule;
DROP TABLE IF EXISTS parking_floor;
DROP TABLE IF EXISTS parking_lot;

-- user_account for OAuth2 and role management
CREATE TABLE user_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    role VARCHAR(50) NOT NULL
);

-- pricing_rule for flexible pricing based on vehicle type
CREATE TABLE pricing_rule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_type VARCHAR(50) NOT NULL UNIQUE,
    base_price DOUBLE NOT NULL,
    hourly_rate DOUBLE NOT NULL
);

-- parking_lot represents a physical parking facility
CREATE TABLE parking_lot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    total_floors INT NOT NULL
);

-- parking_floor represents a level within a parking lot
CREATE TABLE parking_floor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    floor_number INT NOT NULL,
    total_slots INT NOT NULL,
    parking_lot_id BIGINT,
    FOREIGN KEY (parking_lot_id) REFERENCES parking_lot(id)
);

-- parking_slot represents a single parking space
CREATE TABLE parking_slot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    slot_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    floor_id BIGINT,
    FOREIGN KEY (floor_id) REFERENCES parking_floor(id)
);

-- vehicle that is or has been parked
CREATE TABLE vehicle (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plate_no VARCHAR(50) NOT NULL UNIQUE,
    vehicle_type VARCHAR(50) NOT NULL
);

-- ticket tracks a vehicle's parking session
CREATE TABLE ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT,
    slot_id BIGINT,
    entry_time TIMESTAMP NOT NULL,
    exit_time TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
    FOREIGN KEY (slot_id) REFERENCES parking_slot(id)
);

-- payment records a payment transaction
CREATE TABLE payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT,
    amount DOUBLE NOT NULL,
    payment_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES ticket(id)
);