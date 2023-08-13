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

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 1, 'Завтрак', 1000, '30-01-2020 10:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 2, 'Обед', 500, '30-01-2020 13:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 3, 'Ужин', 500, '30-01-2020 20:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 4, 'Еда на граничное значение', 1000, '31-01-2020 00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 5, 'Завтрак', 1000, '31-01-2020 10:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 6, 'Обед', 500, '31-01-2020 13:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100000, 7, 'Ужин', 500, '31-01-2020 20:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 8, 'Breakfast', 1000, '30-01-2020 10:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 9, 'Lunch', 500, '30-01-2020 13:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 10, 'Diner', 500, '30-01-2020 20:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 11, 'Breakfast', 1000, '31-01-2020 10:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 12, 'Meal', 1000, '31-01-2020 00:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 13, 'Lunch', 500, '31-01-2020 13:00');

INSERT INTO meals(userid, id, description, calories, datetime)
VALUES (100001, 14, 'Diner', 500, '31-01-2020 20:00');


