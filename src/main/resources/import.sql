-- ========== IMPORT.SQL TRANSPARENTE ==========
-- Usa INSERT IGNORE para não dar erro se os dados já existirem

-- ========== EDITORAS (PUBLISHERS) ==========
INSERT IGNORE INTO publishers (id, name, cnpj, city, state, website, description, created_at, updated_at)
VALUES (1, 'Editora Pearson', '12.345.678/0001-90', 'São Paulo', 'SP', 'www.pearson.com.br', 'Editora especializada em tecnologia', NOW(), NOW());

INSERT IGNORE INTO publishers (id, name, cnpj, city, state, website, description, created_at, updated_at)
VALUES (2, 'Editora Campus', '98.765.432/0001-10', 'Rio de Janeiro', 'RJ', 'www.campus.com.br', 'Editora acadêmica', NOW(), NOW());

INSERT IGNORE INTO publishers (id, name, cnpj, city, state, website, description, created_at, updated_at)
VALUES (3, 'Editora Saraiva', '45.678.901/0001-23', 'São Paulo', 'SP', 'www.saraiva.com.br', 'Editora tradicional brasileira', NOW(), NOW());

-- ========== AUTORES ==========
INSERT IGNORE INTO authors (id, name, nationality, birth_date, biography, created_at, updated_at)
VALUES (1, 'Robert C. Martin', 'Americano', '1952-12-05', 'Conhecido como Uncle Bob, especialista em programação', NOW(), NOW());

INSERT IGNORE INTO authors (id, name, nationality, birth_date, biography, created_at, updated_at)
VALUES (2, 'Martin Fowler', 'Britânico', '1963-12-18', 'Autor e palestrante sobre desenvolvimento de software', NOW(), NOW());

INSERT IGNORE INTO authors (id, name, nationality, birth_date, biography, created_at, updated_at)
VALUES (3, 'Eric Evans', 'Americano', '1950-05-10', 'Criador do conceito Domain-Driven Design', NOW(), NOW());

-- ========== CATEGORIAS ==========
INSERT IGNORE INTO categories (id, name, description, created_at, updated_at)
VALUES (1, 'Tecnologia', 'Livros técnicos de programação e software', NOW(), NOW());

INSERT IGNORE INTO categories (id, name, description, created_at, updated_at)
VALUES (2, 'Engenharia de Software', 'Metodologias e práticas de desenvolvimento', NOW(), NOW());

INSERT IGNORE INTO categories (id, name, description, created_at, updated_at)
VALUES (3, 'Programação', 'Linguagens e técnicas de programação', NOW(), NOW());

-- ========== LIVROS ==========
INSERT IGNORE INTO books (id, title, subtitle, isbn, publication_year, number_of_pages, price, edition, synopsis, stock_quantity, publisher_id, created_at, updated_at)
VALUES (1, 'Clean Code', 'Um guia prático para código limpo', '9780132350884', 2008, 464, 89.90, 1, 'Um guia prático para escrever código de qualidade', 10, 1, NOW(), NOW());

INSERT IGNORE INTO books (id, title, subtitle, isbn, publication_year, number_of_pages, price, edition, synopsis, stock_quantity, publisher_id, created_at, updated_at)
VALUES (2, 'Refatoração', 'Melhorando o design de código existente', '9780201485677', 1999, 456, 79.90, 2, 'Melhorando o design de código existente', 5, 2, NOW(), NOW());

INSERT IGNORE INTO books (id, title, subtitle, isbn, publication_year, number_of_pages, price, edition, synopsis, stock_quantity, publisher_id, created_at, updated_at)
VALUES (3, 'Padrões de Projeto', 'Soluções reutilizáveis para design de software', '9788550804080', 2010, 480, 99.90, 1, 'Soluções reutilizáveis para design de software orientado a objetos', 8, 1, NOW(), NOW());

INSERT IGNORE INTO books (id, title, subtitle, isbn, publication_year, number_of_pages, price, edition, synopsis, stock_quantity, publisher_id, created_at, updated_at)
VALUES (4, 'Domain-Driven Design', 'Atacando as complexidades no coração do software', '9780321125217', 2003, 560, 129.90, 1, 'Abordagem para modelagem de software complexo', 7, 3, NOW(), NOW());

-- ========== USUÁRIOS (TABELA: app_users) ==========
INSERT IGNORE INTO app_users (id, name, email, cpf, phone, address, birth_date, registration_date, is_active, created_at, updated_at)
VALUES (1, 'João Silva', 'joao@email.com', '123.456.789-00', '(11) 99999-9999', 'Rua A, 123 - São Paulo/SP', '1990-01-01', NOW(), true, NOW(), NOW());

INSERT IGNORE INTO app_users (id, name, email, cpf, phone, address, birth_date, registration_date, is_active, created_at, updated_at)
VALUES (2, 'Maria Santos', 'maria@email.com', '987.654.321-00', '(11) 88888-8888', 'Rua B, 456 - São Paulo/SP', '1985-05-15', NOW(), true, NOW(), NOW());

INSERT IGNORE INTO app_users (id, name, email, cpf, phone, address, birth_date, registration_date, is_active, created_at, updated_at)
VALUES (3, 'Pedro Oliveira', 'pedro@email.com', '456.789.123-00', '(11) 77777-7777', 'Rua C, 789 - São Paulo/SP', '1995-08-20', NOW(), true, NOW(), NOW());

-- ========== RELACIONAMENTOS (LIVROS_X_AUTORES) ==========
INSERT IGNORE INTO books_authors (book_id, author_id) VALUES (1, 1);
INSERT IGNORE INTO books_authors (book_id, author_id) VALUES (2, 2);
INSERT IGNORE INTO books_authors (book_id, author_id) VALUES (3, 1);
INSERT IGNORE INTO books_authors (book_id, author_id) VALUES (4, 3);

-- ========== RELACIONAMENTOS (LIVROS_X_CATEGORIAS) ==========
INSERT IGNORE INTO books_categories (book_id, category_id) VALUES (1, 1);
INSERT IGNORE INTO books_categories (book_id, category_id) VALUES (1, 2);
INSERT IGNORE INTO books_categories (book_id, category_id) VALUES (2, 1);
INSERT IGNORE INTO books_categories (book_id, category_id) VALUES (2, 2);
INSERT IGNORE INTO books_categories (book_id, category_id) VALUES (3, 1);
INSERT IGNORE INTO books_categories (book_id, category_id) VALUES (3, 3);
INSERT IGNORE INTO books_categories (book_id, category_id) VALUES (4, 1);
INSERT IGNORE INTO books_categories (book_id, category_id) VALUES (4, 2);