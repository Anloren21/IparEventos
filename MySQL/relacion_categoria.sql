-- Relacion con las categorias
SELECT
    e.titulo,
    c.nombre AS categoria
FROM eventos e
JOIN eventos_categorias ec ON ec.evento_id = e.id
JOIN categorias c ON c.id = ec.categoria_id
ORDER BY e.titulo, c.nombre;