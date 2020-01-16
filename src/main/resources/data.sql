INSERT INTO roles(name) VALUES('ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO roles(name) VALUES('ROLE_MARKET') ON CONFLICT DO NOTHING;
INSERT INTO roles(name) VALUES('ROLE_SUPER_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles(name) VALUES('ROLE_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles(name) VALUES('ROLE_USER_BLOCKED') ON CONFLICT DO NOTHING;

INSERT INTO users(user_id, name, username, email, password, read_key)
VALUES('ff82d45b-e423-4c8f-8b9b-9334857bf7dc','Okka Muhiba', 'okkamuhiba', 'test1@mail.com', '$2a$10$2Q5lqCIMn.vic0CCrsYwmeHu7tvZ55w7reqKpKEwPJDENRtpaA3UO', 'ohLJHfgOJOPIGLHO')
ON CONFLICT DO NOTHING;

INSERT INTO users(user_id, name, username, email, password, read_key)
VALUES('d24146e7-0563-41db-9375-a4f4637a2c50','Handi Hermawan', 'handihermawan', 'test2@mail.com', '$2a$10$BOUuYbflXEBSeHpQJ0aRWuKzUCDX4GV6TjVIZjzAkmAYgyIY349Eu', 'GljGOpgjpJpjpjpg')
ON CONFLICT DO NOTHING;

INSERT INTO users(user_id, name, username, email, password, read_key)
VALUES('07ecd5dd-e652-400d-8821-c8e8107b12dd','Admin123', 'admin123', 'test3@mail.com', '$2a$10$sYDOwjpH74sZDdVtpEmJJ.iYMp1Gi6B3hJ8lMBCIWzG6otLmgFP4W', '9UlLlXTxbBRMhgR4')
ON CONFLICT DO NOTHING;

INSERT INTO user_roles
VALUES('ff82d45b-e423-4c8f-8b9b-9334857bf7dc', 1)
ON CONFLICT DO NOTHING;

INSERT INTO user_roles
VALUES('ff82d45b-e423-4c8f-8b9b-9334857bf7dc', 2)
ON CONFLICT DO NOTHING;

INSERT INTO user_roles
VALUES('d24146e7-0563-41db-9375-a4f4637a2c50', 1)
ON CONFLICT DO NOTHING;

INSERT INTO user_roles
VALUES('d24146e7-0563-41db-9375-a4f4637a2c50', 2)
ON CONFLICT DO NOTHING;

INSERT INTO user_roles
VALUES('07ecd5dd-e652-400d-8821-c8e8107b12dd', 3)
ON CONFLICT DO NOTHING;

INSERT INTO category_lists
VALUES('c74bba01-66ee-4b41-acf0-3fdc80c2c760', 'Fiksi')
ON CONFLICT DO NOTHING;

INSERT INTO category_lists
VALUES('62fd358e-f810-404f-bd11-52caa08428c4', 'Edukasi')
ON CONFLICT DO NOTHING;

INSERT INTO category_lists
VALUES('5ae17781-c90f-4faa-b048-570f7e04f7db', 'Bisnis')
ON CONFLICT DO NOTHING;

INSERT INTO category_lists
VALUES('89e9289a-30d6-42cd-a3aa-0260763cc8ae', 'Novel')
ON CONFLICT DO NOTHING;

INSERT INTO category_lists
VALUES('7f5b664a-16ca-4fdf-b35d-139904d04c06', 'Teknologi')
ON CONFLICT DO NOTHING;

INSERT INTO category_lists
VALUES('024e516d-7e45-4871-bd5e-a8f869c91f41', 'Romansa')
ON CONFLICT DO NOTHING;

INSERT INTO markets
VALUES('adedb3cd-2170-4f9c-936a-c84e87d2c1fb', 'Sebuah Toko Buku', 'Ini merupakan sebauh toko buku.', null, 'SEUKU', 0, 'ff82d45b-e423-4c8f-8b9b-9334857bf7dc', 'ff82d45b-e423-4c8f-8b9b-9334857bf7dc', '2020-01-13 13:03:17.291', '2020-01-13 13:03:17.291')
ON CONFLICT DO NOTHING;

INSERT INTO markets
VALUES('2d35d6ef-f24d-4174-905c-7cdc1c457eef', 'Fluctlight', 'The future is in your hand.', null, 'FLGHT', 0, 'd24146e7-0563-41db-9375-a4f4637a2c50', 'd24146e7-0563-41db-9375-a4f4637a2c50', '2020-01-13 13:07:40.932', '2020-01-13 13:07:40.932')
ON CONFLICT DO NOTHING;

INSERT INTO products
VALUES('f4d78702-7284-43e9-90fc-43c0d464cd19', 'To Kill A Mocking Bird', 'Nelle Harper Lee', 'Modern Classics', 'To Kill a Mockingbird adalah judul sebuah novel karangan Harper Lee yang diterbitkan pada tahun 1960. Novel ini didasari pada pengamatan penulis terhadap keluarga dan tetangga-tetangganya, serta kejadian-kejadian yang terjadi di sekitarnya pada tahun 1936, ketika penulis masih berumur 10 tahun.', '978-2-2369-5651-1', 149, 59000, 'FLGHT-0001', 'http://127.0.0.1:8080/api/files/books/20200115103900-bfB1cjeK6rBEHyh2VUqIZitvN27gxi3s.pdf', 'http://127.0.0.1:8080/api/files/products/20200115103901-O1EVGVIAqEMi6b64CCDmR4KBflFY4Hei.jpg',	'UNCONFIRMED', '2d35d6ef-f24d-4174-905c-7cdc1c457eef', '2020-01-15 10:39:01.923', '2020-01-15 10:39:01.923')
ON CONFLICT DO NOTHING;

INSERT INTO products
VALUES('e059788c-64c5-410e-ae7c-85c1249dd797', 'The Little Prince', 'Antoine de Saint-Exupery', 'Freeditoial', 'The Little Prince is a child with golden locks and a laugh that sounds like "a lot of little bells." He is thoughtful and curious and never lets go of a question once he has asked it. His home is a tiny planet identified as Asteroid B-612. It has three knee-high volcanoes, two of which are active, and is infested with dangerous baobab seeds. It is also home to a unique and lovely flower that the Little Prince cares for. But in her pride and vanity, she fails to show him affection in return. Feeling sad and lonely, the Little Prince leaves his tiny planet in a quest for friendship and to learn what is important in life. His interplanetary travels show him the foolishness of grown-ups who have forgotten what is essential and are consumed by pointless matters. Then on Earth he befriends and is befriended by a fox who shares with him the secret of seeing with his heart and not with his eyes. The prince learns that "what is important cannot be seen." By the time the Little Prince meets the narrator, marooned in the Sahara Desert, he has learned how to see rightly and how to care for whomever he loves. Befriending the pilot, his role shifts from student to teacher as he helps the pilot, who has forgotten the important things in life.', '978-5-6052-7746-0', 47, 22000, 'FLGHT-0002', 'http://127.0.0.1:8080/api/files/books/20200115104045-GwTRNyJxXyrYqH2w7T7LzosWWRv1iv8l.pdf', 'http://127.0.0.1:8080/api/files/products/20200115104046-uJFp69biy8j1yPxZARW5UnKdny7EGKCU.jpg', 'UNCONFIRMED', '2d35d6ef-f24d-4174-905c-7cdc1c457eef', '2020-01-15 10:40:46.836', '2020-01-15 10:40:46.836')
ON CONFLICT DO NOTHING;

INSERT INTO products
VALUES('257c6878-4d83-40d3-9c47-83b621a0a1a3', 'El Principito', 'Antoine de Saint-Exupery', 'Freeditoial', 'Novel ini diterbitkan pada bulan April 1943 , baik dalam bahasa Inggris dan Prancis, oleh penerbit Amerika Reynal & Hitchcock, sementara penerbit Prancis Éditions Gallimard tidak dapat mencetak karya sampai 1946, setelah pembebasan Perancis . Termasuk di antara buku-buku terbaik abad kedua puluh di Prancis , The Little Prince telah menjadi buku yang paling banyak dibaca dan diterjemahkan yang ditulis dalam bahasa Prancis. Dengan demikian, ia memiliki terjemahan ke lebih dari dua ratus lima puluh bahasa dan dialek, termasuk sistem membaca Braille. Pekerjaan juga telah menjadi salah satu yang terbaik - buku menjualsepanjang masa, karena telah berhasil menjual lebih dari 140 juta kopi di seluruh dunia, dengan lebih dari satu juta penjualan per tahun. Catatan Novel ini diterjemahkan ke dalam bahasa Spanyol untuk Bonifacio Lane dan publikasi pertamanya dalam bahasa ini dibuat oleh Argentina publisher Emecé Penerbit pada bulan September 1951. Sejak itu, beberapa penerjemah dan penerbit telah membuat versi mereka sendiri.', '978-1-2902-8838-5', 46, 52000, 'FLGHT-0003', 'http://127.0.0.1:8080/api/files/books/20200115104231-L8pMNeJpYlKKZuu28Z5xnuqYG46p9Xji.pdf', 'http://127.0.0.1:8080/api/files/products/20200115104231-YUx6gC0ldrhaB5zDiKrLqW7mNej85Ots.jpg', 'UNCONFIRMED', '2d35d6ef-f24d-4174-905c-7cdc1c457eef', '2020-01-15 10:42:31.307', '2020-01-15 10:42:31.307')
ON CONFLICT DO NOTHING;

INSERT INTO products
VALUES('6ab84304-3b5a-4929-9c1e-4c2a68f940e2', 'Tierra de Los Hombres', 'Antoine de Saint-Exupery', 'Freeditoial', 'Dalam karya otobiografi ini, Antoine de Saint Exupéry membangkitkan serangkaian peristiwa dalam hidupnya - sejak ia bekerja untuk Aéropostale -, dan mengambil kesempatan untuk memberi makan refleksinya tentang persahabatan, kematian, kepahlawanan, pencarian makna dari hal-hal, dll ... Elemen utama dari novel ini adalah kecelakaan yang dideritanya pada tahun 1935 di bagian Libya dari gurun Sahara , ketika ia bepergian dengan André Prévot , dan di mana mereka hampir mati kehausan.', '978-8-4394-3989-9', 78, 78000, 'FLGHT-0004', 'http://127.0.0.1:8080/api/files/books/20200115104411-Ondj8e86ceCWpkdtd9v89zcLPLyPISXh.pdf', 'http://127.0.0.1:8080/api/files/products/20200115104411-oAbLWeBT8m7FyxSfD2ImjHeZcEYyytSj.jpg', 'UNCONFIRMED', '2d35d6ef-f24d-4174-905c-7cdc1c457eef', '2020-01-15 10:44:13.173', '2020-01-15 10:44:13.173')
ON CONFLICT DO NOTHING;

INSERT INTO products
VALUES('ef92120f-fad7-4a0f-9bc2-7d244ee71cd3', 'Vuelo Nocturno', 'Antoine de Saint-Exupery', 'Freeditoial', '-', '978-8-1065-2530-6', 52, 94500, 'FLGHT-0005', 'http://127.0.0.1:8080/api/files/books/20200115104551-OJJ5FJO5QsnYo1i40c7Me9OBU7P3zHAw.pdf', 'http://127.0.0.1:8080/api/files/products/20200115104553-pghZkIzTACYBG4a4ltPlg9hoDUcDslgM.jpg', 'UNCONFIRMED', '2d35d6ef-f24d-4174-905c-7cdc1c457eef', '2020-01-15 10:45:53.578', '2020-01-15 10:45:53.579')
ON CONFLICT DO NOTHING;

INSERT INTO products
VALUES('41f07738-8825-420f-bb1c-cc9fbb917c29', 'Drawing Management With Autocad Sheet', 'Edwin Prakoso', 'Engineers Book', 'Drawing Management With AutoCAD Sheet Set by Edwin', '978-0-7294-4346-3', 88, 135000, 'SEUKU-0001', 'http://127.0.0.1:8080/api/files/books/20200115105236-dIqkZMB2xS6VBwMneVVjUzcULqkFLN2Z.pdf', 'http://127.0.0.1:8080/api/files/products/20200115105237-U9v3Ymc0aSWNIFoz4UfJvQ8RE1AtcbpW.jpg', 'UNCONFIRMED', 'adedb3cd-2170-4f9c-936a-c84e87d2c1fb', '2020-01-15 10:52:37.218', '2020-01-15 10:52:37.218')
ON CONFLICT DO NOTHING;

INSERT INTO products
VALUES('0ffa54de-d359-40aa-9315-40807010cfd2', 'Data Mining Algorithms in C++', 'Timothy Masters', 'Apress', 'Data mining algorithms including machine learning, statistical analysis, and pattern recognition techniques can greatly improve our understanding of data warehouses that are now becoming more widespread. In this paper, we focus on classification algorithms and review the need for multiple classification algorithms. We describe a system called MLC++, which was designed to help choose the appropriate classification algorithm for a given dataset by making it easy to compare the utility of different algorithms on a specific dataset of interest. MLC++ not only provides a workbench for such comparisons, but also provides a library of C++ classes to aid in the development of new algorithms, especially hybrid algorithms and multi-strategy algorithms. Such algorithms are generally hard to code from scratch. We discuss design issues, interfaces to other programs, and visualization of the resulting classifiers.', '978-1-4842-3315-3', 296, 175000, 'SEUKU-0002', 'http://127.0.0.1:8080/api/files/books/20200115105455-9CVJCflpL9GODXjPMTDthFxrGTnPLbAZ.pdf', 'http://127.0.0.1:8080/api/files/products/20200115105455-t8lsTXUQJ26vRjUoDleHl8wBySub4sW4.jpg', 'UNCONFIRMED', 'adedb3cd-2170-4f9c-936a-c84e87d2c1fb', '2020-01-15 10:54:55.683', '2020-01-15 10:54:55.683')
ON CONFLICT DO NOTHING;

INSERT INTO products
VALUES('2f2aa2fc-4bda-4bb7-a2c9-ee288baa1dab', 'Learn Keras For Deep Neural Networks', 'Jojo Moolayil', 'Apress', 'Learn, understand, and implement deep neural networks in a math- and programming-friendly approach using Keras and Python. The book focuses on an end-to-end approach to developing supervised learning algorithms in regression and classification with practical business-centric use-cases implemented in Keras.', '978-1-4842-4240-7', 192, 205000, 'SEUKU-0003', 'http://127.0.0.1:8080/api/files/books/20200115105621-T2jGAmcGILQtf10nkrKjh5x8f6my6HGS.pdf', 'http://127.0.0.1:8080/api/files/products/20200115105621-jmO2LrIj8NWqLAtSVEAxOwRNCjz21Djj.jpg', 'UNCONFIRMED', 'adedb3cd-2170-4f9c-936a-c84e87d2c1fb', '2020-01-15 10:56:21.5', '2020-01-15 10:56:21.5')
ON CONFLICT DO NOTHING;

INSERT INTO products
VALUES('b56a3321-7619-49eb-890e-54646fa968d4', 'Cisco Router Configuration Handbook', 'Andrew Whitaker', 'Cisco Press', 'Locating reliable Cisco router configuration command information can require extensive, time-consuming research. Cisco Router Configuration Handbook, 2/e, is the solution: a day-to-day reference to the most widely used Cisco router features and configurations. Straight from Cisco experts, it covers every facet of router configuration, including fundamentals, network protocols, packet processing, voice/telephony, security, and more. This book is organized for maximum efficiency. Related features are covered together, and features and options are covered in the sequence in which they are typically used. Shaded tabs mark each section for quick reference.', '978-1-58714-116-4', 665, 150000, 'SEUKU-0004', 'http://127.0.0.1:8080/api/files/books/20200115105753-scecJCUF6Zz0dmdCJo8gRiCIqRG2RLHy.pdf', 'http://127.0.0.1:8080/api/files/products/20200115105753-cnK36U4OKdb0OPZ8AtkLE1NDVR3t8mm8.jpg', 'UNCONFIRMED', 'adedb3cd-2170-4f9c-936a-c84e87d2c1fb', '2020-01-15 10:57:53.423', '2020-01-15 10:57:53.423')
ON CONFLICT DO NOTHING;

INSERT INTO products
VALUES('e5b5f382-1e06-460a-b91e-c7d72ecb4155', 'Blender for Animation and Film-Based Production', 'Michelangelo Manrique', 'CRC Press', 'This book will familiarizes you with the animation industry and explores the risks involved in choosing Blender as a primary tool in animation studios. Blender for Animation and Film-Based Production explores why Blender is ideal for animation films. It demonstrates Blender`s capability to do the job in each production department.', '978-4-1061-1261-1', 279, 195000, 'SEUKU-0005', 'http://127.0.0.1:8080/api/files/books/20200115110016-1UnG40ZXRbiE6NEyjjuh3iyts5oeS195.pdf', 'http://127.0.0.1:8080/api/files/products/20200115110016-UgJDn3rPkqLjTGLquchHCOZlOLIWG4EI.jpg', 'UNCONFIRMED', 'adedb3cd-2170-4f9c-936a-c84e87d2c1fb', '2020-01-15 11:00:16.653', '2020-01-15 11:00:16.653')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('e5b5f382-1e06-460a-b91e-c7d72ecb4155','62fd358e-f810-404f-bd11-52caa08428c4')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('b56a3321-7619-49eb-890e-54646fa968d4','62fd358e-f810-404f-bd11-52caa08428c4')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('b56a3321-7619-49eb-890e-54646fa968d4','7f5b664a-16ca-4fdf-b35d-139904d04c06')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('2f2aa2fc-4bda-4bb7-a2c9-ee288baa1dab','7f5b664a-16ca-4fdf-b35d-139904d04c06')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('2f2aa2fc-4bda-4bb7-a2c9-ee288baa1dab','62fd358e-f810-404f-bd11-52caa08428c4')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('0ffa54de-d359-40aa-9315-40807010cfd2','62fd358e-f810-404f-bd11-52caa08428c4')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('0ffa54de-d359-40aa-9315-40807010cfd2','7f5b664a-16ca-4fdf-b35d-139904d04c06')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('41f07738-8825-420f-bb1c-cc9fbb917c29','7f5b664a-16ca-4fdf-b35d-139904d04c06')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('41f07738-8825-420f-bb1c-cc9fbb917c29','62fd358e-f810-404f-bd11-52caa08428c4')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('ef92120f-fad7-4a0f-9bc2-7d244ee71cd3','c74bba01-66ee-4b41-acf0-3fdc80c2c760')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('ef92120f-fad7-4a0f-9bc2-7d244ee71cd3','89e9289a-30d6-42cd-a3aa-0260763cc8ae')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('6ab84304-3b5a-4929-9c1e-4c2a68f940e2','89e9289a-30d6-42cd-a3aa-0260763cc8ae')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('257c6878-4d83-40d3-9c47-83b621a0a1a3','89e9289a-30d6-42cd-a3aa-0260763cc8ae')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('257c6878-4d83-40d3-9c47-83b621a0a1a3','c74bba01-66ee-4b41-acf0-3fdc80c2c760')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('e059788c-64c5-410e-ae7c-85c1249dd797','c74bba01-66ee-4b41-acf0-3fdc80c2c760')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('e059788c-64c5-410e-ae7c-85c1249dd797','89e9289a-30d6-42cd-a3aa-0260763cc8ae')
ON CONFLICT DO NOTHING;

INSERT INTO category_products
VALUES('f4d78702-7284-43e9-90fc-43c0d464cd19','89e9289a-30d6-42cd-a3aa-0260763cc8ae')
ON CONFLICT DO NOTHING;