CREATE TABLE customer
(
    customer_id  INT          NOT NULL PRIMARY KEY,
    username     VARCHAR(255) NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    last_name_pl VARCHAR(255) NOT NULL,
    role         VARCHAR(255) NOT NULL
);

CREATE TABLE borrowed
(
    borrowed_id  INT          NOT NULL PRIMARY KEY,
    customer_id  INT          NOT NULL,
    book_id      INT          NOT NULL,
    username     VARCHAR(255) NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    last_name_pl VARCHAR(255) NOT NULL,
    role         VARCHAR(255) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id)
);
