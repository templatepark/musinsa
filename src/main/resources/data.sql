INSERT INTO brand(brand_name, created_at, updated_at)
VALUES ('A', NOW(), NOW()),
       ('B', NOW(), NOW()),
       ('C', NOW(), NOW()),
       ('D', NOW(), NOW()),
       ('E', NOW(), NOW()),
       ('F', NOW(), NOW()),
       ('G', NOW(), NOW()),
       ('H', NOW(), NOW()),
       ('I', NOW(), NOW());

INSERT INTO category(category_name, created_at, updated_at)
VALUES ('상의', NOW(), NOW()),
       ('아우터', NOW(), NOW()),
       ('바지', NOW(), NOW()),
       ('스니커즈', NOW(), NOW()),
       ('가방', NOW(), NOW()),
       ('모자', NOW(), NOW()),
       ('양말', NOW(), NOW()),
       ('액세서리', NOW(), NOW());

INSERT INTO product(brand_id, category_id, price, deleted, created_at, updated_at)
VALUES (1, 1, 11_200, false, NOW(), NOW()),
       (1, 2, 5_500, false, NOW(), NOW()),
       (1, 3, 4_200, false, NOW(), NOW()),
       (1, 4, 9_000, false, NOW(), NOW()),
       (1, 5, 2_000, false, NOW(), NOW()),
       (1, 6, 1_700, false, NOW(), NOW()),
       (1, 7, 1_800, false, NOW(), NOW()),
       (1, 8, 2_300, false, NOW(), NOW()),
       (2, 1, 10_500, false, NOW(), NOW()),
       (2, 2, 5_900, false, NOW(), NOW()),
       (2, 3, 3_800, false, NOW(), NOW()),
       (2, 4, 9_100, false, NOW(), NOW()),
       (2, 5, 2_100, false, NOW(), NOW()),
       (2, 6, 2_000, false, NOW(), NOW()),
       (2, 7, 2_000, false, NOW(), NOW()),
       (2, 8, 2_200, false, NOW(), NOW()),
       (3, 1, 10_000, false, NOW(), NOW()),
       (3, 2, 6_200, false, NOW(), NOW()),
       (3, 3, 3_300, false, NOW(), NOW()),
       (3, 4, 9_200, false, NOW(), NOW()),
       (3, 5, 2_200, false, NOW(), NOW()),
       (3, 6, 1_900, false, NOW(), NOW()),
       (3, 7, 2_200, false, NOW(), NOW()),
       (3, 8, 2_100, false, NOW(), NOW()),
       (4, 1, 10_100, false, NOW(), NOW()),
       (4, 2, 5_100, false, NOW(), NOW()),
       (4, 3, 3_000, false, NOW(), NOW()),
       (4, 4, 9_500, false, NOW(), NOW()),
       (4, 5, 2_500, false, NOW(), NOW()),
       (4, 6, 1_500, false, NOW(), NOW()),
       (4, 7, 2_400, false, NOW(), NOW()),
       (4, 8, 2_000, false, NOW(), NOW()),
       (5, 1, 10_700, false, NOW(), NOW()),
       (5, 2, 5_000, false, NOW(), NOW()),
       (5, 3, 3_800, false, NOW(), NOW()),
       (5, 4, 9_900, false, NOW(), NOW()),
       (5, 5, 2_300, false, NOW(), NOW()),
       (5, 6, 1_800, false, NOW(), NOW()),
       (5, 7, 2_100, false, NOW(), NOW()),
       (5, 8, 2_100, false, NOW(), NOW()),
       (6, 1, 11_200, false, NOW(), NOW()),
       (6, 2, 7_200, false, NOW(), NOW()),
       (6, 3, 4_000, false, NOW(), NOW()),
       (6, 4, 9_300, false, NOW(), NOW()),
       (6, 5, 2_100, false, NOW(), NOW()),
       (6, 6, 1_600, false, NOW(), NOW()),
       (6, 7, 2_300, false, NOW(), NOW()),
       (6, 8, 1_900, false, NOW(), NOW()),
       (7, 1, 10_500, false, NOW(), NOW()),
       (7, 2, 5_800, false, NOW(), NOW()),
       (7, 3, 3_900, false, NOW(), NOW()),
       (7, 4, 9_000, false, NOW(), NOW()),
       (7, 5, 2_200, false, NOW(), NOW()),
       (7, 6, 1_700, false, NOW(), NOW()),
       (7, 7, 2_100, false, NOW(), NOW()),
       (7, 8, 2_000, false, NOW(), NOW()),
       (8, 1, 10_800, false, NOW(), NOW()),
       (8, 2, 6_300, false, NOW(), NOW()),
       (8, 3, 3_100, false, NOW(), NOW()),
       (8, 4, 9_700, false, NOW(), NOW()),
       (8, 5, 2_100, false, NOW(), NOW()),
       (8, 6, 1_600, false, NOW(), NOW()),
       (8, 7, 2_000, false, NOW(), NOW()),
       (8, 8, 2_000, false, NOW(), NOW()),
       (9, 1, 11_400, false, NOW(), NOW()),
       (9, 2, 6_700, false, NOW(), NOW()),
       (9, 3, 3_200, false, NOW(), NOW()),
       (9, 4, 9_500, false, NOW(), NOW()),
       (9, 5, 2_400, false, NOW(), NOW()),
       (9, 6, 1_700, false, NOW(), NOW()),
       (9, 7, 1_700, false, NOW(), NOW()),
       (9, 8, 2_400, false, NOW(), NOW());
