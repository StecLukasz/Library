CREATE TABLE book
(
    id      BIGSERIAL    NOT NULL PRIMARY KEY,
    title        VARCHAR(255) NULL,
    isbn         VARCHAR(25) NULL,
    pages        INT          NOT NULL,
    genre        VARCHAR(255) NOT NULL,
    signature    VARCHAR(255) NOT NULL
);

ALTER TABLE book
    OWNER TO books;

INSERT INTO book (id, title, isbn, pages, genre, signature)
VALUES (1, 'Magnum detrius vix captiss galatae est', '0-1-2', 123, 'programing', 'Y/001'),
       (2, 'Finis de placidus coordinatae', '0-2-2', 543, 'programing', 'S/021'),
       (3, 'Gratis altus sectams grauiter ', '0-3-2', 532, 'programing', 'R/666'),
       (4, 'Fatalis, nobilis nuclear vexatum iaceres', '0-6-2', 932, 'programing', 'X/004');
