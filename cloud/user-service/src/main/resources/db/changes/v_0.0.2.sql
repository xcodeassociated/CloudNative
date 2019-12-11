create sequence hibernate_sequence start 1 increment 1;

insert into users (name, password, role) values ('admin', 'dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=', 'ROLE_ADMIN');
insert into users (name, password, role) values ('user', 'cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=', 'ROLE_USER');
