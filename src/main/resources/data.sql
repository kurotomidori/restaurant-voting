INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANTS (name)
VALUES ('rest_1'),
       ('rest_2'),
       ('rest_3');

INSERT INTO DISHES (name, price, dish_date, restaurant_id)
VALUES ('dish1', 100, CURRENT_DATE, 1),
       ('dish2', 200, CURRENT_DATE, 1),
       ('old_dish', 300, '2024-01-01', 1),
       ('dish3', 300, CURRENT_DATE, 2),
       ('dish2', 200, CURRENT_DATE, 2),
       ('old_dish', 300, '2024-01-01', 2),
       ('dish2', 200, CURRENT_DATE, 3),
       ('old_dish', 300, '2024-01-01', 3);

INSERT INTO VOTES (vote_date, restaurant_id, user_id)
VALUES (CURRENT_DATE, 1, 1),
       (CURRENT_DATE, 2, 2);