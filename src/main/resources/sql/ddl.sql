CREATE DATABASE IF NOT EXISTS irmp DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

CREATE TABLE `task` (
    `id`                      bigint(20)      NOT NULL    AUTO_INCREMENT  COMMENT '主键'
    , `task_key`              varchar(50)     NOT NULL                    COMMENT '任务编号'
    , `task_name`             varchar(50)     NOT NULL                    COMMENT '任务编号'
    , `transform_sql`         varchar(50)     DEFAULT NULL                COMMENT 'etl的sql'
    , `hive_support`          int(2)          DEFAULT NULL                COMMENT '是否需要读取或者写hive表 1：需要；2：不需要'
    , `spark_config`          text            DEFAULT NULL                COMMENT 'etl的sql'
    , `latch`                 int(2)          DEFAULT NULL                COMMENT '是否执行该任务的开关 1：执行；2：跳过'
    , `created`               datetime        NOT NULL                    COMMENT '创建日期'
    , `modified`              datetime        NOT NULL                    COMMENT '修改时间'
    , `yn`                    int(2)          DEFAULT 1                   COMMENT '是否有效'
    , PRIMARY KEY (`id`)
    , unique key `id_unique` (`id`)
    , unique key `task_key_unique` (`task_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

CREATE TABLE `source` (
    `id`                      bigint(20)      NOT NULL    AUTO_INCREMENT  COMMENT '主键'
    , `task_id`               bigint(20)      NOT NULL                    COMMENT '任务id'
    , `temp_view_name`        varchar(50)     NOT NULL                    COMMENT 'spark临时表的表名'
    , `source_type`           varchar(200)    DEFAULT NULL                COMMENT '数据源类的全限定名'
    , `source_config_type`    varchar(200)    DEFAULT NULL                COMMENT '数据源config类的全限定名'
    , `source_config_json`    text            DEFAULT NULL                COMMENT 'source的配置，以json格式保存'
    , `created`               datetime        NOT NULL                    COMMENT '创建日期'
    , `modified`              datetime        NOT NULL                    COMMENT '修改时间'
    , `yn`                    int(2)          DEFAULT 1                   COMMENT '是否有效'
    , PRIMARY KEY (`id`)
    , unique key `id_unique` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据源表';

CREATE TABLE `sink` (
    `id`                      bigint(20)      NOT NULL    AUTO_INCREMENT  COMMENT '主键'
    , `task_id`               bigint(20)      NOT NULL                    COMMENT '任务id'
    , `sink_type`             varchar(200)    DEFAULT NULL                COMMENT 'sink类的全限定名'
    , `sink_config_type`      varchar(200)    DEFAULT NULL                COMMENT 'sink的config类的全限定名'
    , `sink_config_json`      text            DEFAULT NULL                COMMENT 'sink的配置，以json格式保存'
    , `created`               datetime        NOT NULL                    COMMENT '创建日期'
    , `modified`              datetime        NOT NULL                    COMMENT '修改时间'
    , `yn`                    int(2)          DEFAULT 1                   COMMENT '是否有效'
    , PRIMARY KEY (`id`)
    , unique key `id_unique` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='写数据器表';

CREATE TABLE `handle` (
    `id`                      bigint(20)      NOT NULL    AUTO_INCREMENT  COMMENT '主键'
    , `task_id`               bigint(20)      NOT NULL                    COMMENT '任务id'
    , `executeType`           int(2)          NOT NULL                    COMMENT 'handle执行类型 1：前置；2：后置；3：异常'
    , `handle_type`           varchar(200)    DEFAULT NULL                COMMENT 'handle的全限定名'
    , `handle_config_type`    varchar(200)    DEFAULT NULL                COMMENT 'handle的config类的全限定名'
    , `handle_config_json`    text            DEFAULT NULL                COMMENT 'handle的配置，以json格式保存'
    , `created`               datetime        NOT NULL                    COMMENT '创建日期'
    , `modified`              datetime        NOT NULL                    COMMENT '修改时间'
    , `yn`                    int(2)          DEFAULT 1                   COMMENT '是否有效'
    , PRIMARY KEY (`id`)
    , unique key `id_unique` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='handle处理表';

CREATE TABLE `task_log` (
    `id`                      bigint(20)      NOT NULL    AUTO_INCREMENT  COMMENT '主键'
    , `task_key`              varchar(50)     NOT NULL                    COMMENT '任务编号'
    , `start_time`            datetime        NOT NULL                    COMMENT '任务开始时间'
    , `end_time`              datetime        NOT NULL                    COMMENT '任务结束时间'
    , `status`                int(2)          NOT NULL                    COMMENT '运行状态'
    , `error_log`             text            DEFAULT NULL                COMMENT '运行日志记录'
    , `created`               datetime        NOT NULL                    COMMENT '创建日期'
    , `modified`              datetime        NOT NULL                    COMMENT '修改时间'
    , `yn`                    int(2)          DEFAULT 1                   COMMENT '是否有效'
    , PRIMARY KEY (`id`)
    , unique key `id_unique` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表运行日志表';