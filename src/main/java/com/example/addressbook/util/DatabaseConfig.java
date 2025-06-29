package com.example.addressbook.util;

public final class DatabaseConfig {
    private DatabaseConfig(){}

    public static final String DB_NAME = "address_book";
    public static final String HOST = "localhost";
    public static final String PORT = "3306";

    public static final String createAddress = "CREATE TABLE `address` (\n" +
            "  `id` int NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(100) DEFAULT NULL,\n" +
            "  `number` varchar(15) DEFAULT NULL,\n" +
            "  `email` varchar(100) DEFAULT NULL,\n" +
            "  `address` varchar(250) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `name_UNIQUE` (`name`),\n" +
            "  UNIQUE KEY `email_UNIQUE` (`email`),\n" +
            "  UNIQUE KEY `number_UNIQUE` (`number`),\n" +
            "  UNIQUE KEY `address_UNIQUE` (`address`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

    public static final String createGroups = "CREATE TABLE `contact_groups` (\n" +
            "  `id` int NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(100) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `name_UNIQUE` (`name`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

    public static final String createAddressGroups = "CREATE TABLE `address_contact_groups` (\n" +
            "  `address_id` int NOT NULL,\n" +
            "  `contact_groups_id` int NOT NULL,\n" +
            "  KEY `fk_address_has_groups_groups1_idx` (`contact_groups_id`),\n" +
            "  KEY `fk_address_has_groups_address_idx` (`address_id`),\n" +
            "  KEY `fk_address_has_groups_address_idx1` (`contact_groups_id`,`address_id`),\n" +
            "  CONSTRAINT `fk_address_contact` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON DELETE CASCADE,\n" +
            "  CONSTRAINT `fk_contact_group` FOREIGN KEY (`contact_groups_id`) REFERENCES `contact_groups` (`id`) ON DELETE CASCADE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
}
