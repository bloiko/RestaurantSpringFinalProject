delete from user;
delete from role;

/*insert roles USER and ADMIN*/
INSERT INTO role(role_id, name) VALUES (1,'USER'),
                                       (2,'ADMIN');


/*insert users*/
INSERT INTO user(first_name, last_name, username, password, email, address, phone_number, role_id)
VALUES ('first_name', 'last_name', 'usernameTest', '$2a$12$6fTslEFbPc.wTA6KNNk0Tu3.hVXRN.cj28rEEcBJsD1Qgczl3D3Gi',
        'email@email.com', 'address', '0896755549', 1),
        ('first_name_2', 'last_name_2', 'usernameTest_2', '$2a$12$6fTslEFbPc.wTA6KNNk0Tu3.hVXRN.cj28rEEcBJsD1Qgczl3D3Gi_2',
        'email_2@email.com', 'address_2', '0896755549', 1);

INSERT INTO audit(user_id, entity_id, entity_type, action_type, audit_date)
VALUES (1, 5, 'USER', 'CREATE_USER', '2023-04-01 17:57:25'),
       (1, 5, 'USER', 'UPDATE_USER_DETAILS', '2023-04-03 17:52:25'),
       (1, 5, 'USER', 'UPDATE_USER_DETAILS', '2023-04-03 17:54:25'),
       (1, 5, 'USER', 'UPDATE_USER_DETAILS', '2023-04-01 17:57:25'),
       (1, 5, 'USER', 'UPDATE_USER_PASSWORD', '2023-04-05 17:57:25'),
       (1, 5, 'USER', 'DELETE_USER', '2023-04-06 17:57:25');