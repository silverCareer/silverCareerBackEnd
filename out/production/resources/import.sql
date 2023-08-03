INSERT INTO authority (authority_name) values ('ROLE_MENTOR');
INSERT INTO authority (authority_name) values ('ROLE_MENTEE');
INSERT INTO members (email, password, username, activated, age, balance, career, category, phone_num, token_weight, authority)
VALUES ('tmp@gmail.com', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'tmp', 1, 35, 0, '10년 미만', '기술직', '01012341234', 1, 'ROLE_MENTOR');

