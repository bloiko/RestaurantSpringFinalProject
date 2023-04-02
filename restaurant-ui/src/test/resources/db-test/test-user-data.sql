delete from user;
delete from role;

/*insert roles USER and ADMIN*/
INSERT INTO role(role_id, name) VALUES (1,'USER'),
                                       (2,'ADMIN');


/*insert users*/
INSERT INTO user(first_name, last_name, username, password, email, address, phone_number, role_id)
VALUES ('first_name', 'last_name', 'usernameTest', '$2a$12$6fTslEFbPc.wTA6KNNk0Tu3.hVXRN.cj28rEEcBJsD1Qgczl3D3Gi',
        'email@email.com', 'address', '0896755549', 1);