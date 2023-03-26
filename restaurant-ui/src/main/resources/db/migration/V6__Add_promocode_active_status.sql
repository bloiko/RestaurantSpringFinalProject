ALTER TABLE promo_code
    ADD active TINYINT(1) DEFAULT 0;

INSERT INTO promo_code
VALUES ('promocode_5', 10, true),
       ('promocode_6', 20, true);


