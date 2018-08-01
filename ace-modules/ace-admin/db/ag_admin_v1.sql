/*
 Navicat Premium Data Transfer

 Source Server         : workhub
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : ag_admin_v1

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 01/08/2018 17:11:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_element
-- ----------------------------
DROP TABLE IF EXISTS `base_element`;
CREATE TABLE `base_element`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源编码',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源类型',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源路径',
  `menu_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源关联菜单',
  `parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `path` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源树状检索路径',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源请求类型',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_element
-- ----------------------------
INSERT INTO `base_element` VALUES (3, 'userManager:btn_add', 'button', '新增', '/admin/user', '1', NULL, NULL, 'POST', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (4, 'userManager:btn_edit', 'button', '编辑', '/admin/user', '1', NULL, NULL, 'PUT', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (5, 'userManager:btn_del', 'button', '删除', '/admin/user', '1', NULL, NULL, 'DELETE', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (9, 'menuManager:element', 'uri', '按钮页面', '/admin/element', '6', NULL, NULL, 'GET', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (10, 'menuManager:btn_add', 'button', '新增', '/admin/menu', '6', NULL, NULL, 'POST', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (11, 'menuManager:btn_edit', 'button', '编辑', '/admin/menu', '6', '', '', 'PUT', '', '2017-06-24 00:00:00', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `base_element` VALUES (12, 'menuManager:btn_del', 'button', '删除', '/admin/menu', '6', '', '', 'DELETE', '', '2017-06-24 00:00:00', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `base_element` VALUES (13, 'menuManager:btn_element_add', 'button', '新增元素', '/admin/element', '6', NULL, NULL, 'POST', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (14, 'menuManager:btn_element_edit', 'button', '编辑元素', '/admin/element', '6', NULL, NULL, 'PUT', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (15, 'menuManager:btn_element_del', 'button', '删除元素', '/admin/element', '6', NULL, NULL, 'DELETE', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (16, 'groupManager:btn_add', 'button', '新增', '/admin/group', '7', NULL, NULL, 'POST', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (17, 'groupManager:btn_edit', 'button', '编辑', '/admin/group', '7', NULL, NULL, 'PUT', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (18, 'groupManager:btn_del', 'button', '删除', '/admin/group', '7', NULL, NULL, 'DELETE', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (19, 'groupManager:btn_userManager', 'button', '分配用户', '/admin/group/{*}/user', '7', NULL, NULL, 'PUT', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (20, 'groupManager:btn_resourceManager', 'button', '分配权限', '/admin/group/{*}/authority', '7', NULL, NULL, 'GET', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (21, 'groupManager:menu', 'uri', '分配菜单', '/admin/group/{*}/authority/menu', '7', NULL, NULL, 'POST', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (22, 'groupManager:element', 'uri', '分配资源', '/admin/group/{*}/authority/element', '7', NULL, NULL, 'POST', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (23, 'userManager:view', 'uri', '查看', '/admin/user', '1', '', '', 'GET', '', '2017-06-26 00:00:00', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `base_element` VALUES (24, 'menuManager:view', 'uri', '查看', '/admin/menu', '6', '', '', 'GET', '', '2017-06-26 00:00:00', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `base_element` VALUES (27, 'menuManager:element_view', 'uri', '查看', '/admin/element', '6', NULL, NULL, 'GET', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (28, 'groupManager:view', 'uri', '查看', '/admin/group', '7', NULL, NULL, 'GET', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (32, 'groupTypeManager:view', 'uri', '查看', '/admin/groupType', '8', NULL, NULL, 'GET', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (33, 'groupTypeManager:btn_add', 'button', '新增', '/admin/groupType', '8', NULL, NULL, 'POST', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (34, 'groupTypeManager:btn_edit', 'button', '编辑', '/admin/groupType', '8', NULL, NULL, 'PUT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (35, 'groupTypeManager:btn_del', 'button', '删除', '/admin/groupType', '8', NULL, NULL, 'DELETE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (41, 'gateLogManager:view', 'button', '查看', '/admin/gateLog', '27', NULL, NULL, 'GET', '', '2017-07-01 00:00:00', '1', 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (42, 'serviceManager:view', 'URI', '查看', '/auth/service', '30', NULL, NULL, 'GET', NULL, '2017-12-26 20:17:42', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (43, 'serviceManager:btn_add', 'button', '新增', '/admin/groupType', '30', NULL, NULL, 'POST', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (44, 'serviceManager:btn_edit', 'button', '编辑', '/admin/groupType', '30', NULL, NULL, 'PUT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (45, 'serviceManager:btn_del', 'button', '删除', '/admin/groupType', '30', NULL, NULL, 'DELETE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (46, 'serviceManager:btn_clientManager', 'button', '服务授权', '/auth/service/{*}/client', '30', NULL, NULL, 'POST', NULL, '2017-12-30 16:32:48', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (47, 'orgManager:btn_add', 'button', '新增组织', '/admin/org', '39', NULL, NULL, 'POST', NULL, '2018-03-19 16:03:08', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (48, 'orgManager:btn_edit', 'button', '编辑组织', '/admin/org', '39', NULL, NULL, 'PUT', NULL, '2018-03-19 16:03:38', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (49, 'orgManager:btn_del', 'button', '删除组织', '/admin/org', '39', NULL, NULL, 'DELETE', NULL, '2018-03-19 16:04:11', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (50, 'orgManager:view', 'uri', '查看组织', '/admin/org', '39', NULL, NULL, 'GET', NULL, '2018-03-19 16:04:34', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (51, 'orgManager:btn_userManager', 'button', '分配用户', '/admin/org/{*}/user', '39', NULL, NULL, 'PUT', NULL, '2018-03-19 16:05:28', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (52, 'allProjects:btn_add', 'button', '新增项目', '/project/project', '37', NULL, NULL, 'POST', NULL, '2018-03-23 18:19:55', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (53, 'allProjects:btn_edit', 'button', '编辑项目', '/project/project', '37', NULL, NULL, 'PUT', NULL, '2018-03-23 19:26:59', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (54, 'allProject:btn_del', 'button', '删除项目', '/project/project', '37', NULL, NULL, 'DELETE', NULL, '2018-03-23 19:28:14', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (55, 'allProject:view', 'uri', '查看项目', '/project/project', '37', NULL, NULL, 'GET', NULL, '2018-03-23 19:29:48', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (56, ' allTasks:btn_add 	', 'button', '添加任务', '/project/task', '38', NULL, NULL, 'POST', NULL, '2018-08-01 16:26:54', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (57, 'allTasks:btn_edit', 'button', '修改任务', '/project/task', '38', NULL, NULL, 'PUT', NULL, '2018-08-01 16:28:38', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (58, 'allTasks:btn_del', 'button', '删除任务', '/project/task', '38', NULL, NULL, 'DELETE', NULL, '2018-08-01 16:29:58', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_element` VALUES (59, 'allTasks:btn_view', 'uri', '查看任务', '/project/task', '38', NULL, NULL, 'GET', NULL, '2018-08-01 16:30:57', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_group
-- ----------------------------
DROP TABLE IF EXISTS `base_group`;
CREATE TABLE `base_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `parent_id` int(11) NOT NULL COMMENT '上级节点',
  `path` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '树状关系',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `group_type` int(11) NOT NULL COMMENT '角色组类型',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_group
-- ----------------------------
INSERT INTO `base_group` VALUES (1, 'adminRole', '管理员', -1, '/adminRole', NULL, 1, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group` VALUES (6, 'company', 'DK集团', -1, '/company', NULL, 2, '', NULL, NULL, NULL, NULL, '2018-03-15 11:07:03', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group` VALUES (7, 'financeDepart', '研发部', 6, '/company/financeDepart', NULL, 2, '', NULL, NULL, NULL, NULL, '2018-03-15 11:07:32', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group` VALUES (8, 'hrDepart', '人力资源部', 6, '/company/hrDepart', NULL, 2, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group` VALUES (12, 'testUser', '测试用户', -1, '/testUser', NULL, 1, NULL, '2018-07-22 01:35:55', '1', 'hollykunge', '127.0.0.1', '2018-07-22 01:35:55', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_group_leader
-- ----------------------------
DROP TABLE IF EXISTS `base_group_leader`;
CREATE TABLE `base_group_leader`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_group_leader
-- ----------------------------
INSERT INTO `base_group_leader` VALUES (6, '9', '4', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group_leader` VALUES (16, '11', '6', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group_leader` VALUES (17, '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_group_member
-- ----------------------------
DROP TABLE IF EXISTS `base_group_member`;
CREATE TABLE `base_group_member`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_group_member
-- ----------------------------
INSERT INTO `base_group_member` VALUES (2, '4', '2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group_member` VALUES (9, '9', '4', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group_member` VALUES (18, '1', '6', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group_member` VALUES (19, '1', '8', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group_member` VALUES (20, '1', '7', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group_member` VALUES (21, '1', '13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_group_type
-- ----------------------------
DROP TABLE IF EXISTS `base_group_type`;
CREATE TABLE `base_group_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `crt_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建主机',
  `upd_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后更新人ID',
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后更新人',
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后更新主机',
  `attr1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_group_type
-- ----------------------------
INSERT INTO `base_group_type` VALUES (1, 'role', '角色类型', 'role', NULL, NULL, NULL, NULL, '2018-03-15 11:08:07', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group_type` VALUES (2, 'depart', '部门类型', NULL, NULL, NULL, NULL, NULL, '2017-08-25 17:52:43', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_group_type` VALUES (3, 'free', '自定义类型', 'sadf', NULL, NULL, NULL, NULL, '2017-08-26 08:22:25', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_menu`;
CREATE TABLE `base_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径编码',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `parent_id` int(11) NOT NULL COMMENT '父级节点',
  `href` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源路径',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `order_num` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单上下级关系',
  `enabled` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '启用禁用',
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_menu
-- ----------------------------
INSERT INTO `base_menu` VALUES (1, 'userManager', '用户管理', 5, '/admin/user', 'user', 'menu', 0, '', '/adminSys/baseManager/userManager', NULL, NULL, NULL, NULL, NULL, '2018-04-23 17:27:02', '1', 'hollykunge', '127.0.0.1', '_import(\'admin/user/index\')', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (5, 'baseManager', '基础配置管理   ', 13, '/admin', 'sliders', 'dirt', 0, '', '/adminSys/baseManager', NULL, NULL, NULL, NULL, NULL, '2018-04-23 17:54:45', '1', 'hollykunge', '127.0.0.1', 'Layout', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (6, 'menuManager', '菜单管理', 5, '/admin/menu', 'navicon', 'menu', 0, '', '/adminSys/baseManager/menuManager', NULL, NULL, NULL, NULL, NULL, '2018-04-23 17:27:21', '1', 'hollykunge', '127.0.0.1', '_import(\'admin/menu/index\')', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (7, 'groupManager', '角色权限', 5, '/admin/group', 'toggle-on', 'menu', 0, '', '/adminSys/baseManager/groupManager', NULL, NULL, NULL, NULL, NULL, '2018-04-23 17:27:36', '1', 'hollykunge', '127.0.0.1', 'import(\'admin/group/index\')', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (8, 'groupTypeManager', '角色类型', 5, '/admin/groupType', 'vcard', 'menu', 0, '', '/adminSys/baseManager/groupTypeManager', NULL, NULL, NULL, NULL, NULL, '2018-04-23 17:35:31', '1', 'hollykunge', '127.0.0.1', '_import(\'admin/groupType/index\')', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (13, 'adminSys', '权限管理', -1, '/base', 'gears', 'dirt', 0, '', '/adminSys', NULL, NULL, NULL, NULL, NULL, '2018-04-23 17:54:05', '1', 'hollykunge', '127.0.0.1', 'Layout', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (21, 'dictManager', '数据字典', 5, '', 'graduation-cap', NULL, 0, '', '/adminSys/baseManager/dictManager', NULL, NULL, NULL, NULL, NULL, '2018-04-23 17:28:03', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (27, 'gateLogManager', '操作日志', 5, '/admin/gateLog', 'ioxhost', 'menu', 0, '', '/adminSys/baseManager/gateLogManager', NULL, '2017-07-01 00:00:00', '1', 'admin', '0:0:0:0:0:0:0:1', '2018-04-23 17:28:29', '1', 'hollykunge', '127.0.0.1', '_import(\'admin/gateLog/index\')', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (29, 'authManager', '服务权限管理   ', 13, '/auth', 'server', NULL, 0, '服务权限管理', '/adminSys/authManager', NULL, '2017-12-26 19:54:45', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:54:55', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (30, 'serviceManager', '服务权限管理', 29, '/auth/service', 'server', NULL, 0, '服务管理', '/adminSys/authManager/serviceManager', NULL, '2017-12-26 19:56:06', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:30:20', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (31, 'monitorManager', '监控模块管理   ', 13, NULL, 'cube', NULL, 0, NULL, '/adminSys/monitorManager', NULL, '2018-02-25 09:36:35', '1', 'hollykunge', '127.0.0.1', '2018-04-23 18:08:58', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (32, 'serviceEurekaManager', '服务注册中心', 31, NULL, 'handshake-o', NULL, 0, NULL, '/adminSys/monitorManager/serviceEurekaManager', NULL, '2018-02-25 09:37:04', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:32:25', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (33, 'serviceMonitorManager', '服务状态监控', 31, NULL, 'heartbeat', NULL, 0, NULL, '/adminSys/monitorManager/serviceEurekaManager', NULL, '2018-02-25 09:37:05', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:32:48', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (34, 'serviceZipkinManager', '服务链路监控', 31, NULL, 'share-alt', NULL, 0, NULL, '/adminSys/monitorManager/serviceZipkinManager', NULL, '2018-02-25 09:38:05', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:33:00', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (35, 'projectSys', '项目管理', -1, '/base', 'folder', 'dirt', 0, NULL, '/adminSys/projectSys', NULL, '2018-03-05 14:25:27', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:33:15', '1', 'hollykunge', '127.0.0.1', 'Layout', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (37, 'allProjects', '全部项目', 35, '/project/project', 'folder-open', 'menu', 0, NULL, '/adminSys/projectSys/allProjects', NULL, '2018-03-15 11:25:54', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:33:36', '1', 'hollykunge', '127.0.0.1', '_import(\'project/project/index\')', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (38, 'allTasks', '全部任务', 41, '/project/task', 'list', 'menu', 0, NULL, '/adminSys/taskSys/allTasks', NULL, '2018-03-15 11:33:33', '1', 'hollykunge', '127.0.0.1', '2018-08-01 16:24:40', '1', 'hollykunge', '127.0.0.1', '_import(\'', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (39, 'orgManager', '组织管理', 5, 'admin/org', 'window-restore', 'menu', 0, NULL, '/adminSys/baseManager/baseManager', NULL, '2018-03-19 15:58:05', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:28:53', '1', 'hollykunge', '127.0.0.1', '_import(\'admin/organize/index\')', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (41, 'taskSys', '我的任务', -1, '/base', 'list', 'dirt', 0, '', '/adminSys/taskSys', '', '2018-03-05 14:25:27', '1', 'hollykunge', '127.0.0.1', '2018-07-25 08:59:45', '1', 'hollykunge', '127.0.0.1', 'Layout', '', '', '', '', '', '', '');
INSERT INTO `base_menu` VALUES (42, 'allTeams', '全部团队', 46, '', 'th', 'menu', 0, '', '/teamManager/allTeams', '', '2018-04-18 10:58:53', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:34:21', '1', 'hollykunge', '127.0.0.1', '_import(\'team/myTeams\')', '', '', '', '', '', '', '');
INSERT INTO `base_menu` VALUES (44, 'myProjects', '我创建的', 35, '/project/project', 'pencil-square', 'menu', 0, '我创建的项目', '/adminSys/projectSys/myProjects', '', '2018-07-22 00:25:21', '1', 'hollykunge', '127.0.0.1', '2018-07-22 00:25:47', '1', 'hollykunge', '127.0.0.1', '_import(\'project/project/index\')', '', '', '', '', '', '', '');
INSERT INTO `base_menu` VALUES (45, 'joinedProjects', '我参加的', 35, '/project/project', 'share-alt-square', 'menu', 0, '我参加的项目', '/adminSys/projectSys/joinedProjects', '', '2018-07-22 00:25:21', '1', 'hollykunge', '127.0.0.1', '2018-07-23 10:08:22', '1', 'hollykunge', '127.0.0.1', '_import(\'project/project/index\')', '', '', '', '', '', '', '');
INSERT INTO `base_menu` VALUES (46, 'teamManager', '团队管理', -1, '/base', 'users', 'dirt', 0, NULL, '/teamManager', NULL, '2018-04-18 10:57:00', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:34:04', '1', 'hollykunge', '127.0.0.1', 'Layout', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (47, 'myTasks', '我创建的', 41, '/project/task', 'pencil-square', 'menu', 0, '我创建的任务', '/adminSys/taskSys/myTasks', '', '2018-07-22 00:25:21', '1', 'hollykunge', '127.0.0.1', '2018-07-22 00:25:47', '1', 'hollykunge', '127.0.0.1', '_import(\'project/project/index\')', '', '', '', '', '', '', '');
INSERT INTO `base_menu` VALUES (48, 'joinedTasks', '我参加的', 41, '/project/task', 'share-alt-square', 'menu', 0, '我参加的任务', '/adminSys/taskSys/joinedTasks', '', '2018-07-22 00:25:21', '1', 'hollykunge', '127.0.0.1', '2018-07-23 10:08:22', '1', 'hollykunge', '127.0.0.1', '_import(\'project/project/index\')', '', '', '', '', '', '', '');
INSERT INTO `base_menu` VALUES (49, 'myTeams', '我创建的', 46, NULL, 'user-plus', 'menu', 0, '我创建的团队', '/teamManager/myTeams', NULL, '2018-04-18 10:58:53', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:34:21', '1', 'hollykunge', '127.0.0.1', '_import(\'team/myTeams\')', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu` VALUES (50, 'joinedTeams', '我参加的', 46, NULL, 'user-circle', 'menu', 0, '我参加的团队', '/teamManager/joinedTeams', NULL, '2018-04-18 11:00:19', '1', 'hollykunge', '127.0.0.1', '2018-04-23 17:34:42', '1', 'hollykunge', '127.0.0.1', '_import(\'team/joinedTeams\')', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_org
-- ----------------------------
DROP TABLE IF EXISTS `base_org`;
CREATE TABLE `base_org`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父节点',
  `org_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_org
-- ----------------------------
INSERT INTO `base_org` VALUES (1, '十一室', '211', 2, 'b', '信息技术中心', '2018-03-19 15:32:26', '1', 'hollykunge', '127.0.0.1', '2018-03-19 20:01:51', '6', '姬海南', '127.0.0.1', '', '', '');
INSERT INTO `base_org` VALUES (2, '三室', '203', 2, 'b', '武器装备总体室', '2018-03-19 15:32:30', '1', 'hollykunge', '127.0.0.1', '2018-03-19 19:46:05', '6', '姬海南', '127.0.0.1', '', '', '');
INSERT INTO `base_org` VALUES (3, '一室', '201', 2, 'b', '', '2018-03-19 15:32:34', '1', 'hollykunge', '127.0.0.1', '2018-03-19 15:33:06', '1', 'hollykunge', '127.0.0.1', '', '', '');
INSERT INTO `base_org` VALUES (4, '二室', '202', 2, 'b', '', '2018-03-19 15:32:38', '1', 'hollykunge', '127.0.0.1', '2018-03-19 15:33:08', '1', 'hollykunge', '127.0.0.1', '', '', '');
INSERT INTO `base_org` VALUES (6, '五室', '205', 2, 'b', '', '2018-03-19 15:32:40', '1', 'hollykunge', '127.0.0.1', '2018-03-19 15:33:11', '1', 'hollykunge', '127.0.0.1', '', '', '');
INSERT INTO `base_org` VALUES (7, '六室', '206', 2, 'b', '', '2018-03-19 15:32:43', '1', 'hollykunge', '127.0.0.1', '2018-03-19 15:33:14', '1', 'hollykunge', '127.0.0.1', '', '', '');
INSERT INTO `base_org` VALUES (8, '十室', '210', 2, 'b', NULL, '2018-03-19 20:08:33', '6', '姬海南', '127.0.0.1', '2018-03-19 20:08:33', '6', '姬海南', '127.0.0.1', NULL, NULL, NULL);
INSERT INTO `base_org` VALUES (9, '321', '321', 1, 'b', NULL, '2018-03-28 16:19:43', '1', 'hollykunge', '127.0.0.1', '2018-03-28 16:19:43', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL);
INSERT INTO `base_org` VALUES (10, '432', '432', 1, 'b', NULL, '2018-03-28 16:21:59', '1', 'hollykunge', '127.0.0.1', '2018-03-28 16:21:59', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_org_leader
-- ----------------------------
DROP TABLE IF EXISTS `base_org_leader`;
CREATE TABLE `base_org_leader`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(255) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_org_leader
-- ----------------------------
INSERT INTO `base_org_leader` VALUES (3, 2, 7, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_org_leader` VALUES (4, 2, 8, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_org_leader` VALUES (11, 1, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_org_leader` VALUES (12, 1, 7, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_org_member
-- ----------------------------
DROP TABLE IF EXISTS `base_org_member`;
CREATE TABLE `base_org_member`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_org_member
-- ----------------------------
INSERT INTO `base_org_member` VALUES (3, 1, 11, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_org_member` VALUES (4, 1, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_resource_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_authority`;
CREATE TABLE `base_resource_authority`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色ID',
  `authority_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色类型',
  `resource_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源ID',
  `resource_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源类型',
  `parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `path` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1719 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_resource_authority
-- ----------------------------
INSERT INTO `base_resource_authority` VALUES (287, '1', 'group', '5', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (288, '1', 'group', '9', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (289, '1', 'group', '10', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (290, '1', 'group', '11', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (291, '1', 'group', '12', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (294, '1', 'group', '5', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (295, '1', 'group', '9', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (296, '1', 'group', '10', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (297, '1', 'group', '11', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (298, '1', 'group', '12', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (299, '1', 'group', '9', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (300, '1', 'group', '12', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (301, '1', 'group', '10', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (302, '1', 'group', '11', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (303, '1', 'group', '13', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (304, '1', 'group', '14', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (305, '1', 'group', '15', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (306, '1', 'group', '10', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (307, '1', 'group', '11', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (308, '1', 'group', '12', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (309, '1', 'group', '13', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (310, '1', 'group', '14', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (311, '1', 'group', '9', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (312, '1', 'group', '15', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (313, '1', 'group', '16', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (314, '1', 'group', '17', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (315, '1', 'group', '18', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (317, '1', 'group', '20', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (318, '1', 'group', '21', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (319, '1', 'group', '22', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (349, '4', 'group', '9', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (371, '1', 'group', '23', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (372, '1', 'group', '24', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (373, '1', 'group', '27', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (374, '1', 'group', '28', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (375, '1', 'group', '23', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (378, '1', 'group', '5', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (379, '1', 'group', '9', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (380, '1', 'group', '11', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (381, '1', 'group', '14', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (382, '1', 'group', '13', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (383, '1', 'group', '15', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (384, '1', 'group', '12', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (385, '1', 'group', '24', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (386, '1', 'group', '10', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (387, '1', 'group', '27', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (388, '1', 'group', '16', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (389, '1', 'group', '18', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (390, '1', 'group', '17', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (392, '1', 'group', '20', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (393, '1', 'group', '28', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (394, '1', 'group', '22', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (395, '1', 'group', '21', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (396, '4', 'group', '23', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (397, '4', 'group', '9', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (398, '4', 'group', '27', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (399, '4', 'group', '24', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (400, '4', 'group', '28', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (401, '1', 'group', '30', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (402, '1', 'group', '30', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (403, '1', 'group', '31', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (421, '1', 'group', '31', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (422, '1', 'group', '30', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (423, '4', 'group', '31', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (424, '4', 'group', '30', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (436, '1', 'group', '32', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (437, '1', 'group', '33', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (438, '1', 'group', '34', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (439, '1', 'group', '35', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (440, '4', 'group', '32', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (464, '1', 'group', '30', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (465, '1', 'group', '31', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (466, '1', 'group', '30', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (467, '1', 'group', '31', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (468, '1', 'group', '30', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (469, '1', 'group', '31', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (470, '1', 'group', '30', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (471, '1', 'group', '31', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (472, '1', 'group', '40', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (492, '1', 'group', '30', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (493, '1', 'group', '31', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (494, '1', 'group', '40', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (516, '4', 'group', '41', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (517, '4', 'group', '30', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (518, '4', 'group', '31', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (519, '4', 'group', '40', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (611, '4', 'group', '42', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (612, '4', 'group', '36', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (628, '4', 'group', '13', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (629, '4', 'group', '5', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (630, '4', 'group', '1', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (631, '4', 'group', '6', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (632, '4', 'group', '7', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (633, '4', 'group', '8', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (634, '4', 'group', '27', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (635, '4', 'group', '9', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (636, '4', 'group', '24', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (637, '4', 'group', '22', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (638, '4', 'group', '23', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (639, '4', 'group', '25', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (640, '4', 'group', '26', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (641, '4', 'group', '28', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (666, '1', 'group', '41', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (689, '1', 'group', '43', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (691, '1', 'group', '44', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (710, '9', 'group', '42', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (711, '9', 'group', '43', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (712, '9', 'group', '44', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (713, '9', 'group', '45', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (718, '9', 'group', '42', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (719, '9', 'group', '44', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (720, '9', 'group', '45', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (721, '9', 'group', '43', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (722, '1', 'group', '41', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (749, '10', 'group', '13', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (750, '10', 'group', '14', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (751, '10', 'group', '-1', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (752, '10', 'group', '5', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (753, '10', 'group', '6', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (754, '10', 'group', '17', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (755, '10', 'group', '20', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (774, '1', 'group', '3', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (775, '1', 'group', '4', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (812, '1', 'group', '19', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (849, '9', 'group', '1', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (850, '9', 'group', '13', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (851, '9', 'group', '14', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (852, '9', 'group', '-1', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (853, '9', 'group', '5', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (854, '9', 'group', '17', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (855, '9', 'group', '18', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (856, '9', 'group', '20', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (924, '1', 'group', '42', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (945, '1', 'group', '45', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (956, '1', 'group', '46', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1087, '1', 'group', '47', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1088, '1', 'group', '48', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1089, '1', 'group', '49', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1090, '1', 'group', '50', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1091, '1', 'group', '51', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1130, '11', 'group', '1', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1131, '11', 'group', '13', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1132, '11', 'group', '-1', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1133, '11', 'group', '5', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1134, '11', 'group', '27', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1135, '11', 'group', '6', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1136, '11', 'group', '39', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1137, '11', 'group', '7', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1138, '11', 'group', '8', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1139, '11', 'group', '21', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1549, '1', 'group', '56', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1578, '1', 'group', '57', 'button', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1691, '1', 'group', '44', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1692, '1', 'group', '45', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1693, '1', 'group', '46', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1694, '1', 'group', '47', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1695, '1', 'group', '48', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1696, '1', 'group', '27', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1697, '1', 'group', '49', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1698, '1', 'group', '29', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1699, '1', 'group', '50', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1700, '1', 'group', '30', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1701, '1', 'group', '31', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1702, '1', 'group', '32', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1703, '1', 'group', '33', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1704, '1', 'group', '34', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1705, '1', 'group', '13', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1706, '1', 'group', '35', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1707, '1', 'group', '37', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1708, '1', 'group', '-1', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1709, '1', 'group', '38', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1710, '1', 'group', '39', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1711, '1', 'group', '1', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1712, '1', 'group', '5', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1713, '1', 'group', '6', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1714, '1', 'group', '7', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1715, '1', 'group', '8', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1716, '1', 'group', '41', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1717, '1', 'group', '42', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_resource_authority` VALUES (1718, '1', 'group', '21', 'menu', '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_user
-- ----------------------------
DROP TABLE IF EXISTS `base_user`;
CREATE TABLE `base_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `birthday` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mobile_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tel_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `can_admin` blob NULL,
  `can_fork` blob NULL,
  `can_create` blob NULL,
  `attr4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attr8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_user
-- ----------------------------
INSERT INTO `base_user` VALUES (1, 'admin', '$2a$12$S/yLlj9kzi5Dgsz97H4rAekxrPlk/10eXp1lUJcAVAx.2M9tOpWie', 'hollykunge', '', NULL, '', NULL, '', '男', NULL, NULL, '管理员', NULL, NULL, NULL, NULL, '2018-03-15 11:08:29', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (2, 'jihainan', '$2a$12$CqGpCTW190s5fZaG7uyPUezl6RRk4SsITuYQnZlBEc2rTwWQE2S3C', '姬海南', '', '', '', '', '', '男', '', '', '管理员', '2018-03-19 15:35:57', '1', 'hollykunge', '127.0.0.1', '2018-03-19 15:36:18', '1', 'hollykunge', '127.0.0.1', '', '', '', '', '', '', '', '');
INSERT INTO `base_user` VALUES (5, '19881210', '$2a$12$k5RkyE9F0O51u4Ns9y0cZ.nzxBJ2UOdgq8bzk4tc5Fx7tdpmOJtaq', '19881210', '', '', '', '', '', '男', '', '', '', '2018-03-19 15:35:48', '1', 'hollykunge', '127.0.0.1', '2018-03-19 15:36:14', '1', 'hollykunge', '127.0.0.1', '', '', '', '', '', '', '', '');
INSERT INTO `base_user` VALUES (7, 'wangzhunzhong', '$2a$12$b8veMCpETW9VHyZqPxTo7OB3YodgNVzDi31nbvK3FDtm9NwZi3IN.', '王准忠', '', '', '', '', '', '男', '', '', '管理员', '2018-03-19 15:36:00', '6', '姬海南', '127.0.0.1', '2018-03-19 15:36:21', '6', '姬海南', '127.0.0.1', '', '', '', '', '', '', '', '');
INSERT INTO `base_user` VALUES (8, 'jihang', '$2a$12$pihMUzpMVsCblOzUFr50gObNbEZqBdPgBMl.RQoiENF1befeyRlwC', '姬航', '', '', '', '', '', '男', '', '', '管理员', '2018-03-19 15:36:04', '6', '姬海南', '127.0.0.1', '2018-03-19 19:29:05', '1', 'hollykunge', '127.0.0.1', '', '', '', '', '', '', '', '');
INSERT INTO `base_user` VALUES (10, 'test2', '$2a$12$QkOel.D8z6ZpNhY87q/Ctuyb0JXAAT0N5AeTWqbl.yPOfcaPpy/TO', '测试二', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, NULL, '2018-03-19 16:24:29', '1', 'hollykunge', '127.0.0.1', '2018-03-19 16:24:29', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (11, 'test3', '$2a$12$HkUTcpkOQWKufXIVzWXjYe7b8NrgkMk4pQy5BipMfDchyfk4y3EzK', '测试三', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, NULL, '2018-03-19 16:26:30', '1', 'hollykunge', '127.0.0.1', '2018-03-19 16:26:30', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (12, 'test4', '$2a$12$LCPnjCiyhIepoxSalsntbeKffPlgFyMjYLpJ2xOv7lTJm5zLewzNS', '测试四', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, NULL, '2018-03-19 16:26:58', '1', 'hollykunge', '127.0.0.1', '2018-03-19 16:26:58', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (13, 'test5', '$2a$12$MSA7uI7iH0j8eDTKepGr6eBStbGxUZYeoQ5wW2pBqYeS/ObnVZndO', '测试五', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, NULL, '2018-03-19 16:27:17', '1', 'hollykunge', '127.0.0.1', '2018-03-19 16:27:17', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (14, 'test6', '$2a$12$aY0Q10Q.FbreX.EOhmdrSuMZluPyPVlRf3KG8JvDXL0XArCOls7QG', '测试六', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, NULL, '2018-03-19 16:27:44', '1', 'hollykunge', '127.0.0.1', '2018-03-19 16:27:44', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (15, 'test8', '$2a$12$DdUf2jwqYRDgE16vrvP/9uX0/mh9awPDYOqtcJsG0gl6lUgNbKUPi', '测试八', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, NULL, '2018-03-19 17:17:34', '1', 'hollykunge', '127.0.0.1', '2018-03-19 17:17:34', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (16, '321', '$2a$12$clWcykpYL3sN2W8UAxiqFOivhEvvg0/uBTN3pSxcLSKopc2Ri30cW', '321', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, NULL, '2018-03-28 16:18:47', '1', 'hollykunge', '127.0.0.1', '2018-03-28 16:18:47', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (17, 'shejishi1', '$2a$12$rL2DRiEd0JN8tIOOFelPIeEK.ZjWXUO.sLjynB6is/R/t5FDfpwra', '设计师一', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, '123456', '2018-08-01 14:03:26', '1', 'hollykunge', '127.0.0.1', '2018-08-01 14:03:26', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (18, 'shejishi2', '$2a$12$xCk.QvqfmLBv0pFWPmrVCu9Gl8tDEFpjpGmuAUrnf0gQr4r1ULM6W', '设计师二', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, '123', '2018-08-01 14:04:13', '1', 'hollykunge', '127.0.0.1', '2018-08-01 14:04:13', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (19, 'shejishi3', '$2a$12$9f.7FEKBDQnOp.j0cxBQ8.E126Apedb6yVkG.zU2phJjHlDiwp2A.', '设计师三', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, '123', '2018-08-01 14:04:49', '1', 'hollykunge', '127.0.0.1', '2018-08-01 14:04:49', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (20, 'shejishi4', '$2a$12$niMPemBY5Iz/ORpzcCa1Xu1zfZdNRsDhUuE89aWOaE1wfvdSB3IAS', '设计师四', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, '123', '2018-08-01 14:10:26', '1', 'hollykunge', '127.0.0.1', '2018-08-01 14:10:26', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (21, 'shejishi5', '$2a$12$w6qsLgVwLt2z7nh3WTbEFu3bLwfrObVJSoxL7SESja5YqZU5iHLUC', '设计师5', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, '1235', '2018-08-01 15:11:58', '1', 'hollykunge', '127.0.0.1', '2018-08-01 15:11:58', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_user` VALUES (22, '66666', '$2a$12$zeuYJej93ADah5eB1EOUa.I1HuXHIp7bvGcGjQwudCme2gBgokau.', '00099', NULL, NULL, NULL, NULL, NULL, '男', NULL, NULL, NULL, '2018-08-01 15:19:01', '1', 'hollykunge', '127.0.0.1', '2018-08-01 15:19:01', '1', 'hollykunge', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for gate_log
-- ----------------------------
DROP TABLE IF EXISTS `gate_log`;
CREATE TABLE `gate_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `menu` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单',
  `opt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作',
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源路径',
  `crt_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人ID',
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作主机',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 246 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gate_log
-- ----------------------------
INSERT INTO `gate_log` VALUES (50, '角色权限管理', '新增', '/admin/group', '2018-03-19 17:15:23', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (51, '角色权限管理', '新增', '/admin/group', '2018-03-19 17:15:57', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (52, '角色权限管理', '新增', '/admin/group', '2018-03-19 17:15:59', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (53, '角色权限管理', '新增', '/admin/group', '2018-03-19 17:15:59', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (54, '角色权限管理', '新增', '/admin/group', '2018-03-19 17:16:00', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (55, '角色权限管理', '新增', '/admin/group', '2018-03-19 17:16:01', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (56, '角色权限管理', '新增', '/admin/group', '2018-03-19 17:16:02', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (57, '角色权限管理', '新增', '/admin/group', '2018-03-19 17:16:12', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (58, '用户管理', '新增', '/admin/user', '2018-03-19 17:17:32', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (59, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 17:41:33', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (60, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 17:46:10', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (61, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 17:48:19', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (62, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 17:49:11', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (63, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 17:51:36', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (64, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 17:55:16', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (65, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 17:56:53', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (66, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 17:58:26', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (67, '角色权限管理', '编辑', '/admin/group', '2018-03-19 17:59:07', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (68, '角色权限管理', '新增', '/admin/group', '2018-03-19 18:00:36', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (69, '角色权限管理', '删除', '/admin/group', '2018-03-19 18:00:45', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (70, '角色权限管理', '新增', '/admin/group', '2018-03-19 18:01:02', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (71, '角色权限管理', '新增', '/admin/group', '2018-03-19 18:01:28', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (72, '角色权限管理', '新增', '/admin/group', '2018-03-19 18:01:49', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (73, '角色权限管理', '新增', '/admin/group', '2018-03-19 18:02:15', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (74, '角色权限管理', '编辑', '/admin/group', '2018-03-19 18:02:51', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (75, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 18:09:54', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (76, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 19:11:49', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (77, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 19:12:34', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (78, '用户管理', '编辑', '/admin/user', '2018-03-19 19:29:04', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (79, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 19:41:29', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (80, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 19:41:45', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (81, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 19:46:04', '6', '姬海南', '127.0.0.1');
INSERT INTO `gate_log` VALUES (82, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 19:54:20', '6', '姬海南', '127.0.0.1');
INSERT INTO `gate_log` VALUES (83, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 19:54:32', '6', '姬海南', '127.0.0.1');
INSERT INTO `gate_log` VALUES (84, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 19:54:44', '6', '姬海南', '127.0.0.1');
INSERT INTO `gate_log` VALUES (85, '组织部门管理', '编辑组织', '/admin/org', '2018-03-19 20:01:51', '6', '姬海南', '127.0.0.1');
INSERT INTO `gate_log` VALUES (86, '组织部门管理', '新增组织', '/admin/org', '2018-03-19 20:08:33', '6', '姬海南', '127.0.0.1');
INSERT INTO `gate_log` VALUES (87, '组织部门管理', '编辑组织', '/admin/org', '2018-03-20 20:59:39', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (88, '组织部门管理', '编辑组织', '/admin/org', '2018-03-21 08:54:15', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (89, '组织部门管理', '编辑组织', '/admin/org', '2018-03-21 08:54:35', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (90, '菜单管理', '编辑', '/admin/menu', '2018-03-23 18:00:28', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (91, '菜单管理', '编辑', '/admin/menu', '2018-03-23 18:14:02', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (92, '菜单管理', '编辑', '/admin/menu', '2018-03-23 18:16:24', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (93, '菜单管理', '编辑', '/admin/menu', '2018-03-23 18:17:18', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (94, '菜单管理', '新增元素', '/admin/element', '2018-03-23 18:19:54', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (95, '菜单管理', '新增元素', '/admin/element', '2018-03-23 19:26:59', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (96, '菜单管理', '新增元素', '/admin/element', '2018-03-23 19:28:13', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (97, '菜单管理', '新增元素', '/admin/element', '2018-03-23 19:29:47', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (98, '角色权限管理', '新增', '/admin/group', '2018-03-23 19:30:41', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (99, '角色权限管理', '新增', '/admin/group', '2018-03-23 19:30:52', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (100, '角色权限管理', '新增', '/admin/group', '2018-03-23 19:30:53', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (101, '角色权限管理', '新增', '/admin/group', '2018-03-23 19:30:53', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (102, '角色权限管理', '新增', '/admin/group', '2018-03-23 19:30:54', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (103, '角色权限管理', '新增', '/admin/group', '2018-03-23 19:30:55', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (104, '菜单管理', '编辑', '/admin/menu', '2018-03-23 19:50:48', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (105, '全部项目', '编辑项目', '/project/project', '2018-03-26 20:37:26', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (106, '全部项目', '编辑项目', '/project/project', '2018-03-26 20:38:39', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (107, '全部项目', '编辑项目', '/project/project', '2018-03-26 20:38:46', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (108, '全部项目', '编辑项目', '/project/project', '2018-03-26 20:39:03', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (109, '全部项目', '编辑项目', '/project/project', '2018-03-26 20:39:11', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (110, '全部项目', '编辑项目', '/project/project', '2018-03-26 20:39:26', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (111, '全部项目', '新增项目', '/project/project', '2018-03-28 09:57:51', '6', '姬海南', '127.0.0.1');
INSERT INTO `gate_log` VALUES (112, '全部项目', '新增项目', '/project/project', '2018-03-28 09:58:44', '6', '姬海南', '127.0.0.1');
INSERT INTO `gate_log` VALUES (113, '全部项目', '新增项目', '/project/project', '2018-03-28 14:41:05', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (114, '全部项目', '新增项目', '/project/project', '2018-03-28 14:49:31', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (115, '全部项目', '新增项目', '/project/project', '2018-03-28 14:52:44', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (116, '全部项目', '新增项目', '/project/project', '2018-03-28 15:06:23', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (117, '全部项目', '新增项目', '/project/project', '2018-03-28 15:08:54', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (118, '全部项目', '新增项目', '/project/project', '2018-03-28 15:11:09', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (119, '全部项目', '新增项目', '/project/project', '2018-03-28 15:11:23', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (120, '全部项目', '新增项目', '/project/project', '2018-03-28 15:17:04', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (121, '全部项目', '新增项目', '/project/project', '2018-03-28 15:17:21', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (122, '全部项目', '新增项目', '/project/project', '2018-03-28 15:18:30', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (123, '全部项目', '新增项目', '/project/project', '2018-03-28 15:51:52', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (124, '全部项目', '新增项目', '/project/project', '2018-03-28 15:57:08', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (125, '全部项目', '新增项目', '/project/project', '2018-03-28 16:02:08', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (126, '全部项目', '新增项目', '/project/project', '2018-03-28 16:04:21', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (127, '全部项目', '新增项目', '/project/project', '2018-03-28 16:09:46', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (128, '用户管理', '新增', '/admin/user', '2018-03-28 16:18:45', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (129, '组织部门管理', '新增组织', '/admin/org', '2018-03-28 16:19:42', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (130, '组织部门管理', '新增组织', '/admin/org', '2018-03-28 16:21:35', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (131, '组织部门管理', '新增组织', '/admin/org', '2018-03-28 16:22:15', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (132, '全部项目', '新增项目', '/project/project', '2018-03-28 18:04:37', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (133, '全部项目', '新增项目', '/project/project', '2018-03-28 18:06:31', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (134, '全部项目', '新增项目', '/project/project', '2018-03-28 18:10:11', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (135, '全部项目', '新增项目', '/project/project', '2018-03-28 18:11:32', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (136, '全部项目', '新增项目', '/project/project', '2018-03-28 18:11:44', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (137, '全部项目', '新增项目', '/project/project', '2018-03-28 18:11:53', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (138, '全部项目', '新增项目', '/project/project', '2018-03-28 18:12:01', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (139, '全部项目', '新增项目', '/project/project', '2018-03-28 18:12:03', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (140, '全部项目', '新增项目', '/project/project', '2018-03-28 18:12:03', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (141, '全部项目', '新增项目', '/project/project', '2018-03-28 18:12:03', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (142, '全部项目', '新增项目', '/project/project', '2018-03-28 18:12:03', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (143, '全部项目', '新增项目', '/project/project', '2018-03-28 18:12:26', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (144, '全部项目', '新增项目', '/project/project', '2018-03-28 18:12:27', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (145, '全部项目', '新增项目', '/project/project', '2018-03-28 18:12:28', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (146, '全部项目', '新增项目', '/project/project', '2018-03-28 18:12:28', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (147, '全部项目', '新增项目', '/project/project', '2018-03-28 18:12:28', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (148, '全部项目', '新增项目', '/project/project', '2018-03-28 18:12:52', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (149, '全部项目', '新增项目', '/project/project', '2018-03-28 18:13:02', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (150, '全部项目', '新增项目', '/project/project', '2018-03-28 18:13:56', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (151, '全部项目', '新增项目', '/project/project', '2018-03-28 18:14:03', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (152, '全部项目', '新增项目', '/project/project', '2018-03-28 18:14:10', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (153, '全部项目', '新增项目', '/project/project', '2018-03-28 18:14:12', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (154, '全部项目', '新增项目', '/project/project', '2018-03-28 18:14:16', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (155, '全部项目', '新增项目', '/project/project', '2018-03-28 18:14:44', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (156, '全部项目', '新增项目', '/project/project', '2018-03-28 18:15:15', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (157, '菜单管理', '编辑', '/admin/menu', '2018-03-29 15:25:40', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (158, '菜单管理', '编辑', '/admin/menu', '2018-03-29 16:21:27', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (159, '全部项目', '新增项目', '/project/project', '2018-04-02 17:30:59', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (160, '全部项目', '新增项目', '/project/project', '2018-04-02 17:31:20', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (161, '全部项目', '新增项目', '/project/project', '2018-04-02 17:31:42', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (162, '全部项目', '新增项目', '/project/project', '2018-04-02 17:32:21', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (163, '全部项目', '新增项目', '/project/project', '2018-04-02 17:35:17', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (164, '全部项目', '新增项目', '/project/project', '2018-04-02 17:36:19', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (165, '全部项目', '新增项目', '/project/project', '2018-04-02 17:39:15', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (166, '全部项目', '新增项目', '/project/project', '2018-04-02 17:40:04', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (167, '全部项目', '新增项目', '/project/project', '2018-04-02 17:40:38', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (168, '全部项目', '新增项目', '/project/project', '2018-04-02 17:42:20', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (169, '全部项目', '新增项目', '/project/project', '2018-04-02 18:00:46', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (170, '全部项目', '新增项目', '/project/project', '2018-04-02 18:04:58', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (171, '全部项目', '新增项目', '/project/project', '2018-04-02 18:08:25', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (172, '全部项目', '新增项目', '/project/project', '2018-04-02 18:13:10', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (173, '全部项目', '新增项目', '/project/project', '2018-04-02 18:17:54', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (174, '全部项目', '新增项目', '/project/project', '2018-04-02 18:23:56', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (175, '全部项目', '新增项目', '/project/project', '2018-04-02 18:46:30', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (176, '全部项目', '新增项目', '/project/project', '2018-04-02 18:47:42', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (177, '全部项目', '新增项目', '/project/project', '2018-04-11 17:06:05', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (178, '全部项目', '新增项目', '/project/project', '2018-04-11 17:29:49', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (179, '全部项目', '新增项目', '/project/project', '2018-04-11 17:31:52', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (180, '全部项目', '新增项目', '/project/project', '2018-04-11 17:32:09', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (181, '全部项目', '新增项目', '/project/project', '2018-04-11 17:32:20', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (182, '全部项目', '新增项目', '/project/project', '2018-04-11 17:32:42', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (183, '全部项目', '新增项目', '/project/project', '2018-04-11 17:32:56', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (184, '全部项目', '新增项目', '/project/project', '2018-04-11 21:03:10', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (185, '全部项目', '新增项目', '/project/project', '2018-04-11 21:03:23', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (186, '全部项目', '新增项目', '/project/project', '2018-04-11 21:09:08', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (187, '全部项目', '新增项目', '/project/project', '2018-04-11 21:15:15', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (188, '全部项目', '新增项目', '/project/project', '2018-04-12 11:04:48', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (189, '全部项目', '新增项目', '/project/project', '2018-04-12 11:12:23', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (190, '全部项目', '新增项目', '/project/project', '2018-04-12 14:55:15', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (191, '全部项目', '新增项目', '/project/project', '2018-04-12 17:33:52', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (192, '用户管理', '删除', '/admin/user', '2018-04-13 13:39:21', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (193, '全部项目', '新增项目', '/project/project', '2018-04-13 14:18:01', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (194, '角色权限管理', '编辑', '/admin/group', '2018-04-20 14:39:04', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (195, '角色权限管理', '新增', '/admin/group', '2018-04-20 14:40:28', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (196, '角色权限管理', '新增', '/admin/group', '2018-07-22 00:39:27', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (197, '角色权限管理', '新增', '/admin/group', '2018-07-22 01:24:11', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (198, '菜单管理', '删除', '/admin/menu', '2018-07-22 01:25:30', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (199, '菜单管理', '删除', '/admin/menu', '2018-07-22 01:25:39', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (200, '角色权限管理', '新增', '/admin/group', '2018-07-22 01:35:54', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (201, '角色权限管理', '新增', '/admin/group', '2018-07-22 01:36:17', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (202, '菜单管理', '编辑', '/admin/menu', '2018-07-23 10:08:22', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (203, '角色权限管理', '新增', '/admin/group', '2018-07-23 11:37:23', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (204, '角色权限管理', '删除', '/admin/group', '2018-07-23 13:56:44', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (205, '角色权限管理', '新增', '/admin/group', '2018-07-23 14:07:08', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (206, '菜单管理', '编辑', '/admin/menu', '2018-07-23 14:14:25', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (207, '菜单管理', '编辑', '/admin/menu', '2018-07-23 14:15:25', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (208, '菜单管理', '编辑', '/admin/menu', '2018-07-23 14:16:06', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (209, '菜单管理', '编辑', '/admin/menu', '2018-07-23 14:16:13', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (210, '角色权限管理', '新增', '/admin/group', '2018-07-23 14:21:44', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (211, '角色权限管理', '新增', '/admin/group', '2018-07-23 14:23:46', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (212, '角色权限管理', '新增', '/admin/group', '2018-07-23 14:26:23', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (213, '角色权限管理', '新增', '/admin/group', '2018-07-23 15:19:04', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (214, '菜单管理', '编辑', '/admin/menu', '2018-07-25 08:59:44', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (215, '角色权限管理', '新增', '/admin/group', '2018-07-31 17:28:41', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (216, '用户管理', '新增', '/admin/user', '2018-08-01 14:03:25', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (217, '用户管理', '新增', '/admin/user', '2018-08-01 14:04:12', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (218, '用户管理', '新增', '/admin/user', '2018-08-01 14:04:48', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (219, '用户管理', '新增', '/admin/user', '2018-08-01 14:07:42', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (220, '用户管理', '新增', '/admin/user', '2018-08-01 14:10:40', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (221, '用户管理', '新增', '/admin/user', '2018-08-01 15:13:11', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (222, '全部项目', '新增项目', '/project/project', '2018-08-01 15:22:53', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (223, '全部项目', '新增项目', '/project/project', '2018-08-01 15:30:16', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (224, '全部项目', '新增项目', '/project/project', '2018-08-01 15:38:28', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (225, '全部项目', '新增项目', '/project/project', '2018-08-01 15:41:20', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (226, '全部项目', '新增项目', '/project/project', '2018-08-01 15:49:01', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (227, '菜单管理', '编辑', '/admin/menu', '2018-08-01 16:24:32', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (228, '菜单管理', '编辑', '/admin/menu', '2018-08-01 16:24:40', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (229, '菜单管理', '新增元素', '/admin/element', '2018-08-01 16:26:45', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (230, '菜单管理', '编辑元素', '/admin/element', '2018-08-01 16:27:31', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (231, '菜单管理', '新增元素', '/admin/element', '2018-08-01 16:28:37', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (232, '菜单管理', '新增元素', '/admin/element', '2018-08-01 16:29:57', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (233, '菜单管理', '新增元素', '/admin/element', '2018-08-01 16:30:57', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (234, '角色权限', '新增', '/admin/group', '2018-08-01 16:40:02', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (235, '角色权限', '新增', '/admin/group', '2018-08-01 16:40:56', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (236, '角色权限', '新增', '/admin/group', '2018-08-01 16:44:04', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (237, '角色权限', '新增', '/admin/group', '2018-08-01 16:47:14', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (238, '角色权限', '新增', '/admin/group', '2018-08-01 16:47:15', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (239, '角色权限', '新增', '/admin/group', '2018-08-01 16:47:44', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (240, '角色权限', '新增', '/admin/group', '2018-08-01 16:47:47', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (241, '角色权限', '新增', '/admin/group', '2018-08-01 16:48:23', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (242, '角色权限', '新增', '/admin/group', '2018-08-01 16:50:40', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (243, '角色权限', '新增', '/admin/group', '2018-08-01 16:52:35', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (244, '角色权限', '新增', '/admin/group', '2018-08-01 16:53:30', '1', 'hollykunge', '127.0.0.1');
INSERT INTO `gate_log` VALUES (245, '全部任务', '添加任务', '/project/task', '2018-08-01 17:03:32', '1', 'hollykunge', '127.0.0.1');

SET FOREIGN_KEY_CHECKS = 1;
