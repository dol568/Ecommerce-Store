INSERT INTO product_brand (name, id)
VALUES ('Angular', 'b0c8bd8cf5284fcb8e0da0e2ea3c8e06');
INSERT INTO product_brand (name, id)
VALUES ('SpringBoot', '6e26d0fedca24002bbcc259c7e955fba');
INSERT INTO product_brand (name, id)
VALUES ('VS Code', 'b63474c5fcd94aaea7da044a9e5b9182');
INSERT INTO product_brand (name, id)
VALUES ('React', '39ad2b694aec423cae3c868d0423b21c');
INSERT INTO product_brand (name, id)
VALUES ('Typescript', '616f8e6c580c4179b575b4fbab8db308');
INSERT INTO product_brand (name, id)
VALUES ('MySQL', '225906769fc64f0b94f16e62538d30b1');

INSERT INTO product_type (name, id)
VALUES ('Boards', '56fa15ed7ee84f65a63ec6bf7602b68a');
INSERT INTO product_type (name, id)
VALUES ('Hats', 'aeea18dbff9142e0bb7645f9e683538d');
INSERT INTO product_type (name, id)
VALUES ('Boots', '95935946e6d74cdda81ec79728a803ee');
INSERT INTO product_type (name, id)
VALUES ('Gloves', 'eb67b0be52a84f55b2d3bc46a8fb25d0');

INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('b0c8bd8cf5284fcb8e0da0e2ea3c8e06',
        'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Maecenas porttitor congue massa. Fusce posuere, magna sed pulvinar ultricies, purus lectus malesuada libero, sit amet commodo magna eros quis urna.',
        'Angular Speedster Board 2000', '', '56fa15ed7ee84f65a63ec6bf7602b68a', 200,
        '6126d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('225906769fc64f0b94f16e62538d30b1', 'Nunc viverra imperdiet enim. Fusce est. Vivamus a tellus.',
        'Green Angular Board 3000', '', 'aeea18dbff9142e0bb7645f9e683538d', 150, '6226d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('616f8e6c580c4179b575b4fbab8db308',
        'Suspendisse dui purus, scelerisque at, vulputate vitae, pretium mattis, nunc. Mauris eget neque at sem venenatis eleifend. Ut nonummy.',
        'Core Board Speed Rush 3', '', '95935946e6d74cdda81ec79728a803ee', 180, '6326d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('616f8e6c580c4179b575b4fbab8db308',
        'Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin pharetra nonummy pede. Mauris et orci.',
        'Net Core Super Board', '', 'eb67b0be52a84f55b2d3bc46a8fb25d0', 300, '6426d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('616f8e6c580c4179b575b4fbab8db308',
        'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Maecenas porttitor congue massa. Fusce posuere, magna sed pulvinar ultricies, purus lectus malesuada libero, sit amet commodo magna eros quis urna.',
        'React Board Super Whizzy Fast', '', 'eb67b0be52a84f55b2d3bc46a8fb25d0', 250,
        '6526d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('39ad2b694aec423cae3c868d0423b21c', 'Aenean nec lorem. In porttitor. Donec laoreet nonummy augue.',
        'Typescript Entry Board', '', '95935946e6d74cdda81ec79728a803ee', 120, '6626d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('39ad2b694aec423cae3c868d0423b21c',
        'Fusce posuere, magna sed pulvinar ultricies, purus lectus malesuada libero, sit amet commodo magna eros quis urna.',
        'Core Blue Hat', '', 'aeea18dbff9142e0bb7645f9e683538d', 10, '6726d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('39ad2b694aec423cae3c868d0423b21c',
        'Suspendisse dui purus, scelerisque at, vulputate vitae, pretium mattis, nunc. Mauris eget neque at sem venenatis eleifend. Ut nonummy.',
        'Green React Woolen Hat', '', '56fa15ed7ee84f65a63ec6bf7602b68a', 8, '6826d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('39ad2b694aec423cae3c868d0423b21c',
        'Fusce posuere, magna sed pulvinar ultricies, purus lectus malesuada libero, sit amet commodo magna eros quis urna.',
        'Purple React Woolen Hat', '', '56fa15ed7ee84f65a63ec6bf7602b68a', 15, '6926d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('b63474c5fcd94aaea7da044a9e5b9182', 'Nunc viverra imperdiet enim. Fusce est. Vivamus a tellus.',
        'Blue Code Gloves', '', 'aeea18dbff9142e0bb7645f9e683538d', 18, '6136d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('b63474c5fcd94aaea7da044a9e5b9182',
        'Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin pharetra nonummy pede. Mauris et orci.',
        'Green Code Gloves', '', 'aeea18dbff9142e0bb7645f9e683538d', 15, '6146d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('b63474c5fcd94aaea7da044a9e5b9182',
        'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Maecenas porttitor congue massa.',
        'Purple React Gloves', '', '95935946e6d74cdda81ec79728a803ee', 16, '6156d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('6e26d0fedca24002bbcc259c7e955fba',
        'Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin pharetra nonummy pede. Mauris et orci.',
        'Green React Gloves', '', 'eb67b0be52a84f55b2d3bc46a8fb25d0', 14, '6166d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('6e26d0fedca24002bbcc259c7e955fba',
        'Suspendisse dui purus, scelerisque at, vulputate vitae, pretium mattis, nunc. Mauris eget neque at sem venenatis eleifend. Ut nonummy.',
        'Redis Red Boots', '', 'eb67b0be52a84f55b2d3bc46a8fb25d0', 250, '6176d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('6e26d0fedca24002bbcc259c7e955fba',
        'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Maecenas porttitor congue massa. Fusce posuere, magna sed pulvinar ultricies, purus lectus malesuada libero, sit amet commodo magna eros quis urna.',
        'Core Red Boots', '', 'eb67b0be52a84f55b2d3bc46a8fb25d0', 189.99, '6186d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('b0c8bd8cf5284fcb8e0da0e2ea3c8e06',
        'Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin pharetra nonummy pede. Mauris et orci.',
        'Core Purple Boots', '', '95935946e6d74cdda81ec79728a803ee', 199.99, '6196d0fedca24002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('b0c8bd8cf5284fcb8e0da0e2ea3c8e06', 'Aenean nec lorem. In porttitor. Donec laoreet nonummy augue.',
        'Angular Purple Boots', '', '95935946e6d74cdda81ec79728a803ee', 150, '6126d0fedca25002bbcc259c7e955fba');
INSERT INTO product (brand_id, description, name, picture_url, type_id, unit_price, id)
VALUES ('b0c8bd8cf5284fcb8e0da0e2ea3c8e06',
        'Suspendisse dui purus, scelerisque at, vulputate vitae, pretium mattis, nunc. Mauris eget neque at sem venenatis eleifend. Ut nonummy.',
        'Angular Blue Boots', '', 'aeea18dbff9142e0bb7645f9e683538d', 180, '6126d0fedca26002bbcc259c7e955fba');

INSERT INTO user (id, email, is_enabled, is_non_locked, password, username)
VALUES (1, 'dar@gmail.com', true, true, '$2a$10$yyR/BmyGcjJpcHnovCPxSuTXp98XAd1aYheBGbHi9jQvCnfQgprGu', 'Darek');
INSERT INTO user (id, email, is_enabled, is_non_locked, password, username)
VALUES (2, 'err@gmail.com', true, true, '$2a$10$yyR/BmyGcjJpcHnovCPxSuTXp98XAd1aYheBGbHi9jQvCnfQgprGu', 'Error');
INSERT INTO address (id, city, country, name, postal_code, province, street, user_id)
VALUES (1, 'Suwalki', 'Polska', 'Darek', '16-400', 'Podlaskie', 'Kowalskiego 12A', 1);
INSERT INTO role (id, name)
VALUES (1, 'USER');

INSERT INTO users_roles (role_id, user_id)
VALUES (1, 1);
INSERT INTO users_roles (role_id, user_id)
VALUES (1, 2);

INSERT INTO `order` (buyer_email, order_date, order_status, shipping_address_id, total, id)
VALUES ('dar@gmail.com', '2024-01-25 10:07:57.216000', 'PENDING', 1, 8, 'c7dc2294-e712-4399-91b0-aca5632105e4');
INSERT INTO order_item (order_id, picture_url, product_item_id, product_name, quantity, unit_price)
VALUES ('c7dc2294-e712-4399-91b0-aca5632105e4', '', '6826d0fedca24002bbcc259c7e955fba', 'Green React Woolen Hat', 1, 8);