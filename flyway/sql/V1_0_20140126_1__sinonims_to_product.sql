ALTER TABLE product ADD COLUMN colorQuery VARCHAR(255);

UPDATE product AS pr
SET colorQuery = c.yandex_color FROM product p INNER JOIN color c
    ON p.color_id = c.id
WHERE pr.id = p.id;