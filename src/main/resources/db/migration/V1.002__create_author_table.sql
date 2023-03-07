CREATE TABLE authors
(
    author_id  INT          NOT NULL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL
);

INSERT INTO authors (author_id, first_name)
VALUES (1, 'Hoppkins'),
       (2, 'King'),
       (3, 'Tumberg'),
       (4, 'Rachel');

CREATE TABLE book_author
(
    book_id   INT NOT NULL,
    author_id INT NOT NULL,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id),
    FOREIGN KEY (author_id) REFERENCES authors (author_id)
);

INSERT INTO book_author (book_id, author_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 2);
