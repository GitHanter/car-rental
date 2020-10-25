-- ---------------------------------------- User --------------------------------------
-- Password-PlainText: vnKWx5hY7Ek5MHhp
INSERT INTO USERS(USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL, PHONE, DRIVING_LICENCE_NO, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, VERSION)
VALUES ('customerA', '$2a$10$icV.3JxkVcL2VuX4bXC41OHA.Z2cZ3UEdhR7qXdV0cb8CEGfcGOYy', 'CUSA_First', 'CUSA_Last', 'customerA@plc.com', '123456789', '21028219910101888X', 'Admin', NOW(), 'Admin', NOW(), 0);

-- Password-PlainText: JK5hVC84EtDKx4VQ
INSERT INTO USERS(USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL, PHONE, DRIVING_LICENCE_NO, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, VERSION)
VALUES ('customerB', '$2a$10$ZFvRIl7bn37EDAPSse8wJetGhgie2mpyD24mUFhvPODWPzoKJqnzW', 'CUSB_First', 'CUSB_Last', 'customerB@plc.com', '234567890', '21028219950101666X', 'Admin', NOW(), 'Admin', NOW(), 0);




-- ------------------------------------------- Cars --------------------------------
INSERT INTO CAR(CAR_BRAND, CAR_MODEL, CAR_TYPE, COLOR, CAPACITY, PLATE_NUMBER, MILEAGE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, VERSION) VALUES
('BMW', 'BMW 650', 'LCAR', 'WHITE', 5, 'YB 000AA', 5000, 'Admin', NOW(), 'Admin', NOW(), 0);

INSERT INTO CAR(CAR_BRAND, CAR_MODEL, CAR_TYPE, COLOR, CAPACITY, PLATE_NUMBER, MILEAGE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, VERSION) VALUES
('BMW', 'BMW 650', 'LCAR', 'BLACK', 5, 'YB 001AA', 6000, 'Admin', NOW(), 'Admin', NOW(), 0);

INSERT INTO CAR(CAR_BRAND, CAR_MODEL, CAR_TYPE, COLOR, CAPACITY, PLATE_NUMBER, MILEAGE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, VERSION) VALUES
('Toyota', 'Toyota Camry', 'ECAR', 'SILVER', 5, 'YB 002AA', 7000, 'Admin', NOW(), 'Admin', NOW(), 0);

INSERT INTO CAR(CAR_BRAND, CAR_MODEL, CAR_TYPE, COLOR, CAPACITY, PLATE_NUMBER, MILEAGE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, VERSION) VALUES
('Toyota', 'Toyota Camry', 'ECAR', 'GRAY', 5, 'YB 003AA', 8000, 'Admin', NOW(), 'Admin', NOW(), 0);





-- ------------------------------------------- Car Price --------------------------------
-- ---------------- 90 days from Now ----------------
-- YB 000AA: Start from 300, each day add 1$
INSERT INTO CAR_PRICE(CAR_ID, DATE_OF_YEAR, PRICE, IS_AVAILABLE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, VERSION)
SELECT  c.ID, dt.doy,dt.price,1,'Admin',NOW(),'Admin',NOW(),0
FROM
    (SELECT @d:=(@d + interval 1 day) doy, @price:=@price+1 AS price
     FROM (SELECT @d:=date(NOW()), @price:=300) r
              CROSS JOIN information_schema.tables t
     WHERE @d < DATE_ADD(NOW(), INTERVAL 89 DAY)
    ) dt
        CROSS JOIN CAR c
WHERE c.PLATE_NUMBER = 'YB 000AA';

-- YB 001AA: Start from 280, each day add 1$
INSERT INTO CAR_PRICE(CAR_ID, DATE_OF_YEAR, PRICE, IS_AVAILABLE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, VERSION)
SELECT  c.ID, dt.doy,dt.price,1,'Admin',NOW(),'Admin',NOW(),0
FROM
    (SELECT @d:=(@d + interval 1 day) doy, @price:=@price+1 AS price
     FROM (SELECT @d:=date(NOW()), @price:=280) r
              CROSS JOIN information_schema.tables t
     WHERE @d < DATE_ADD(NOW(), INTERVAL 89 DAY)
    ) dt
        CROSS JOIN CAR c
WHERE c.PLATE_NUMBER = 'YB 001AA';

-- YB 002AA: Start from 260, each day add 1$
INSERT INTO CAR_PRICE(CAR_ID, DATE_OF_YEAR, PRICE, IS_AVAILABLE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, VERSION)
SELECT  c.ID, dt.doy,dt.price,1,'Admin',NOW(),'Admin',NOW(),0
FROM
    (SELECT @d:=(@d + interval 1 day) doy, @price:=@price+1 AS price
     FROM (SELECT @d:=date(NOW()), @price:=260) r
              CROSS JOIN information_schema.tables t
     WHERE @d < DATE_ADD(NOW(), INTERVAL 89 DAY)
    ) dt
        CROSS JOIN CAR c
WHERE c.PLATE_NUMBER = 'YB 002AA';

-- YB 003AA: Start from 240, each day add 1$
INSERT INTO CAR_PRICE(CAR_ID, DATE_OF_YEAR, PRICE, IS_AVAILABLE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, VERSION)
SELECT  c.ID, dt.doy,dt.price,1,'Admin',NOW(),'Admin',NOW(),0
FROM
    (SELECT @d:=(@d + interval 1 day) doy, @price:=@price+1 AS price
     FROM (SELECT @d:=date(NOW()), @price:=240) r
              CROSS JOIN information_schema.tables t
     WHERE @d < DATE_ADD(NOW(), INTERVAL 89 DAY)
    ) dt
        CROSS JOIN CAR c
WHERE c.PLATE_NUMBER = 'YB 003AA';