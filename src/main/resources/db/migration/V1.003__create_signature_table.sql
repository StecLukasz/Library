CREATE TABLE signature
(
    id             INT          NOT NULL PRIMARY KEY,
    book_id        INT          NOT NULL,
    book_signature VARCHAR(255) NOT NULL,


    FOREIGN KEY (book_id) REFERENCES book (id)
);

