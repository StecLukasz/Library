CREATE TABLE borrowed
(
    id            BIGSERIAL    NOT NULL,
    login         VARCHAR(255) NOT NULL,
    signature_id  INT          NOT NULL,
    borrowed_date DATE         NOT NULL,
    overdue_date  DATE NULL,
    return_date   DATE         NOT NULL,
    status_date   TIMESTAMP NULL,
    status        VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (signature_id) REFERENCES signature (id)
);
INSERT INTO borrowed (id, login, signature_id, borrowed_date, overdue_date, return_date, status_date, status)
VALUES (1, 'dmichna', 4, '01-02-2000', '01-02-2000', '01-02-2000', '01-01-2000', 'borrowed'),
       (2, 'dmichna', 4, '05-06-1966', '01-02-2000', '01-02-2000', '01-02-2000', 'available'),
       (3, 'lstec', 8, '09-10-1995', '01-02-2000', '01-02-2000', '01-01-2000', 'borrowed'),
       (4, 'lstec', 3, '04-05-2005', '01-02-2000', '01-02-2000', '01-01-2000', 'available'),
       (5, 'jchyla', 2, '04-05-2005', '01-02-2000', '01-02-2000', '01-01-2000', 'available'),
       (6, 'dmichna', 6, '01-02-2000', '01-02-2000', '01-02-2000', '01-01-2000', 'borrowed'),
       (7, 'dmichna', 1, '05-06-1966', '01-02-2000', '01-02-2000', '01-01-2000', 'available'),
       (8, 'lstec', 5, '09-10-1995', '01-02-2000', '01-02-2000', '01-01-2000', 'available'),
       (9, 'lstec', 5, '09-10-1995', '01-02-2000', '01-02-2000', '01-02-2000', 'borrowed'),
       (10, 'lstec', 7, '09-10-1995', '01-02-2000', '01-02-2000', '01-01-2000', 'available'),
       (11, 'lstec', 8, '09-10-1995', '01-02-2000', '01-02-2000', '01-01-2000', 'borrowed');

