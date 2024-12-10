-- Create Room Table

CREATE TABLE IF NOT EXISTS room (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

-- Create Booking Table
CREATE TABLE IF NOT EXISTS booking (
    id INT PRIMARY KEY AUTO_INCREMENT,
    room_id INT NOT NULL,
    employee_email VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room(id),
    CONSTRAINT unique_booking_time UNIQUE (room_id, date, start_time, end_time)
);

INSERT INTO room (name) VALUES ('Conference Room A');
INSERT INTO room (name) VALUES ('Meeting Room B');

INSERT INTO booking (room_id, employee_email, date, start_time, end_time)
VALUES (1, 'alice@example.com', '2024-12-08', '09:00', '11:00');