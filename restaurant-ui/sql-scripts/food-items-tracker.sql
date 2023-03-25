
DROP TABLE IF EXISTS food_item;
CREATE TABLE food_item (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(45) DEFAULT NULL,
  price int(11) DEFAULT NULL,
  image varchar(45) DEFAULT NULL,
  category int(11) DEFAULT NULL,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci ROW_FORMAT=DYNAMIC;

/*insert desserts*/
INSERT INTO food_item VALUES (1,'Ice cream',4,'https://storcpdkenticomedia.blob.core.windows.net/media/recipemanagementsystem/media/recipe-media-files/recipes/retail/x17/17244-caramel-topped-ice-cream-dessert-760x580.jpg?ext=.jpg',6),
                             (2,'Lemon cake',3,'https://www.rockrecipes.com/wp-content/uploads/2013/09/Lemon-Velvet-Cake-close-up-photo-of-single-slice-on-white-plate.jpg',6),
                             (3,'Chocolate Cake',5,'https://www.hanielas.com/wp-content/uploads/2020/03/chocolate-cake-recipe-tutorial-500x500.jpg',6),
                                (4,'Biscuit',5,'https://storcpdkenticomedia.blob.core.windows.net/media/recipemanagementsystem/media/recipe-media-files/recipes/retail/x17/2020_retail_perfect-buttermilk-biscuit_760x580.jpg?ext=.jpg',6);

/*insert snacks*/
INSERT INTO food_item VALUES (5, 'Burger', 20, 'https://burgersushipoint.com.ua/wp-content/uploads/2021/02/burger-sushi-point_meny_17-2-500x500.jpg', 5),
                             (6, 'Cheese Burger', 15, 'https://simply-delicious-food.com/wp-content/uploads/2015/07/Bacon-and-cheese-burgers-3-500x500.jpg', 5),
                             (7, 'Potato free', 30, 'https://i.pinimg.com/736x/31/7f/f6/317ff60cbdfe3d07e29062f278c529c0.jpg', 5),
                            (8, 'Sandwich', 21, 'https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fimg1.cookinglight.timeinc.net%2Fsites%2Fdefault%2Ffiles%2Fstyles%2Fmedium_2x%2Fpublic%2F1556744250%2Fthe-ultimate-veggie-sandwich-1905.jpg%3Fitok%3D1ip9ZNIm', 5)