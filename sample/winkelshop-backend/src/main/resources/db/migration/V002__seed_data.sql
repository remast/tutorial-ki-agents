INSERT INTO categories (id, name, description) VALUES
('cat_wands', 'Zauberstaebe', 'Zauberstaebe fuer Hexen und Zauberer'),
('cat_books', 'Buecher', 'Magische Lehr- und Fachbuecher');

INSERT INTO products (id, name, description, price, currency, in_stock, stock, category_id, image_url) VALUES
('prod_wand_holly_001', 'Stechpalme-Zauberstab', '11 Zoll, Phoenixfederkern, passend fuer praezise Zauber', 7.0, 'GALLEON', TRUE, 14, 'cat_wands', 'https://cdn.zauberbedarf.test/products/prod_wand_holly_001.jpg'),
('prod_book_potions_001', 'Fortgeschrittene Zaubertrankkunst', 'Lehrbuch fuer fortgeschrittene Trankbrauer', 5.0, 'GALLEON', TRUE, 30, 'cat_books', 'https://cdn.zauberbedarf.test/products/prod_book_potions_001.jpg'),
('prod_book_hogwarts_001', 'Hogwarts: Eine Geschichte', 'Historische Chronik von Hogwarts', 4.0, 'GALLEON', TRUE, 20, 'cat_books', 'https://cdn.zauberbedarf.test/products/prod_book_hogwarts_001.jpg');

INSERT INTO users (id, name, email, password, role, created_at) VALUES
('usr_harry', 'Harry Potter', 'harry.potter@hogwarts.test', 'Expelliarmus123!', 'buyer', TIMESTAMP WITH TIME ZONE '2026-01-01T00:00:00Z'),
('usr_hermione', 'Hermione Granger', 'hermione.granger@hogwarts.test', 'Expelliarmus123!', 'buyer', TIMESTAMP WITH TIME ZONE '2026-01-01T00:00:00Z'),
('usr_ron', 'Ron Weasley', 'ron.weasley@hogwarts.test', 'Expelliarmus123!', 'buyer', TIMESTAMP WITH TIME ZONE '2026-01-01T00:00:00Z'),
('usr_draco', 'Draco Malfoy', 'draco.malfoy@hogwarts.test', 'Expelliarmus123!', 'buyer', TIMESTAMP WITH TIME ZONE '2026-01-01T00:00:00Z'),
('usr_luna', 'Luna Lovegood', 'luna.lovegood@hogwarts.test', 'Expelliarmus123!', 'buyer', TIMESTAMP WITH TIME ZONE '2026-01-01T00:00:00Z');

INSERT INTO orders (id, user_id, status, payment_method, total, currency, shipping_full_name, shipping_street, shipping_city, shipping_postal_code, shipping_country, created_at) VALUES
('ord_2026_0001', 'usr_harry', 'placed', 'invoice', 25.0, 'GALLEON', 'Harry Potter', 'Ligusterweg 4', 'Little Whinging', '12345', 'UK', TIMESTAMP WITH TIME ZONE '2026-05-25T10:15:30Z'),
('ord_2026_0002', 'usr_harry', 'shipped', 'invoice', 8.0, 'GALLEON', 'Harry Potter', 'Ligusterweg 4', 'Little Whinging', '12345', 'UK', TIMESTAMP WITH TIME ZONE '2026-05-20T08:02:10Z');

INSERT INTO order_items (order_id, product_id, name, quantity, unit_price) VALUES
('ord_2026_0001', 'prod_wand_holly_001', 'Stechpalme-Zauberstab', 3, 7.0),
('ord_2026_0001', 'prod_book_hogwarts_001', 'Hogwarts: Eine Geschichte', 1, 4.0),
('ord_2026_0002', 'prod_book_hogwarts_001', 'Hogwarts: Eine Geschichte', 2, 4.0);
