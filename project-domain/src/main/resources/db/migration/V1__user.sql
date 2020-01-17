CREATE TABLE `user`
(
  `id`         BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME(6)                             DEFAULT NULL,
  `password`   VARCHAR(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `username`   VARCHAR(255) UNIQUE NOT NULL COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;