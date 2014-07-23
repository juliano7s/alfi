SELECT COUNT(*) FROM pobs.orders WHERE delivered = 0 AND ready = 0;
UPDATE pobs.orders SET status='INPROGRESS' WHERE delivered = 0 AND ready = 0;

SELECT COUNT(*) FROM pobs.orders WHERE delivered = 0 AND ready = 1;
UPDATE pobs.orders SET status='READY' WHERE delivered = 0 AND ready = 1;

SELECT COUNT(*) FROM pobs.orders WHERE delivered = 1;
UPDATE pobs.orders SET status='DELIVERED' WHERE delivered = 1;

