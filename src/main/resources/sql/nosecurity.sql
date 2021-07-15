/*
 Navicat Premium Data Transfer

 Source Server         : dev
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 10.0.81.212:3306
 Source Schema         : spring_security

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 15/07/2021 17:12:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `permission_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限code',
  `permission_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, 'create_user', '创建用户');
INSERT INTO `sys_permission` VALUES (2, 'query_user', '查看用户');
INSERT INTO `sys_permission` VALUES (3, 'delete_user', '删除用户');
INSERT INTO `sys_permission` VALUES (4, 'modify_user', '修改用户');

-- ----------------------------
-- Table structure for sys_request_path
-- ----------------------------
DROP TABLE IF EXISTS `sys_request_path`;
CREATE TABLE `sys_request_path`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `url` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求路径',
  `description` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '路径描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '请求路径' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_request_path
-- ----------------------------
INSERT INTO `sys_request_path` VALUES (1, '/getUser', '查询用户');
INSERT INTO `sys_request_path` VALUES (2, '/file/upload', '文件增强');
INSERT INTO `sys_request_path` VALUES (3, '/file/page', '文件列表');
INSERT INTO `sys_request_path` VALUES (4, 'file/download', '文件下载');

-- ----------------------------
-- Table structure for sys_request_path_permission_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_request_path_permission_relation`;
CREATE TABLE `sys_request_path_permission_relation`  (
  `id` int(11) DEFAULT NULL COMMENT '主键id',
  `url_id` int(11) DEFAULT NULL COMMENT '请求路径id',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '路径权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_request_path_permission_relation
-- ----------------------------
INSERT INTO `sys_request_path_permission_relation` VALUES (1, 1, 2);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `role_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名',
  `role_description` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin', '管理员', '管理员，拥有所有权限');
INSERT INTO `sys_role` VALUES (2, 'user', '普通用户', '普通用户，拥有部分权限');

-- ----------------------------
-- Table structure for sys_role_permission_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission_relation`;
CREATE TABLE `sys_role_permission_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色-权限关联关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission_relation
-- ----------------------------
INSERT INTO `sys_role_permission_relation` VALUES (1, 1, 1);
INSERT INTO `sys_role_permission_relation` VALUES (2, 1, 2);
INSERT INTO `sys_role_permission_relation` VALUES (3, 1, 3);
INSERT INTO `sys_role_permission_relation` VALUES (4, 1, 4);
INSERT INTO `sys_role_permission_relation` VALUES (5, 2, 1);
INSERT INTO `sys_role_permission_relation` VALUES (6, 2, 2);
INSERT INTO `sys_role_permission_relation` VALUES (7, 2, 3);
INSERT INTO `sys_role_permission_relation` VALUES (8, 2, 4);

-- ----------------------------
-- Table structure for sys_school
-- ----------------------------
DROP TABLE IF EXISTS `sys_school`;
CREATE TABLE `sys_school`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `school_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '学校名',
  `account` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联账号',
  `host` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '录取通知书访问路径前缀',
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_school
-- ----------------------------
INSERT INTO `sys_school` VALUES (1, '华南理工大学', 'user2', 'http://view.dev.5gimos.com/colleges-universities/hg', '2021-07-07 17:01:50', '2021-07-07 16:02:05');
INSERT INTO `sys_school` VALUES (7, '中山大学', 'shindo', 'http://view.dev.5gimos.com/colleges-universities/zd', '2021-07-08 16:01:43', '2021-07-08 17:01:34');

-- ----------------------------
-- Table structure for sys_student
-- ----------------------------
DROP TABLE IF EXISTS `sys_student`;
CREATE TABLE `sys_student`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号',
  `stu_uid` bigint(20) DEFAULT NULL,
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `text1` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数1',
  `text2` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数2',
  `text3` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数3',
  `text4` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数4',
  `text5` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `text6` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数6',
  `text7` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数7',
  `text8` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数8',
  `text9` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数9',
  `text10` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数10',
  `status` tinyint(1) DEFAULT NULL COMMENT '是否点击 0：未点击，1：已点击',
  `click_time` datetime(0) DEFAULT NULL COMMENT '第一次访问时间',
  `click_nums` int(3) DEFAULT NULL COMMENT '点击次数',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间，每次点击都更新',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `user_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户密码',
  `last_login_time` datetime(0) DEFAULT NULL COMMENT '上一次登录时间',
  `enabled` tinyint(1) DEFAULT 1 COMMENT '账号是否可用。默认为1（可用）',
  `not_expired` tinyint(1) DEFAULT 1 COMMENT '是否过期。默认为1（没有过期）',
  `account_non_expired` tinyint(1) DEFAULT NULL,
  `account_non_locked` tinyint(1) DEFAULT 1 COMMENT '账号是否锁定。默认为1（没有锁定）',
  `credentials_non_expired` tinyint(1) DEFAULT 1 COMMENT '证书（密码）是否过期。默认为1（没有过期）',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `create_user` int(11) DEFAULT NULL COMMENT '创建人',
  `update_user` int(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '管理员', '6607f51871353c72ad66bfcb21635565', NULL, 1, 1, 1, 1, 1, NULL, NULL, 0, 0);
INSERT INTO `sys_user` VALUES (2, 'user2', '用户2', '1c1f4f0008c032a01dbb17bddaca9b7a', '2021-07-07 11:27:43', 1, 1, 1, 1, 1, '2019-08-29 06:29:24', '2021-07-07 11:27:43', 1, 2);
INSERT INTO `sys_user` VALUES (3, 'user1', '用户1', '1c1f4f0008c032a01dbb17bddaca9b7a', '2019-09-04 20:25:36', 1, 1, 1, 1, 1, '2019-08-29 06:28:36', '2019-09-04 20:25:36', 1, 1);
INSERT INTO `sys_user` VALUES (11, 'shindo', '进藤', '1c1f4f0008c032a01dbb17bddaca9b7a', NULL, 1, 1, 1, 1, 1, '2021-07-08 15:55:58', NULL, 1, NULL);

-- ----------------------------
-- Table structure for sys_user_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_file`;
CREATE TABLE `sys_user_file`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账号',
  `file_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `file_url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1412347814498811910 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role_relation`;
CREATE TABLE `sys_user_role_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role_relation
-- ----------------------------
INSERT INTO `sys_user_role_relation` VALUES (1, 1, 1);
INSERT INTO `sys_user_role_relation` VALUES (2, 2, 2);

-- ----------------------------
-- Table structure for sys_user_school_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_school_relation`;
CREATE TABLE `sys_user_school_relation`  (
  `id` int(11) NOT NULL COMMENT '主键id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `school_id` int(11) DEFAULT NULL COMMENT '学校id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_school_relation
-- ----------------------------
INSERT INTO `sys_user_school_relation` VALUES (1, 2, 1);
INSERT INTO `sys_user_school_relation` VALUES (2, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
