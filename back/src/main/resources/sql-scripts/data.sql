INSERT INTO app_role (id, role_name, description) VALUES (1, 'STANDARD_USER', 'Standard User - Has no admin rights');
INSERT INTO app_role (id, role_name, description) VALUES (2, 'ADMIN_USER', 'Admin User - Has permission to perform admin tasks');

-- USER
-- non-encrypted password: jwtpass
INSERT INTO app_user (first_name, last_name, password, username, enabled, salt) VALUES ('John', 'Doe', '17c8e1fe1d8f9033fb28b610e3b4096d8da2f73e96c2aaf778f67ad40066609f', 'john.doe', true, 'blublu');
INSERT INTO app_user (first_name, last_name, password, username, enabled, salt) VALUES ('Admin', 'Admin', '604b8acb518f30589eb7d1ee672aa6726028c7160674b08e82d5a417859a6062', 'admin.admin', true, 'blabla');


INSERT INTO user_role(user_id, role_id) VALUES(1,1);
INSERT INTO user_role(user_id, role_id) VALUES(2,1);
INSERT INTO user_role(user_id, role_id) VALUES(2,2);
