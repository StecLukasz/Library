CREATE TABLE signature
(
    id             INT          NOT NULL PRIMARY KEY,
    book_id        INT          NOT NULL,
    book_signature VARCHAR(255) NOT NULL,


    FOREIGN KEY (book_id) REFERENCES book (id)
);

INSERT INTO signature (id, book_id, book_signature)
VALUES (1, 4, 'yyy'),
       (2, 3, 'xxx'),
       (3, 2, 'eee'),
       (4, 1, 'hhh'),
       (5, 4, 'yyy1'),
       (6, 3, 'xxx1'),
       (7, 2, 'eee1'),
       (8, 1, 'hhh1');
