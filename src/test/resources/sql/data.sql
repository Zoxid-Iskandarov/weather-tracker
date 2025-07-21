INSERT INTO users (id, username, password, created_at)
VALUES (1,'johndoe@gmail.com', 'password123', '2025-06-21 10:00:00'),
       (2, 'janeSmith@gmail.com', 'securePass456', '2025-06-20 15:30:00'),
       (3, 'alex.wilson@gmail.com', 'mySecret789', '2025-07-19 08:45:00');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO locations (id, name, user_id, latitude, longitude, created_at)
VALUES (1, 'Los Angeles', 1, 34.0537, -118.2428, '2025-07-16 06:50:54.468611'),
       (2, 'Toshkent Shahri', 1, 41.3123, 69.2787, '2025-07-12 19:03:07.930774'),
       (3, 'London', 1, 51.5099, -0.1278, '2025-07-13 01:33:53.148181'),
       (4, 'Moscow', 2, 55.7487, 37.6187, '2025-07-13 02:37:56.904586');
SELECT SETVAL('locations_id_seq', (SELECT MAX(id) FROM locations));