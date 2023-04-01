drop  table if exists audit;
CREATE TABLE IF NOT EXISTS `audit` (
                                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                                          `user_id` BIGINT NOT NULL,
                                          `entity_id` BIGINT NOT NULL,
                                          `entity_type` varchar(30) NOT NULL,
                                          `action_type` varchar(30) NOT NULL,
                                          `audit_date` timestamp NOT NULL,

    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=UTF8;



