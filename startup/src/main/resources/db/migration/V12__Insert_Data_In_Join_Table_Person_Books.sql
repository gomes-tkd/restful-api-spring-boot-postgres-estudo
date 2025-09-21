INSERT INTO person_books (person_id, book_id)
SELECT
    p.id AS person_id,
    b.id AS book_id
FROM
    (SELECT id FROM person WHERE id <= 12) p
        CROSS JOIN
    (SELECT id FROM books ORDER BY RANDOM() LIMIT 20) b;

-- Associar 3 livros às demais pessoas
INSERT INTO person_books (person_id, book_id)
SELECT
    p.id AS person_id,
    b.id AS book_id
FROM
    (SELECT id FROM person WHERE id > 12) p
        CROSS JOIN
    (SELECT id FROM books ORDER BY RANDOM() LIMIT 3) b;