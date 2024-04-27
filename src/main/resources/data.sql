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
       ('rest_2');

INSERT INTO MEALS (name, price, meal_date, restaurant_id)
VALUES ('meal1', 100, CURRENT_DATE, 1),
       ('meal2', 200, CURRENT_DATE, 1),
       ('meal3', 300, CURRENT_DATE, 2);

INSERT INTO VOTES (vote_date, restaurant_id, user_id)
VALUES (CURRENT_DATE, 1, 1),
       (CURRENT_DATE, 2, 2);