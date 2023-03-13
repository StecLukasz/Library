CREATE TABLE borrowed
(
    id            INT          NOT NULL,
    username         VARCHAR(255) NOT NULL,
    signature_id  INT          NOT NULL,
    borrowed_date DATE         NOT NULL,
    overdue_date  DATE NULL,
    return_date   DATE         NOT NULL,
    status        VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (signature_id) REFERENCES signature (id)
);
INSERT INTO borrowed (id, username, signature_id, borrowed_date, overdue_date, return_date, status)
VALUES (1, 'Jack', 1, '01-02-2000', '01-02-2000', '01-02-2000', 'aaa'),
       (2, 'Lizard', 2, '05-06-1966', '01-02-2000', '01-02-2000', 'bbb'),
       (3, 'Greta', 3, '09-10-1995', '01-02-2000', '01-02-2000', 'ccc'),
       (4, 'Rachel', 4, '04-05-2005', '01-02-2000', '01-02-2000', 'eee'),
       (5, 'Jack', 5, '01-02-2000', '01-02-2000', '01-02-2000', 'aaa1'),
       (6, 'Lizard', 6, '05-06-1966', '01-02-2000', '01-02-2000', 'bbb1'),
       (7, 'Greta', 7, '09-10-1995', '01-02-2000', '01-02-2000', 'ccc1'),
       (8, 'Rachel', 8, '04-05-2005', '01-02-2000', '01-02-2000', 'eee1'),
       (9, 'Jack', 1, '01-02-2000', '01-02-2000', '01-02-2000', 'aaa'),
       (10, 'Lizard', 2, '05-06-1966', '01-02-2000', '01-02-2000', 'bbb'),
       (11, 'Greta', 3, '09-10-1995', '01-02-2000', '01-02-2000', 'ccc'),
       (12, 'Rachel', 4, '04-05-2005', '01-02-2000', '01-02-2000', 'eee'),
       (13, 'Jack', 5, '01-02-2000', '01-02-2000', '01-02-2000', 'aaa1'),
       (14, 'Lizard', 6, '05-06-1966', '01-02-2000', '01-02-2000', 'bbb1'),
       (15, 'Greta', 7, '09-10-1995', '01-02-2000', '01-02-2000', 'ccc1'),
       (16, 'Rachel', 8, '04-05-2005', '01-02-2000', '01-02-2000', 'eee1');
