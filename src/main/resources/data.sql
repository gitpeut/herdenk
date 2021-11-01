
INSERT INTO users (username, password, enabled)
VALUES
('user', '$2a$12$8Dp3ed9TUrwlG9Put.EVDelL8iZew0YndzMsJmHIb3iIgHTZ.jt1m', TRUE),
('admin', '$2a$12$8Dp3ed9TUrwlG9Put.EVDelL8iZew0YndzMsJmHIb3iIgHTZ.jt1m', TRUE),
('peter', '$2a$12$8Dp3ed9TUrwlG9Put.EVDelL8iZew0YndzMsJmHIb3iIgHTZ.jt1m', TRUE),
('nieuw', '$2a$12$8Dp3ed9TUrwlG9Put.EVDelL8iZew0YndzMsJmHIb3iIgHTZ.jt1m', TRUE);

INSERT INTO authorities (username, authority)
VALUES
('user', 'ROLE_USER'),
('admin', 'ROLE_ADMIN'),
('peter', 'ROLE_USER'),
('peter', 'ROLE_ADMIN'),
('nieuw', 'ROLE_USER'),
('nieuw', 'ROLE_nieuw');
