CREATE TABLE `category` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `name` varchar(20) DEFAULT NULL,
                            `nameUa` varchar(20) DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `food_item` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `name` varchar(45) DEFAULT NULL,
                             `price` int DEFAULT NULL,
                             `image` varchar(250) DEFAULT NULL,
                             `category_id` char(50) DEFAULT NULL,
                             `nameFoodUa` varchar(60) DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `food_item_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `food_order` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `order_date` timestamp NULL DEFAULT NULL,
                              `user_id` int DEFAULT NULL,
                              `status_id` int DEFAULT NULL,
                              `order_price` bigint DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `food_order_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `item` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `food_id` int DEFAULT NULL,
                        `quantity` int DEFAULT NULL,
                        `order_id` int NOT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `item_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `role` (
                        `role_id` int NOT NULL AUTO_INCREMENT,
                        `name` varchar(20) DEFAULT NULL,
                        PRIMARY KEY (`role_id`),
                        UNIQUE KEY `role_role_id_uindex` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `status` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `status_name` varchar(20) DEFAULT NULL,
                          `status_name_ua` varchar(20) DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `status_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `first_name` varchar(25) DEFAULT NULL,
                        `last_name` varchar(25) DEFAULT NULL,
                        `username` varchar(25) DEFAULT NULL,
                        `password` varchar(30) DEFAULT NULL,
                        `email` varchar(25) DEFAULT NULL,
                        `address` varchar(25) DEFAULT NULL,
                        `phone_number` varchar(25) DEFAULT NULL,
                        `role_id` int DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `user_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




