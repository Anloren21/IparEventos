-- Consulta de Evento pasado y plazas disponibles
SELECT
    e.titulo,
    e.fecha,
    CASE
        WHEN e.fecha < NOW() THEN 'Celebrado'
        ELSE 'Próximo'
    END AS estado,
    e.aforo,
    COALESCE(SUM(i.plazas), 0) AS plazas_reservadas,
    e.aforo - COALESCE(SUM(i.plazas), 0) AS plazas_disponibles
FROM eventos e
LEFT JOIN inscripciones i ON i.evento_id = e.id
GROUP BY e.id, e.titulo, e.fecha, e.aforo
ORDER BY e.fecha;