drop  table if exists promo_code;
CREATE TABLE IF NOT EXISTS `promo_code` (
                                          `code` varchar(30) NOT NULL ,
                                          `discount` int NOT NULL,
    PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

INSERT INTO promo_code
VALUES ('promocode_1', 10),
       ('promocode_2', 20);


ALTER TABLE food_order
    ADD promo_code varchar(30) DEFAULT NULL;
ALTER TABLE food_order
    ADD FOREIGN KEY (promo_code) REFERENCES promo_code(code);