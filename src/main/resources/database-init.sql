DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS balance;
DROP TABLE IF EXISTS payers;

CREATE TABLE payers
(
    id   INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE transactions
(
    id   INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    payer_id   INT          NOT NULL,
    payer     VARCHAR(50)  NOT NULL,
    points    INT          NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    status bit NOT NULL DEFAULT (1),
    FOREIGN KEY (payer_id) REFERENCES payers(id) ON DELETE CASCADE
);

CREATE TABLE balance(
    id   INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    payer_id INT NOT NULL,
    balance INT NOT NULL,
    FOREIGN KEY (payer_id) REFERENCES payers(id) ON DELETE CASCADE
);