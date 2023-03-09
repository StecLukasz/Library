CREATE TABLE book
(
    id      BIGSERIAL    NOT NULL PRIMARY KEY,
    title        VARCHAR(255) NULL,
    pages        INT          NOT NULL,
    genre        VARCHAR(255) NOT NULL
);

ALTER TABLE book
    OWNER TO books;

INSERT INTO book (id, title, pages, genre)
VALUES (1, 'Magnum detrius vix captiss galatae est', 123, 'programing'),
       (2, 'Finis de placidus coordinatae', 543, 'programing'),
       (3, 'Gratis altus sectams grauiter ', 532, 'programing'),
       (4, 'Fatalis, nobilis nuclear vexatum iaceres', 932, 'programing');
