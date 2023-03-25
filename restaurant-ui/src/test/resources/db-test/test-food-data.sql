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
        1)