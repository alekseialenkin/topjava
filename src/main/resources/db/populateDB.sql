DELETE
FROM user_role;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100000, 'Завтрак', 1000, '30-01-2020 10:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100000, 'Обед', 500, '30-01-2020 13:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100000, 'Ужин', 500, '30-01-2020 20:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100000, 'Еда на граничное значение', 1000, '31-01-2020 00:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100000, 'Завтрак', 1000, '31-01-2020 10:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100000, 'Обед', 500, '31-01-2020 13:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100000, 'Ужин', 500, '31-01-2020 20:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100001, 'Breakfast', 1000, '30-01-2020 10:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100001, 'Lunch', 500, '30-01-2020 13:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100001, 'Diner', 500, '30-01-2020 20:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100001, 'Breakfast', 1000, '31-01-2020 10:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100001, 'Meal', 1000, '31-01-2020 00:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100001, 'Lunch', 500, '31-01-2020 13:00');

INSERT INTO meals(user_id, description, calories, date_time)
VALUES (100001, 'Diner', 500, '31-01-2020 20:00');


