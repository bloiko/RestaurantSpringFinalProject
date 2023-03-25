



drop table if exists category;
create table category(
                     id int PRIMARY KEY AUTO_INCREMENT,
                     name varchar NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci ROW_FORMAT=DYNAMIC;

drop table if exists item;
create table item(
                     id int PRIMARY KEY AUTO_INCREMENT,
                     food_id int NOT NULL,
                     quantity int NOT NULL ,
                     foreign key(food_id) REFERENCES food_item(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci ROW_FORMAT=DYNAMIC;

drop table if exists food_order;
create table food_order(
                           id int PRIMARY KEY AUTO_INCREMENT,
                           order_date TIMESTAMP NOT NULL,
                           user_id int NOT NULL,
                           status_id int NOT NULL,
                           foreign key (user_id) REFERENCES user(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci ROW_FORMAT=DYNAMIC;

drop table if exists food_order;
create table food_order(
                           id int PRIMARY KEY AUTO_INCREMENT,
                           order_date TIMESTAMP NOT NULL,
                           user_id int NOT NULL,
                           status_id int NOT NULL,
                           foreign key (user_id) REFERENCES user(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci ROW_FORMAT=DYNAMIC;