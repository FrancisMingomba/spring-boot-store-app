INSERT INTO categories (name)
VALUES ('Fruits'),
       ('Vegetables'),
       ('Dairy'),
       ('Beverages');
INSERT INTO products (name, price, description, category_id)
VALUES ('Apple', 0.75, 'Fresh and juicy red apples. Sold by the piece.', 1),
       ('Banana', 0.30, 'Sweet and ripe bananas. Sold by the piece.', 1),
       ('Carrot', 0.40, 'Crunchy organic carrots. Sold by the piece.', 2),
       ('Broccoli', 1.20, 'Fresh broccoli crowns. Packed with nutrients.', 2),
       ('Milk', 2.50, '1 liter of whole milk. High in calcium.', 3),
       ('Cheddar Cheese', 3.00, 'Aged cheddar cheese block.', 3),
       ('Orange Juice', 3.75, '1 liter of 100% pure orange juice.', 4),
       ('Green Tea', 4.50, 'Premium loose leaf green tea. 100g pack.', 4),
       ('Yogurt', 1.10, 'Plain yogurt, 500g.', 3),
       ('Lettuce', 1.00, 'Fresh iceberg lettuce head.', 2);
