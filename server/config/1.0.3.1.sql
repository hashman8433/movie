-- movie_test.img_file definition

CREATE TABLE `img_file` (
  `id` varchar(64) NOT NULL,
  `video_file_id` varchar(64) NOT NULL,
  `file_path` varchar(1000) DEFAULT NULL,
  `file_path_web` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `column_6` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `video_file_id` (`video_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- movie_test.tag_file definition

CREATE TABLE `tag_file` (
  `tag_id` varchar(255) DEFAULT NULL,
  `file_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- movie_test.video_file definition

CREATE TABLE `video_file` (
  `id` varchar(64) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `file_size` varchar(255) DEFAULT NULL,
  `file_path_web` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `scan_status` varchar(255) DEFAULT NULL,
  `is_delete` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- movie_test.video_file_1 definition

CREATE TABLE `video_file_1` (
  `id` varchar(64) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `file_size` varchar(255) DEFAULT NULL,
  `file_path_web` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `scan_status` varchar(255) DEFAULT NULL,
  `is_delete` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`title`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- movie_test.video_tag definition

CREATE TABLE `video_tag` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `tag_type` varchar(255) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`tag_type`),
  KEY `name` (`name`) USING BTREE,
  KEY `tag_type` (`tag_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

