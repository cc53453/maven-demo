-- DDL
CREATE TABLE IF NOT EXISTS `demo`  (
  `id` int(11) NOT NULL,
  `name` varchar(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- DML
INSERT INTO `demo` VALUES (1, '11');
INSERT INTO `demo` VALUES (2, '22');
