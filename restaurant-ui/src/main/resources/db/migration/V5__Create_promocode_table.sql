CREATE TABLE IF NOT EXISTS `promo_code` (
                                          `code` varchar(30) NOT NULL ,
                                          `discount` int NOT NULL,
    PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


ALTER TABLE food_order
    ADD promo_code varchar(30) DEFAULT NULL;
ALTER TABLE food_order
    ADD FOREIGN KEY (promo_code) REFERENCES promo_code(code);