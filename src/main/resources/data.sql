insert into authorities (name) values ('ROLE_USER');
insert into authorities (name) values ('ROLE_ADMIN');
insert into authorities (name) values ('ROLE_DRIVER');

-- password == name
insert into users (name, surname, username, password, enabled, role, city) values ('admin1', 'admin1', 'admin1@gmail.com', '$2y$12$LdA5w1xM5qBwt1l.Srv62etWXUpyfCbU/usi3EprOEdZ3ZbCiDx/K', true, 'ADMIN', 'Subotica');
insert into users (name, surname, username, password, enabled, role, city) values ('admin2', 'admin2', 'admin2@gmail.com', '$2y$12$fcgxX0RiMeL1jbEI/wKt/uD3b1nCrCY22O6Y5ecR28btz4miOnI5q', true, 'ADMIN', 'Subotica');

insert into user_authority (user_id, authority_id) values (1, 2);
insert into user_authority (user_id, authority_id) values (2, 2);