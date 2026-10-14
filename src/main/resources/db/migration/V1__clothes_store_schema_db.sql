-- 1. USERS
CREATE TABLE `users`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) NOT NULL,
    `email` varchar(150) NOT NULL,
    `name` varchar(100) NOT NULL ,
    `password` varchar(255) NOT NULL ,
    `phone` varchar(20) NOT NULL ,
    `role` enum('ADMIN','USER') DEFAULT NULL,
    `user_name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 2. BRANDS
CREATE TABLE `brands` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL ,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 3. CATEGORY
CREATE TABLE `category`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `description` varchar(500) DEFAULT NULL,
    `name` varchar(100) NOT NULL ,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK46ccwnsi9409t36lurvtyljak` (`name`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 4. SUB_CATEGORIES
CREATE TABLE `sub_categories`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL ,
    `category_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKgnmq16mx8br8blna7yuwhcw9m` (`category_id`),
    CONSTRAINT `FKgnmq16mx8br8blna7yuwhcw9m` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 5. PRODUCTS
CREATE TABLE `products`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `base_price` decimal(10,2) NOT NULL ,
    `creation_at` datetime(6) NOT NULL ,
    `description` varchar(2000) DEFAULT NULL,
    `name` varchar(200) NOT NULL ,
    `product_status` enum('ACTIVE','INACTIVE','OUT_OF_STOCK') DEFAULT NULL,
    `brand_id` bigint DEFAULT NULL,
    `subcategory_id` bigint DEFAULT NULL,
    `admin_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKo61fmio5yukmmiqgnxf8pnavn` (`name`),
    KEY `FKa3a4mpsfdf4d2y6r8ra3sc8mv` (`brand_id`),
    KEY `FKh7nopotm4ii6cr5np9nhxs4aa` (`subcategory_id`),
    KEY `FKcuswhcoaly6brhqn0g5qit502` (`admin_id`),
    CONSTRAINT `FKa3a4mpsfdf4d2y6r8ra3sc8mv` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`),
    CONSTRAINT `FKcuswhcoaly6brhqn0g5qit502` FOREIGN KEY (`admin_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKh7nopotm4ii6cr5np9nhxs4aa` FOREIGN KEY (`subcategory_id`) REFERENCES `sub_categories` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 6. PRODUCT_VARIANTS
CREATE TABLE `product_variants`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `color` varchar(50) NOT NULL ,
    `price` decimal(10,2) NOT NULL ,
    `size` varchar(20) NOT NULL ,
    `sku` varchar(100) NOT NULL ,
    `product_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKosqitn4s405cynmhb87lkvuau` (`product_id`),
    CONSTRAINT `FKosqitn4s405cynmhb87lkvuau` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 7. PRODUCT_IMAGES
CREATE TABLE `product_images`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `image_url` varchar(500) NOT NULL,
    `product_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKqnq71xsohugpqwf3c9gxmsuy` (`product_id`),
    CONSTRAINT `FKqnq71xsohugpqwf3c9gxmsuy` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 8. INVENTORY
CREATE TABLE `inventory`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `stock_quantity` int NOT NULL ,
    `product_variant_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKsmhosiiwyweyooj1okkh3tsqs` (`product_variant_id`),
    CONSTRAINT `FKrh3ah6qwgo1pipvxjby2a7208` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 9. CARTS
CREATE TABLE `carts`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) NOT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK64t7ox312pqal3p7fg9o503c2` (`user_id`),
    CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 10. CART_ITEMS
CREATE TABLE `cart_items`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `quantity` int NOT NULL ,
    `cart_id` bigint DEFAULT NULL,
    `product_variant_id` bigint NOT NULL ,
    PRIMARY KEY (`id`),
    KEY `FKpcttvuq4mxppo8sxggjtn5i2c` (`cart_id`),
    KEY `FKn1s4l7h0vm4o259wpu7ft0y2y` (`product_variant_id`),
    CONSTRAINT `FKn1s4l7h0vm4o259wpu7ft0y2y` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`),
    CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`),
    CONSTRAINT `cart_items_chk_1` CHECK ((`quantity` >= 1))
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 11. WISHLIST
CREATE TABLE `wishlist`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKrcuy9aqx9c6q56x1xdoty8r3q` (`user_id`),
    CONSTRAINT `FKtrd6335blsefl2gxpb8lr0gr7` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- 12. WISHLIST_ITEMS
CREATE TABLE `wishlist_items` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `product_id` bigint DEFAULT NULL,
    `wishlist_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKt0eybn5siuw2bgmcxjvojtbhb` (`product_id`),
    KEY `FKp1ce2cs62p57h0uw8ydtdmbx8` (`wishlist_id`),
    CONSTRAINT `FKp1ce2cs62p57h0uw8ydtdmbx8` FOREIGN KEY (`wishlist_id`) REFERENCES `wishlist` (`id`),
    CONSTRAINT `FKt0eybn5siuw2bgmcxjvojtbhb` FOREIGN KEY (`product_id`) REFERENCES `product_variants` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 13. ADDRESS
CREATE TABLE `address` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `city` varchar(255) NOT NULL,
    `country` varchar(255) NOT NULL,
    `district` varchar(255) NOT NULL,
    `is_default` bit(1) NOT NULL,
    `postal_code` varchar(20) NOT NULL,
    `street` varchar(255) NOT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK6i66ijb8twgcqtetl8eeeed6v` (`user_id`),
    CONSTRAINT `FK6i66ijb8twgcqtetl8eeeed6v` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 14. ORDERS
CREATE TABLE `orders`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `create_at` datetime(6) NOT NULL,
    `order_status` enum('CANCELLED','COMPLETED','CONFIRMED','PENDING') DEFAULT NULL,
    `total_amount` decimal(10,2) NOT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
    CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 15. ORDER_ITEMS
CREATE TABLE `order_items` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `price` decimal(10,2) NOT NULL,
    `quantity` int NOT NULL,
    `order_id` bigint DEFAULT NULL,
    `product_variant_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
    KEY `FKltmtlue0wixrg1cf0xo7x0l4d` (`product_variant_id`),
    CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    CONSTRAINT `FKltmtlue0wixrg1cf0xo7x0l4d` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 16. PAYMENTS
CREATE TABLE `payments` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `amount` decimal(10,2) NOT NULL,
    `payment_date` datetime(6) NOT NULL,
    `payment_method` enum('BANK_TRANSFER','CARD','CASH_ON_DELIVERY','PAYPAL','STRIPE') DEFAULT NULL,
    `payment_status` enum('FAILED','PENDING','SUCCESS') DEFAULT NULL,
    `transaction_id` varchar(100) NOT NULL,
    `order_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKlryndveuwa4k5qthti0pkmtlx` (`transaction_id`),
    UNIQUE KEY `UK8vo36cen604as7etdfwmyjsxt` (`order_id`),
    CONSTRAINT `FK81gagumt0r8y3rmudcgpbk42l` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 17. DELIVERY
CREATE TABLE `delivery` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `delivered_date` datetime(6) DEFAULT NULL,
    `delivery_status` enum('CANCELLED','DELIVERED','PROCESSING','SHIPPED') DEFAULT NULL,
    `shipped_date` datetime(6) DEFAULT NULL,
    `tracking_number` varchar(100) NOT NULL,
    `order_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKcfp5smv4gs9srljhd0bdug73r` (`tracking_number`),
    UNIQUE KEY `UK3bdrbd2jcybaaa5rxkj4s7vlk` (`order_id`),
    CONSTRAINT `FKu4e8rjwmg09vmas3ccjwglso` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 18. REVIEWS
CREATE TABLE `reviews` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `comment` varchar(1000) DEFAULT NULL,
    `created_at` datetime(6) NOT NULL,
    `rating` int NOT NULL,
    `product_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKpl51cejpw4gy5swfar8br9ngi` (`product_id`),
    KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
    CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKpl51cejpw4gy5swfar8br9ngi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
