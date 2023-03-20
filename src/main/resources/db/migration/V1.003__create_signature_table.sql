CREATE TABLE signature
(
    id             INT          NOT NULL PRIMARY KEY,
    book_id        INT          NOT NULL,
    book_signature VARCHAR(255) NOT NULL,


    FOREIGN KEY (book_id) REFERENCES book (id)
);

INSERT INTO signature (id, book_id, book_signature)
VALUES (1, 1, 'yyy'),
       (2, 1, 'xxx'),
       (3, 2, 'eee'),
       (4, 3, 'hhh'),
       (5, 4, 'eee'),
       (6, 4, 'hhh');

