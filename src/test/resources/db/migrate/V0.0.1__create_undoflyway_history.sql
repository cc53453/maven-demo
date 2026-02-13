CREATE TABLE IF NOT EXISTS undoflyway_history (
  `installed_rank` int(11) NOT NULL AUTO_INCREMENT,
  `version` varchar(50) NULL DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) NULL DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  `installed_rank_in_flyway_schema_history` int(11) NOT NULL, 
  PRIMARY KEY (`installed_rank`)
)