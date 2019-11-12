INSERT INTO roles(name) VALUES('ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO roles(name) VALUES('ROLE_ADMIN') ON CONFLICT DO NOTHING;

INSERT INTO users(user_id, name, username, email, password)
VALUES('ff82d45b-e423-4c8f-8b9b-9334857bf7dc','Okka Muhiba', 'okkamuhiba', 'test1@mail.com', '$2a$10$2Q5lqCIMn.vic0CCrsYwmeHu7tvZ55w7reqKpKEwPJDENRtpaA3UO')
ON CONFLICT DO NOTHING;

INSERT INTO users(user_id, name, username, email, password)
VALUES('d24146e7-0563-41db-9375-a4f4637a2c50','Handi Hermawan', 'handihermawan', 'test2@mail.com', '$2a$10$BOUuYbflXEBSeHpQJ0aRWuKzUCDX4GV6TjVIZjzAkmAYgyIY349Eu')
ON CONFLICT DO NOTHING;

INSERT INTO user_roles
VALUES('ff82d45b-e423-4c8f-8b9b-9334857bf7dc', 1)
ON CONFLICT DO NOTHING;

INSERT INTO user_roles
VALUES('d24146e7-0563-41db-9375-a4f4637a2c50', 1)
ON CONFLICT DO NOTHING;

INSERT INTO markets(market_id, market_name, market_sku, market_bio, user_id, user_fk)
VALUES('784ebb05-c34a-4737-84a6-a312e7661a53', 'Sebuah Toko', 'SBT', 'Ini merupakan sebuah toko', 'ff82d45b-e423-4c8f-8b9b-9334857bf7dc', 'ff82d45b-e423-4c8f-8b9b-9334857bf7dc')
ON CONFLICT DO NOTHING;

INSERT INTO products(product_id, title, description, author, publisher, SKU, price, market_fk)
VALUES('5e817d2b-13a7-4123-a71a-a5f1d076b9e6', 'Sebuah Buku', 'Ini adalah sebuah buku', 'Manusia', 'Orang lain', 'SBT-010', 50000, '784ebb05-c34a-4737-84a6-a312e7661a53')
ON CONFLICT DO NOTHING;

INSERT INTO products(product_id, title, description, author, publisher, SKU, price, market_fk)
VALUES('cbfe146e-da05-43d1-8dd9-a875ee95f0f0', 'How To Become Human', 'How someone can be called as "Human" in this world', 'Manusia', 'Sapa saja bisa', 'SBT-011', 65000, '784ebb05-c34a-4737-84a6-a312e7661a53')
ON CONFLICT DO NOTHING;

INSERT INTO products(product_id, title, description, author, publisher, SKU, price, market_fk)
VALUES('1454ca3e-a0c2-4c9c-863c-b4cc81a9ad93', 'Sebuah Kegabutan', 'Bagaimana cara terbaik untuk bisa gabut dalam kehidupan sehari-hari?', 'Manusia', 'Sapa saja bisa', 'SBT-012', 35000, '784ebb05-c34a-4737-84a6-a312e7661a53')
ON CONFLICT DO NOTHING;