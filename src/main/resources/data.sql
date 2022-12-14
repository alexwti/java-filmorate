/*DELETE FROM FILM_LIKES WHERE LIKE_ID IS NOT NULL;
DELETE FROM FILM_GENRE WHERE GENRE_ID IS NOT NULL;
DELETE FROM FILMS WHERE FILM_ID IS NOT NULL;
DELETE FROM FRIENDSHIP WHERE FRIENDSHIP_ID IS NOT NULL;
DELETE FROM STATUS_REL WHERE STATUS_REL_ID IS NOT NULL;
DELETE FROM USERS WHERE USER_ID IS NOT NULL;*/


MERGE INTO RATING_MPA KEY (RATING_ID)
VALUES (1, 'G', 'Нет возрастных ограничений'),
       (2, 'PG', 'Рекомендуется присутствие родителей'),
       (3, 'PG-13', 'Детям до 13 лет просмотр не желателен'),
       (4, 'R', 'Лицам до 17 лет обязательно присутствие взрослого'),
       (5, 'NC-17', 'Лицам до 18 лет просмотр запрещен');

MERGE INTO GENRES KEY (GENRE_ID)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

MERGE INTO STATUS_REL KEY (STATUS_REL_ID)
VALUES (1, 'Ожидается подтверждение'),
       (2, 'Подтверждено');