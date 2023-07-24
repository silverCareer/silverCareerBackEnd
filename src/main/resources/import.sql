INSERT INTO AUTH (AUTH_IDX, USERNAME, PASSWORD, EMAIL, ACTIVATED, TOKEN_WEIGHT, PHONE_NUMBER, AGE, PROVIDER) VALUES (1, '곽동현', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'kkddhh1998@ac.kr', 1, 1, '01011112222', 26, 'MENTOR');

insert into authority (authority_name) values ('ROLE_MEMBER');
insert into authority (authority_name) values ('ROLE_ADMIN');

INSERT INTO AUTH_AUTHORITY (AUTH_IDX, AUTHORITY_NAME) values (1, 'ROLE_MEMBER');
