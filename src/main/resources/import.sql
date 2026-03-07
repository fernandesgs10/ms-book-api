-- Script de carga inicial com UUIDs explícitos
-- Útil se você precisar referenciar esses IDs em outras tabelas depois

INSERT INTO tb_users (userId, username) VALUES
    (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), 'admin'),
    (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174001'), 'joao.silva'),
    (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174002'), 'maria.santos'),
    (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174003'), 'pedro.oliveira'),
    (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174004'), 'ana.costa'),
    (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174005'), 'carlos.pereira'),
    (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174006'), 'lucia.mendes');

-- Para consultar depois (converte de volta para formato legível):
-- SELECT BIN_TO_UUID(userId) as userId, username FROM tb_users;