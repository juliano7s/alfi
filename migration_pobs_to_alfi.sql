INSERT INTO alfi_dev.clients
	SELECT clientid, name, cpf, address, email
	FROM pobs.clients;

INSERT INTO alfi_dev.client_phones
	SELECT clientid, phone, 'Celular'
	FROM pobs.clients;

INSERT INTO alfi_dev.orders 
	SELECT orderid, clientid, ownerid, description, request_date, delivery_date, ready_date, NULL, 'INPROGRESS', value, cost, NULL, 0, 0
	FROM pobs.orders;

SELECT 
    COUNT(*)
FROM
    pobs.orders
WHERE
    delivered = 0 AND ready = 0;
UPDATE alfi_dev.orders 
SET 
    status = 'INPROGRESS'
WHERE
    id in (SELECT 
            id
        FROM
            pobs.orders
        WHERE
            delivered = 0 AND ready = 0);

SELECT 
    COUNT(*)
FROM
    pobs.orders
WHERE
    delivered = 0 AND ready = 1;
UPDATE alfi_dev.orders 
SET 
    status = 'READY'
WHERE
    id in (SELECT 
            id
        FROM
            pobs.orders
        WHERE
            delivered = 0 AND ready = 1);

SELECT 
    COUNT(*)
FROM
    pobs.orders
WHERE
    delivered = 1;
UPDATE alfi_dev.orders 
SET 
    status = 'DELIVERED'
WHERE
    id in (SELECT 
            id
        FROM
            pobs.orders
        WHERE
            delivered = 1);