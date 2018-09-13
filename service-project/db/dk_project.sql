/*
 Navicat Premium Data Transfer

 Source Server         : workhub
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : dk_project

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 13/09/2018 17:32:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
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
  `crt_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_type` int(11) NULL DEFAULT NULL,
  `poster_id` int(11) NULL DEFAULT NULL,
  `issue_id` int(11) NULL DEFAULT NULL,
  `commit_id` int(11) NULL DEFAULT NULL,
  `line` int(11) NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `created_unix` int(11) NULL DEFAULT NULL,
  `updated_unix` int(11) NULL DEFAULT NULL,
  `commit_sha` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IDX_comment_issue_id`(`issue_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence`  (
  `next_val` bigint(20) NULL DEFAULT NULL
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Fixed;

-- ----------------------------
-- Table structure for issue
-- ----------------------------
DROP TABLE IF EXISTS `issue`;
CREATE TABLE `issue`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `repo_id` int(11) NULL DEFAULT NULL,
  `issue_index` int(11) NULL DEFAULT NULL,
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
  `crt_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UQE_issue_repo_index`(`repo_id`, `issue_index`) USING BTREE,
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
  `permission` tinyint(6) NULL DEFAULT NULL COMMENT '0-NONE 1-VIEW 2-CLONE 3-PUSH 4-ADMIN',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for milestone
-- ----------------------------
DROP TABLE IF EXISTS `milestone`;
CREATE TABLE `milestone`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `is_closed` tinyint(1) NULL DEFAULT NULL,
  `num_issues` int(11) NULL DEFAULT NULL,
  `num_closed_issues` int(11) NULL DEFAULT NULL,
  `completeness` int(11) NULL DEFAULT NULL,
  `deadline_unix` bigint(20) NULL DEFAULT NULL,
  `closed_date_unix` bigint(20) NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IDX_milestone_task_id`(`task_id`) USING BTREE
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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pull_request
-- ----------------------------
DROP TABLE IF EXISTS `pull_request`;
CREATE TABLE `pull_request`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `issue_id` bigint(20) NULL DEFAULT NULL,
  `index` bigint(20) NULL DEFAULT NULL,
  `head_repo_id` bigint(20) NULL DEFAULT NULL,
  `base_repo_id` bigint(20) NULL DEFAULT NULL,
  `head_user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `head_branch` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `base_branch` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `merge_base` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `has_merged` tinyint(1) NULL DEFAULT NULL,
  `merged_commit_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `merger_id` bigint(20) NULL DEFAULT NULL,
  `merged_unix` bigint(20) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `context` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `task_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
  `size` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '大小',
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
  `access_restriction` enum('VIEW','PUSH','CLONE','NONE') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问限制\r\nVIEW\r\n PUSH\r\n CLONE\r\n NONE',
  `authorization_control` enum('AUTHENTICATED','NAMED') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`task_id`) USING BTREE,
  INDEX `FKkg64gl3x0mqlkqrkyn81gyew3`(`task_project_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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

-- ----------------------------
-- Table structure for ticket
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket`  (
  `id` int(1) NOT NULL,
  `project_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `task_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `number` bigint(11) NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `body` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `topic` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `responsible` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `milestone` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `merge_sha` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `merge_to` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `insertions` int(255) NULL DEFAULT NULL,
  `deletions` int(255) NULL DEFAULT NULL,
  `priority` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `severity` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
