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
VALUES (1, 'x', 1, '01-02-2000', '01-02-2000', '01-02-2000', '01-01-2000', 'available'),
       (2, 'x', 2, '05-06-1966', '01-02-2000', '01-02-2000', '02-01-2000', 'available'),
       (3, 'x', 3, '09-10-1995', '01-02-2000', '01-02-2000', '03-01-2000', 'available'),
       (4, 'x', 4, '04-05-2005', '01-02-2000', '01-02-2000', '04-01-2000', 'available'),
       (5, 'x', 5, '04-05-2005', '01-02-2000', '01-02-2000', '05-01-2000', 'available'),
       (6, 'x', 6, '01-02-2000', '01-02-2000', '01-02-2000', '06-01-2000', 'available'),
       (7, 'x', 7, '05-06-1966', '01-02-2000', '01-02-2000', '07-01-2000', 'available'),
       (8, 'x', 8, '09-10-1995', '01-02-2000', '01-02-2000', '08-01-2000', 'available');
