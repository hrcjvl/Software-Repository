DROP TABLE IF EXISTS `time_to_failure`;
CREATE TABLE `time_to_failure`  (
                                 `id` int(11) NOT NULL AUTO_INCREMENT,
                                 `failure_number` varchar(40) COLLATE utf8_bin NULL DEFAULT NULL,
                                 `time_to_fail` varchar(40) COLLATE utf8_bin NULL DEFAULT NULL,
                                 PRIMARY KEY (`id`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;