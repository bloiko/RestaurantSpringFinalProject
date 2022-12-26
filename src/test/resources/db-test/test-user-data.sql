
/*insert roles USER and ADMIN*/
INSERT INTO role(role_id, name) VALUES (1,'USER'),
                                       (2,'ADMIN');

/*insert users*/
INSERT INTO user(id, first_name, last_name, username, password, email, address, phone_number, role_id)
VALUES (1,'first_name', 'last_name', 'usernameTest', 'password', 'email@email.com', null, null, 1);