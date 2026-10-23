USE ipareventos;

SELECT 'categorias' AS tabla, COUNT(*) AS total FROM categorias
UNION ALL
SELECT 'usuarios', COUNT(*) FROM usuarios
UNION ALL
SELECT 'eventos', COUNT(*) FROM eventos
UNION ALL
SELECT 'inscripciones', COUNT(*) FROM inscripciones;