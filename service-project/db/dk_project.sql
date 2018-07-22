/*
 Navicat Premium Data Transfer

 Source Server         : v2
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : dk_project

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 22/07/2018 22:57:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for action
-- ----------------------------
DROP TABLE IF EXISTS `action`;
CREATE TABLE `action`  (
  `id` int(11) NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `op_type` int(11) NULL DEFAULT NULL,
  `act_user_id` int(11) NULL DEFAULT NULL,
  `act_user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `repo_id` int(11) NULL DEFAULT NULL,
  `repo_user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `repo_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ref_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_private` tinyint(1) NOT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `created_unix` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence`  (
  `next_val` bigint(20) NULL DEFAULT NULL
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Fixed;

-- ----------------------------
-- Records of hibernate_sequence
-- ----------------------------
INSERT INTO `hibernate_sequence` VALUES (16);

-- ----------------------------
-- Table structure for issue
-- ----------------------------
DROP TABLE IF EXISTS `issue`;
CREATE TABLE `issue`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `repo_id` int(11) NULL DEFAULT NULL,
  `index` int(11) NULL DEFAULT NULL,
  `poster_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `milestone_id` int(11) NULL DEFAULT NULL,
  `priority` int(11) NULL DEFAULT NULL,
  `assignee_id` int(11) NULL DEFAULT NULL,
  `is_closed` tinyint(1) NULL DEFAULT NULL,
  `is_pull` tinyint(1) NULL DEFAULT NULL,
  `num_comments` int(11) NULL DEFAULT NULL,
  `deadline_unix` int(11) NULL DEFAULT NULL,
  `created_unix` int(11) NULL DEFAULT NULL,
  `updated_unix` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UQE_issue_repo_index`(`repo_id`, `index`) USING BTREE,
  INDEX `IDX_issue_repo_id`(`repo_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for issue_label
-- ----------------------------
DROP TABLE IF EXISTS `issue_label`;
CREATE TABLE `issue_label`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue_id` int(11) NULL DEFAULT NULL,
  `label_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UQE_issue_label_s`(`issue_id`, `label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for issue_user
-- ----------------------------
DROP TABLE IF EXISTS `issue_user`;
CREATE TABLE `issue_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NULL DEFAULT NULL,
  `issue_id` int(11) NULL DEFAULT NULL,
  `repo_id` int(11) NULL DEFAULT NULL,
  `milestone_id` int(11) NULL DEFAULT NULL,
  `is_read` tinyint(1) NULL DEFAULT NULL,
  `is_assigned` tinyint(1) NULL DEFAULT NULL,
  `is_mentioned` tinyint(1) NULL DEFAULT NULL,
  `is_poster` tinyint(1) NULL DEFAULT NULL,
  `is_closed` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IDX_issue_user_uid`(`uid`) USING BTREE,
  INDEX `IDX_issue_user_repo_id`(`repo_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for label
-- ----------------------------
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `repo_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `color` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `num_issues` int(11) NULL DEFAULT NULL,
  `num_closed_issues` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IDX_label_repo_id`(`repo_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for map_user_project
-- ----------------------------
DROP TABLE IF EXISTS `map_user_project`;
CREATE TABLE `map_user_project`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `project_id` int(11) NULL DEFAULT NULL,
  `project_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for map_user_task
-- ----------------------------
DROP TABLE IF EXISTS `map_user_task`;
CREATE TABLE `map_user_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL,
  `task_id` int(11) NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `task_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `project_id` int(11) NOT NULL AUTO_INCREMENT,
  `project_creator_id` int(11) NULL DEFAULT NULL,
  `project_des` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `project_group_id` int(11) NULL DEFAULT NULL,
  `project_label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `project_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `project_phase` smallint(6) NULL DEFAULT NULL,
  `project_plan_end` datetime(0) NULL DEFAULT NULL,
  `project_process` int(11) NULL DEFAULT NULL,
  `project_resource_id` int(11) NULL DEFAULT NULL,
  `project_state` tinyint(1) NULL DEFAULT NULL,
  `project_time_end` datetime(0) NULL DEFAULT NULL,
  `project_time_start` datetime(0) NULL DEFAULT NULL,
  `project_type` tinyint(4) NULL DEFAULT NULL,
  `project_user_id` int(11) NULL DEFAULT NULL,
  `default_branch` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `size` bigint(20) NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`project_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES (1, 1, '协同设计平台', 1, 'est', '协同设计平台', 1, '2018-04-07 08:49:59', 2, NULL, 1, '2018-03-30 08:50:00', '2018-03-29 08:50:00', 1, 1, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `project` VALUES (2, NULL, '2342342', NULL, NULL, '234', 1, '2018-04-02 18:23:51', NULL, NULL, 0, NULL, '2018-04-02 18:23:53', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-02 18:24:01', '2018-04-02 18:24:01', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (3, NULL, '234234', NULL, NULL, '123123', 1, '2018-04-02 18:46:20', NULL, NULL, 0, NULL, '2018-04-02 18:46:22', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-02 18:46:30', '2018-04-02 18:46:30', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (4, NULL, '444444', NULL, NULL, '333', 1, '2018-04-02 18:47:23', NULL, NULL, 0, NULL, '2018-04-02 18:47:23', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-02 18:47:43', '2018-04-02 18:47:43', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (6, NULL, '123123', NULL, NULL, '3123', 1, '2018-04-11 17:05:58', NULL, NULL, NULL, NULL, '2018-04-11 17:06:03', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-11 17:06:14', '2018-04-11 17:06:14', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (10, NULL, '777777', NULL, NULL, '45', 1, '2018-04-11 17:29:43', NULL, NULL, NULL, NULL, '2018-04-11 17:29:45', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-11 17:30:45', '2018-04-11 17:30:45', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (11, NULL, 'ertert', NULL, NULL, 'rtert', 1, '2018-04-11 17:31:47', NULL, NULL, NULL, NULL, '2018-04-11 17:31:49', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-11 17:31:55', '2018-04-11 17:31:55', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (12, NULL, '678678', NULL, NULL, '6867', 1, '2018-04-11 17:31:58', NULL, NULL, NULL, NULL, '2018-04-11 17:31:58', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-11 17:32:09', '2018-04-11 17:32:09', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (13, NULL, '55555', NULL, NULL, '769', 1, '2018-04-11 17:32:11', NULL, NULL, NULL, NULL, '2018-04-11 17:32:11', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-11 17:32:20', '2018-04-11 17:32:20', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (14, NULL, '3645', NULL, NULL, '76574', 1, '2018-04-11 17:32:33', NULL, NULL, NULL, NULL, '2018-04-11 17:32:33', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-11 17:32:42', '2018-04-11 17:32:42', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (15, NULL, '56747567', NULL, NULL, '756757', 1, '2018-04-11 17:32:48', NULL, NULL, NULL, NULL, '2018-04-11 17:32:48', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-11 17:32:56', '2018-04-11 17:32:56', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (16, NULL, '123313', NULL, NULL, '2323', 1, '2018-04-11 21:15:07', NULL, NULL, NULL, NULL, '2018-04-11 21:15:10', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-11 21:15:18', '2018-04-11 21:15:18', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (17, NULL, '76767', NULL, NULL, '777', 1, '2018-04-12 11:04:42', NULL, NULL, NULL, NULL, '2018-04-12 11:04:44', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-12 11:04:48', '2018-04-12 11:04:48', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (18, NULL, '6546', NULL, NULL, '5566', 1, '2018-04-12 11:12:17', NULL, NULL, NULL, NULL, '2018-04-12 11:12:19', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-12 11:12:23', '2018-04-12 11:12:23', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (19, NULL, '风格化的风格', NULL, NULL, '法国货', 1, '2018-04-12 14:55:09', NULL, NULL, NULL, NULL, '2018-04-12 14:55:11', NULL, NULL, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-12 14:55:16', '2018-04-12 14:55:16', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (20, NULL, '5242342342', NULL, NULL, '13124', 1, '2018-04-12 17:33:38', NULL, NULL, NULL, NULL, '2018-04-12 17:33:40', NULL, 1, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-12 17:33:52', '2018-04-12 17:33:52', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `project` VALUES (21, NULL, '45642', NULL, NULL, '6786', 1, '2018-04-13 14:17:54', NULL, NULL, NULL, NULL, '2018-04-13 14:17:56', NULL, 1, NULL, NULL, 'hollykunge', '1', '127.0.0.1', '2018-04-13 14:18:02', '2018-04-13 14:18:02', '1', 'hollykunge', '127.0.0.1');

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `task_id` int(11) NOT NULL AUTO_INCREMENT,
  `task_creator_id` int(11) NULL DEFAULT NULL COMMENT '暂不使用',
  `task_des` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `task_executor_id` int(11) NULL DEFAULT NULL COMMENT '任务执行人',
  `task_is_leaf` tinyblob NULL COMMENT '是否子任务',
  `task_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `task_parent_id` int(11) NULL DEFAULT NULL COMMENT '爹任务ID',
  `task_plan_end` datetime(0) NULL DEFAULT NULL COMMENT '计划结束时间',
  `task_process` int(11) NULL DEFAULT NULL COMMENT '任务进度',
  `task_project_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属项目名称',
  `task_project_id` int(11) NULL DEFAULT NULL COMMENT '所属项目ID',
  `task_resource_id` int(11) NULL DEFAULT NULL COMMENT '任务资源ID',
  `task_state` tinyint(4) NULL DEFAULT NULL COMMENT '任务状态',
  `task_time_end` datetime(0) NULL DEFAULT NULL COMMENT '任务实际结束时间',
  `task_time_start` datetime(0) NULL DEFAULT NULL COMMENT '任务实际开始时间',
  `task_type` tinyint(4) NULL DEFAULT NULL COMMENT '任务类型',
  `crt_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `default_branch` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '默认分支',
  `size` bigint(20) NULL DEFAULT NULL COMMENT '大小',
  `num_watches` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关注量',
  `num_stars` int(11) NULL DEFAULT NULL COMMENT '标星量',
  `num_forks` int(11) NULL DEFAULT NULL COMMENT '分支任务数',
  `num_issues` int(11) NULL DEFAULT NULL COMMENT '问题量',
  `num_closed_issues` int(11) NULL DEFAULT NULL COMMENT '关闭问题量',
  `num_pulls` int(11) NULL DEFAULT NULL COMMENT '拉取量',
  `num_closed_pulls` int(11) NULL DEFAULT NULL COMMENT '关闭拉取量',
  `num_milestones` int(11) NULL DEFAULT NULL COMMENT '里程碑数',
  `num_closed_milestones` int(11) NULL DEFAULT NULL COMMENT '关闭里程碑量',
  `enable_issues` tinyint(1) NULL DEFAULT NULL COMMENT '是否允许提问',
  `allow_public_issues` tinyint(1) NULL DEFAULT NULL COMMENT '允许公共问题',
  `is_fork` tinyint(1) NULL DEFAULT NULL COMMENT '重用',
  `fork_id` int(11) NULL DEFAULT NULL COMMENT '重用id',
  `head` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `merge_to` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `project_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目路径',
  `access_restriction` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问限制',
  `authorization_control` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`task_id`) USING BTREE,
  INDEX `FKkg64gl3x0mqlkqrkyn81gyew3`(`task_project_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (5, NULL, '7897698', NULL, NULL, '90', NULL, '2018-04-10 19:28:16', 2, NULL, 1, NULL, NULL, NULL, NULL, NULL, 'null', 'null', 'null', '2018-04-10 19:28:47', '2018-04-10 19:28:47', 'null', 'null', 'null', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `task` VALUES (7, NULL, '123123', NULL, NULL, '23123', NULL, '2018-04-11 17:06:42', 3, NULL, 1, NULL, NULL, NULL, NULL, NULL, 'null', 'null', 'null', '2018-04-11 17:06:46', '2018-04-11 17:06:46', 'null', 'null', 'null', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `task` VALUES (8, NULL, '123123', NULL, NULL, '231233', NULL, '2018-04-11 17:06:42', 3, NULL, 1, NULL, NULL, NULL, NULL, NULL, 'null', 'null', 'null', '2018-04-11 17:09:18', '2018-04-11 17:10:27', 'null', 'null', 'null', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `task` VALUES (9, NULL, '123123', NULL, NULL, '231233', NULL, '2018-04-11 17:06:42', 3, NULL, 1, NULL, NULL, NULL, NULL, NULL, 'null', 'null', 'null', '2018-04-11 17:16:58', '2018-04-11 17:22:28', 'null', 'null', 'null', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `task` VALUES (10, NULL, '123123123', 1, NULL, '12313', NULL, '2018-04-13 10:42:10', 1, NULL, 1, NULL, NULL, NULL, NULL, NULL, 'null', 'null', 'null', '2018-04-13 10:42:14', '2018-04-13 10:42:14', 'null', 'null', 'null', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `task` VALUES (11, NULL, '456456456', 1, NULL, '6544', NULL, '2018-04-13 10:42:23', 3, NULL, 1, NULL, NULL, NULL, NULL, NULL, 'null', 'null', 'null', '2018-04-13 10:42:32', '2018-04-13 10:42:32', 'null', 'null', 'null', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `task` VALUES (12, NULL, '567567', 1, NULL, '6456', NULL, '2018-04-13 14:17:14', 3, NULL, 1, NULL, NULL, NULL, NULL, NULL, 'null', 'null', 'null', '2018-04-13 14:17:18', '2018-04-13 14:17:18', 'null', 'null', 'null', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `task` VALUES (13, NULL, '234234', 1, NULL, '43244', NULL, '2018-04-13 14:18:47', 2, NULL, 21, NULL, NULL, NULL, NULL, NULL, 'null', 'null', 'null', '2018-04-13 14:18:51', '2018-04-13 14:18:51', 'null', 'null', 'null', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `task` VALUES (14, NULL, 'wrerwer', 1, NULL, '34234', NULL, '2018-04-17 11:31:19', 2, NULL, 1, NULL, NULL, NULL, NULL, NULL, 'null', 'null', 'null', '2018-04-17 11:31:24', '2018-04-17 11:31:24', 'null', 'null', 'null', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lower_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注名称',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `num_task` int(11) NULL DEFAULT NULL COMMENT '任务数',
  `num_members` int(11) NULL DEFAULT NULL COMMENT '成员数',
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `can_admin` tinyblob NULL,
  `can_fork` tinyblob NULL,
  `can_create` tinyblob NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team_repo
-- ----------------------------
DROP TABLE IF EXISTS `team_repo`;
CREATE TABLE `team_repo`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NULL DEFAULT NULL,
  `task_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `team_id` int(11) NULL DEFAULT NULL,
  `team_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '读写权限',
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team_user
-- ----------------------------
DROP TABLE IF EXISTS `team_user`;
CREATE TABLE `team_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_id` int(11) NULL DEFAULT NULL COMMENT '任务组id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '人员id',
  `team_role` tinyint(6) NULL DEFAULT NULL COMMENT '团队角色',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UQE_team_user_s`(`team_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
