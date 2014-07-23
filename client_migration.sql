SELECT id, phone FROM clients WHERE LENGTH(phone) > 11;

SELECT * FROM
(SELECT 
    id,
    TRIM(phone),
    SUBSTRING_INDEX(SUBSTRING_INDEX(TRIM(phone), ' ', 1), ' ', - 1) p1,
    SUBSTRING_INDEX(SUBSTRING_INDEX(TRIM(phone), ' ', 2), ' ', - 1) p2
FROM
	clients WHERE LENGTH(phone) > 11) phones
WHERE
	p1 <> p2 and length(p2) > 3;

INSERT INTO client_phones
SELECT id, p1 FROM
(SELECT 
    id,
    TRIM(phone),
    SUBSTRING_INDEX(SUBSTRING_INDEX(TRIM(phone), '  ', 1), '  ', - 1) p1,
    SUBSTRING_INDEX(SUBSTRING_INDEX(TRIM(phone), '  ', 2), '  ', - 1) p2
FROM
	clients WHERE LENGTH(phone) > 11) phones
WHERE
	p1 <> p2 and length(p2) > 3;

INSERT INTO client_phones
SELECT id, p2 FROM
(SELECT 
    id,
    TRIM(phone),
    SUBSTRING_INDEX(SUBSTRING_INDEX(TRIM(phone), '  ', 1), '  ', - 1) p1,
    SUBSTRING_INDEX(SUBSTRING_INDEX(TRIM(phone), '  ', 2), '  ', - 1) p2
FROM
	clients WHERE LENGTH(phone) > 11) phones
WHERE
	p1 <> p2 and length(p2) > 3;

INSERT INTO client_phones
SELECT id, phone FROM clients
WHERE
	length(phone) < 12;