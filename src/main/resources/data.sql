
INSERT INTO users ( full_name, email, password, enabled, role)
VALUES
('public', 'public@public.com', '$2a$12$Dtb0dxFXvZKK.PbCj1DKm.C9S1IkYtDe7hPyl5xz4YhKOBG3HWnE2', FALSE, 'ROLE_USER'),
('admin','admin@admin.com', '$2a$12$8Dp3ed9TUrwlG9Put.EVDelL8iZew0YndzMsJmHIb3iIgHTZ.jt1m', TRUE, 'ROLE_ADMIN'),
('user', 'user@user.com', '$2a$12$Dtb0dxFXvZKK.PbCj1DKm.C9S1IkYtDe7hPyl5xz4YhKOBG3HWnE2', TRUE, 'ROLE_USER'),
('user2', 'user2@user.com', '$2a$12$Dtb0dxFXvZKK.PbCj1DKm.C9S1IkYtDe7hPyl5xz4YhKOBG3HWnE2', TRUE, 'ROLE_USER');

