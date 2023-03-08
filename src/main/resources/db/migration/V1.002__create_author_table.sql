CREATE TABLE authors
(
    author_id  INT          NOT NULL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    gender     VARCHAR(255) NOT NULL,
    birth_date DATE         NOT NULL
);

INSERT INTO authors (author_id, first_name, last_name, gender, birth_date)
VALUES (1, 'Jack', 'Hoppkins', 'male', '01-02-2000'),
        (2, 'Lizard', 'King', 'male', '05-06-1966'),
        (3, 'Greta', 'Tumberg', 'female', '09-10-1995'),
        (4, 'Rachel', 'Snow', 'female', '04-05-2005');

CREATE TABLE book_author
(
    book_id   INT NOT NULL,
    author_id INT NOT NULL,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES book (id),
    FOREIGN KEY (author_id) REFERENCES authors (author_id)
);

INSERT INTO book_author (book_id, author_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 2);
