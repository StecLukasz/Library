CREATE TABLE borrowed
(
    id            INT NULL,
    login         VARCHAR(255)  NULL,
    signature_id  INT NULL,
    borrowed_date DATE          NULL,
    overdue_date  DATE NULL,
    return_date   DATE          NULL,
    status_date   date NULL,
    status        VARCHAR(255)  NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (signature_id) REFERENCES signature (id)
);
INSERT INTO borrowed (id, login, signature_id, borrowed_date, overdue_date, return_date, status_date, status)
VALUES (1, 'dmichna', 1, '01-02-2000', '01-02-2000', '01-02-2000', '01-02-2000', 'borrowed'),
       (2, 'dmichna', 1, '05-06-1966', '01-02-2000', '01-02-2000', '01-02-2000', 'returned'),
       (3, 'lstec', 2, '09-10-1995', '01-02-2000', '01-02-2000', '01-02-2000', 'borrowed'),
       (4, 'lstec', 2, '04-05-2005', '01-02-2000', '01-02-2000', '01-02-2000', 'returned'),
       (5, 'jchyla', 3, '04-05-2005', '01-02-2000', '01-02-2000', '01-02-2000', 'borrowed');

