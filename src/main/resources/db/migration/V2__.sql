CREATE TABLE tb_user
(
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_tb_user PRIMARY KEY (username)
);