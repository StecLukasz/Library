CREATE TABLE borrowed
(
    id   INT          NOT NULL,
    login         VARCHAR(255) NOT NULL,
    signature_id  INT          NOT NULL,
    borrowed_date DATE         NOT NULL,
    overdue_date  DATE NULL,
    return_date   DATE         NOT NULL,
    status        VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (signature_id) REFERENCES signature (id)
);
