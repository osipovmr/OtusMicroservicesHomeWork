--liquibase formatted sql
--changeset author:OsipovIlya

insert into user_table (username, first_name, last_name, email, phone)
values
('osipovmr', 'ILYA', 'OSIPOV', 'osipovmr@yandex.ru', '+79112477069'),
('otus', 'John', 'Spilberg', 'otus@gmail.com', '8(812)6009812'),
('travel', 'Turkish', 'Airlines', 'fly@plane.sky', '00000');