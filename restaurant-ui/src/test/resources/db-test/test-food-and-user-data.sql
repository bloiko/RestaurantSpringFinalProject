delete from food_item;
delete from status;
delete from category;
delete from role;
delete from user;
delete from food_order;
delete from item;

/*insert statuses by Id and status_name*/
INSERT INTO status(id, status_name)
VALUES (1, 'WAITING'),
       (2, 'PREPARING'),
       (3, 'READY'),
       (4, 'DELIVERED'),
       (5, 'DONE');


/*insert desserts*/
INSERT INTO category
VALUES (1, 'Snacks'),
       (2, 'Desserts'),
       (3, 'Some Category'),
       (4, 'New Category');

/*insert desserts*/
INSERT INTO food_item(id, name, price, image, category_id)
VALUES (1, 'Ice cream', 4,
        'https://storcpddessert-760x580.jpg?ext=.jpg',
        2),
       (2, 'Lemon cake', 3,
        'https://www.rockreslice-on-white-plate.jpg',
        2),
       (3, 'Chocolate Cake', 5,
        'https:/.ctorial-500x500.jpg', 2),
       (4, 'Biscuit', 5,
        'https://ste760x580.jpg?ext=.jpg',
        2);

/*insert snacks*/
INSERT INTO food_item(id, name, price, image, category_id)
VALUES (5, 'Burger', 20,
        'https://burgersushipoint.com.ua/wp-content/upl0x500.jpg', 1),
       (6, 'Cheese Burger', 15,
        'https://simply-delicious-food.com/wp-content/upjpg', 1),
       (7, 'Potato free', 30, 'https://i.pinimg.com/736x/31/7f/g', 1),
       (8, 'Sandwich', 21,
        'https://imagesvc.meredithcorp.io/v3/',
        1);



/*insert roles USER and ADMIN*/
    INSERT INTO role(role_id, name) VALUES (1,'USER'),
    (2,'ADMIN');

/*insert users*/
INSERT INTO user(id, first_name, last_name, username, password, email, address, phone_number, role_id)
VALUES (1,'first_name', 'last_name', 'usernameTest', '$2a$12$6fTslEFbPc.wTA6KNNk0Tu3.hVXRN.cj28rEEcBJsD1Qgczl3D3Gi',
        'email@email.com', 'address', '0896755549', 1);

INSERT INTO food_order(id, order_date, user_id, status_id, order_price)
VALUES (1, '2022-12-26 14:15:34', 1, 1, 10), (2, '2022-05-10 14:15:34', 1, 2, 11);


INSERT INTO item(id, food_id, quantity, order_id)
VALUES (1, 1, 1, 1);