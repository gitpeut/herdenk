
INSERT INTO users ( full_name, email, password, enabled, role)
VALUES
('public', 'public@public.com', '$2a$12$Dtb0dxFXvZKK.PbCj1DKm.C9S1IkYtDe7hPyl5xz4YhKOBG3HWnE2', FALSE, 'ROLE_USER'),
('admin','admin@admin.com', '$2a$12$8Dp3ed9TUrwlG9Put.EVDelL8iZew0YndzMsJmHIb3iIgHTZ.jt1m', TRUE, 'ROLE_ADMIN'),
('user', 'user@user.com', '$2a$12$Dtb0dxFXvZKK.PbCj1DKm.C9S1IkYtDe7hPyl5xz4YhKOBG3HWnE2', TRUE, 'ROLE_USER'),
('user2', 'user2@user.com', '$2a$12$Dtb0dxFXvZKK.PbCj1DKm.C9S1IkYtDe7hPyl5xz4YhKOBG3HWnE2', TRUE, 'ROLE_USER');

INSERT INTO graves ( creation_date, occupant_full_name )
VALUES
('2021-11-10', 'Napoleon Bonaparte'),
('2021-11-09','Willem de Zwijger'),
('2021-11-08','Zangeres zonder Naam');

INSERT INTO authorities( user_id, grave_id, authority)
VALUES
(2,1,'OWNER'),
(2,2,'OWNER'),
(3,3,'OWNER');



