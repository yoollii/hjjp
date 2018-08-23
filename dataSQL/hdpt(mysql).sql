/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50717
Source Host           : 192.168.1.253:3306
Source Database       : hdpt

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-08-23 16:58:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for applicationsystem（应用系统）
-- ----------------------------
DROP TABLE IF EXISTS `applicationsystem`;
CREATE TABLE `applicationsystem` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `useFlag` int(1) DEFAULT NULL  COMMENT '应用标识',
  `des` varchar(64) DEFAULT NULL  COMMENT ' 描述',
  `baseUrl` varchar(128) DEFAULT NULL COMMENT '基础地址',
  `modelId` varchar(32) DEFAULT NULL COMMENT '对应模型id',
  `state` int(1) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for flowmodel（流程模型）
-- ----------------------------
DROP TABLE IF EXISTS `flowmodel`;
CREATE TABLE `flowmodel` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `state` int(1) DEFAULT NULL,
  `cruser` varchar(32) DEFAULT NULL,
  `des` varchar(1024) DEFAULT NULL,
  `orders` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for powers（权限表）
-- ----------------------------
DROP TABLE IF EXISTS `powers`;
CREATE TABLE `powers` (
  `id` varchar(32) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `code` varchar(32) DEFAULT NULL,
  `weight` varchar(32) DEFAULT NULL,
  `pid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for propertyconfig（属性配置）
-- ----------------------------
DROP TABLE IF EXISTS `propertyconfig`;
CREATE TABLE `propertyconfig` (
  `id` varchar(32) NOT NULL,
  `dataMap` varchar(32) DEFAULT NULL,
  `inConfig` varchar(32) DEFAULT NULL,
  `outConfig` varchar(32) DEFAULT NULL,
  `serId` varchar(32) DEFAULT NULL,
  `modelId` varchar(32) DEFAULT NULL,
  `flowId` varchar(32) DEFAULT NULL  COMMENT '流程id',
  `taskId` varchar(32) DEFAULT NULL   COMMENT 'taskid流程中使用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role（角色）
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `des` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ser（服务）
-- ----------------------------
DROP TABLE IF EXISTS `ser`;
CREATE TABLE `ser` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `state` int(1) DEFAULT NULL,
  `des` varchar(1024) DEFAULT NULL,
  `orders` int(1) DEFAULT NULL,
  `urlFlag` int(1) DEFAULT NULL,
  `url` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `state` int(1) DEFAULT NULL,
  `groupName` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for userrole（用户角色对应关系表）
-- ----------------------------
DROP TABLE IF EXISTS `userrole`;
CREATE TABLE `userrole` (
  `id` varchar(32) NOT NULL,
  `uid` varchar(32) DEFAULT NULL,
  `rid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
