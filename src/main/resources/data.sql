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

INSERT INTO category_lists
VALUES('c74bba01-66ee-4b41-acf0-3fdc80c2c760', 'Fiction')
ON CONFLICT DO NOTHING;

INSERT INTO category_lists
VALUES('62fd358e-f810-404f-bd11-52caa08428c4', 'Education')
ON CONFLICT DO NOTHING;

INSERT INTO category_lists
VALUES('5ae17781-c90f-4faa-b048-570f7e04f7db', 'Health')
ON CONFLICT DO NOTHING;

INSERT INTO category_lists
VALUES('89e9289a-30d6-42cd-a3aa-0260763cc8ae', 'Sport')
ON CONFLICT DO NOTHING;

INSERT INTO markets(market_id, market_name, market_sku, market_bio, user_id, user_fk)
VALUES('784ebb05-c34a-4737-84a6-a312e7661a53', 'Sebuah Toko', 'SBT', 'Ini merupakan sebuah toko', 'ff82d45b-e423-4c8f-8b9b-9334857bf7dc', 'ff82d45b-e423-4c8f-8b9b-9334857bf7dc')
ON CONFLICT DO NOTHING;