CREATE TABLE borrowed
(
    id            INT          NOT NULL,
    login         VARCHAR(255) NOT NULL,
    signature_id  INT          NOT NULL,
    borrowed_date DATE         NOT NULL,
    overdue_date  DATE NULL,
    return_date   DATE         NOT NULL,
    status        VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (signature_id) REFERENCES signature (id)
);
INSERT INTO borrowed (id, login, signature_id, borrowed_date, overdue_date, return_date, status)
VALUES (1, 'Jack', 1, '01-02-2000', '01-02-2000', '01-02-2000', 'aaa'),
       (2, 'Lizard', 2, '05-06-1966', '01-02-2000', '01-02-2000', 'bbbb'),
       (3, 'Greta', 3, '09-10-1995', '01-02-2000', '01-02-2000', 'cccc'),
       (4, 'Rachel', 4, '04-05-2005', '01-02-2000', '01-02-2000', 'eeee');
