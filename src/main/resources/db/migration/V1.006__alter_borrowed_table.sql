DROP TABLE IF EXISTS borrowed;

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
VALUES (1, 'x', 1, '01-02-2000', '01-02-2000', '01-02-2000', '2023-04-07 10:30:00', 'available'),
       (2, 'x', 2, '05-06-1966', '01-02-2000', '01-02-2000', '2023-04-07 10:31:00', 'available'),
       (3, 'x', 3, '09-10-1995', '01-02-2000', '01-02-2000', '2023-04-07 10:32:00', 'available'),
       (4, 'x', 4, '04-05-2005', '01-02-2000', '01-02-2000', '2023-04-07 10:33:00', 'available'),
       (5, 'x', 5, '04-05-2005', '01-02-2000', '01-02-2000', '2023-04-07 10:34:00', 'available'),
       (6, 'x', 6, '01-02-2000', '01-02-2000', '01-02-2000', '2023-04-07 10:35:00', 'available'),
       (7, 'x', 7, '05-06-1966', '01-02-2000', '01-02-2000', '2023-04-07 10:36:00', 'available'),
       (8, 'x', 8, '09-10-1995', '01-02-2000', '01-02-2000', '2023-04-07 10:37:00', 'available');

