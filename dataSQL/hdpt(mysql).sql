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
-- Table structure for applicationsystem��Ӧ��ϵͳ��
-- ----------------------------
DROP TABLE IF EXISTS `applicationsystem`;
CREATE TABLE `applicationsystem` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `useFlag` int(1) DEFAULT NULL  COMMENT 'Ӧ�ñ�ʶ',
  `des` varchar(64) DEFAULT NULL  COMMENT ' ����',
  `baseUrl` varchar(128) DEFAULT NULL COMMENT '������ַ',
  `modelId` varchar(32) DEFAULT NULL COMMENT '��Ӧģ��id',
  `state` int(1) DEFAULT NULL COMMENT '״̬',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for flowmodel������ģ�ͣ�
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
-- Table structure for powers��Ȩ�ޱ�
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
-- Table structure for propertyconfig���������ã�
-- ----------------------------
DROP TABLE IF EXISTS `propertyconfig`;
CREATE TABLE `propertyconfig` (
  `id` varchar(32) NOT NULL,
  `dataMap` varchar(32) DEFAULT NULL,
  `inConfig` varchar(32) DEFAULT NULL,
  `outConfig` varchar(32) DEFAULT NULL,
  `serId` varchar(32) DEFAULT NULL,
  `modelId` varchar(32) DEFAULT NULL,
  `flowId` varchar(32) DEFAULT NULL  COMMENT '����id',
  `taskId` varchar(32) DEFAULT NULL   COMMENT 'taskid������ʹ��',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role����ɫ��
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `des` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ser������
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
-- Table structure for userrole���û���ɫ��Ӧ��ϵ��
-- ----------------------------
DROP TABLE IF EXISTS `userrole`;
CREATE TABLE `userrole` (
  `id` varchar(32) NOT NULL,
  `uid` varchar(32) DEFAULT NULL,
  `rid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
