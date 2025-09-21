-- Initial Users for testing roles. Passwords are not stored, as OAuth2 handles authentication.
INSERT INTO user_account (email, name, role) VALUES ('amankhatri2008@gmail.com', 'Admin User', 'ADMIN');
INSERT INTO user_account (email, name, role) VALUES ('user@example.com', 'Normal User', 'USER');

-- Default Pricing Rules
INSERT INTO pricing_rule (vehicle_type, base_price, hourly_rate) VALUES ('BIKE', 1.5, 2.0);
INSERT INTO pricing_rule (vehicle_type, base_price, hourly_rate) VALUES ('CAR', 2.0, 3.5);
INSERT INTO pricing_rule (vehicle_type, base_price, hourly_rate) VALUES ('TRUCK', 5.0, 7.0);

-- Initial Parking Lot and Floors
INSERT INTO parking_lot (name, location, total_floors) VALUES ('Main Street Lot', '123 Main St', 3);
INSERT INTO parking_floor (floor_number, total_slots, parking_lot_id) VALUES (1, 15, 1); -- Lot ID 1
INSERT INTO parking_floor (floor_number, total_slots, parking_lot_id) VALUES (2, 10, 1);
INSERT INTO parking_floor (floor_number, total_slots, parking_lot_id) VALUES (3, 5, 1);

-- Initial Parking Slots
-- Floor 1: 10 CAR slots and 5 BIKE slots
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('BIKE', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('BIKE', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('BIKE', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('BIKE', 'AVAILABLE', 1);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('BIKE', 'AVAILABLE', 1);

-- Floor 2: 10 CAR slots
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 2);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 2);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 2);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 2);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 2);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 2);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 2);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 2);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 2);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('CAR', 'AVAILABLE', 2);

-- Floor 3: 5 TRUCK slots
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('TRUCK', 'AVAILABLE', 3);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('TRUCK', 'AVAILABLE', 3);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('TRUCK', 'AVAILABLE', 3);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('TRUCK', 'AVAILABLE', 3);
INSERT INTO parking_slot (slot_type, status, floor_id) VALUES ('TRUCK', 'AVAILABLE', 3);