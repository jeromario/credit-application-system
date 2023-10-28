CREATE TABLE Credit (
  id BIGINT AUTO_INCREMENT NOT NULL,
   creditCode UUID NOT NULL,
   creditValue DECIMAL NOT NULL,
   dayFirstInstallment date NOT NULL,
   numberOfInstallments INT NOT NULL,
   status INT,
   customer_id BIGINT,
   CONSTRAINT pk_credit PRIMARY KEY (id)
);

ALTER TABLE Credit ADD CONSTRAINT uc_credit_creditcode UNIQUE (creditCode);

ALTER TABLE Credit ADD CONSTRAINT FK_CREDIT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES Client (id);