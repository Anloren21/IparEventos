-- Consulta de usuarios
SELECT
    id,
    nombre,
    email,
    rol,
    LEFT(password, 8) AS prefijo_hash,
    CHAR_LENGTH(password) AS longitud_hash
FROM usuarios
ORDER BY id;
