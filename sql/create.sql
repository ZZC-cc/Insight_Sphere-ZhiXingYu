/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80200 (8.2.0)
 Source Host           : localhost:3306
 Source Schema         : tb_financial_ms

 Target Server Type    : MySQL
 Target Server Version : 80200 (8.2.0)
 File Encoding         : 65001

 Date: 10/03/2024 14:08:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `user_id` bigint NOT NULL COMMENT '评论人id',
  `user_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论人姓名',
  `post_id` bigint NOT NULL COMMENT '文章id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父评论id',
  `root_parent_id` bigint NULL DEFAULT NULL COMMENT '根评论id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1761634969385721859 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评论' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1760560495152353281, '第一条评论', 1754526766348963841, 'zzc', 1760192904428855293, 0, 0, '2024-02-22 15:01:46', '2024-02-25 14:07:01', 1);
INSERT INTO `comment` VALUES (1760564296387178498, '第二楼评论', 1754526766348963841, 'zzc', 1760192904428855293, 1760560495152353281, 1760560495152353281, '2024-02-22 15:16:52', '2024-02-25 14:07:03', 1);
INSERT INTO `comment` VALUES (1760570562367000578, '第三楼评论', 1754526766348963841, 'zzc', 1760192904428855293, 1760564296387178498, 1760560495152353281, '2024-02-22 15:41:46', '2024-02-25 14:07:04', 1);
INSERT INTO `comment` VALUES (1760700977903878146, '第四楼评论', 1755571113714577409, 'test', 1760192904428855293, 1760570562367000578, 1760560495152353281, '2024-02-23 00:20:00', '2024-02-25 14:07:06', 1);
INSERT INTO `comment` VALUES (1760701774767112194, '好文！！', 1754526766348963841, 'zzc', 1760192904428855298, 0, 0, '2024-02-23 00:23:10', '2024-02-23 00:23:10', 0);
INSERT INTO `comment` VALUES (1760725216954466306, '111', 1754526766348963841, 'zzc', 1760192904428855293, 0, 0, '2024-02-23 01:56:19', '2024-02-23 01:57:50', 1);
INSERT INTO `comment` VALUES (1760725693137993730, '111', 1754526766348963841, 'zzc', 1760192904428855293, 0, 0, '2024-02-23 01:58:12', '2024-02-23 18:59:01', 1);
INSERT INTO `comment` VALUES (1760947649565179905, '1111', 1754526766348963841, 'zzc', 1760192904428855298, 1760701774767112194, 0, '2024-02-23 16:40:11', '2024-02-23 17:50:26', 1);
INSERT INTO `comment` VALUES (1760947716720181250, '写得太好了！', 1754526766348963841, 'zzc', 1760192904428855298, 0, 0, '2024-02-23 16:40:27', '2024-02-23 16:40:27', 0);
INSERT INTO `comment` VALUES (1760969152964870145, '222', 1754526766348963841, 'zzc', 1760192904428855293, 1760725693137993730, 0, '2024-02-23 18:05:37', '2024-02-23 18:58:58', 1);
INSERT INTO `comment` VALUES (1760982671974264833, '写得很好，孩子很喜欢', 1754526766348963841, 'zzc', 1760192904428855298, 0, 0, '2024-02-23 18:59:21', '2024-02-23 18:59:21', 0);
INSERT INTO `comment` VALUES (1760983575259574274, '111', 1754526766348963841, 'zzc', 1760192904428855298, 0, 0, '2024-02-23 19:02:56', '2024-02-23 19:05:08', 1);
INSERT INTO `comment` VALUES (1760983838246629378, '222', 1754526766348963841, 'zzc', 1760192904428855298, 0, 0, '2024-02-23 19:03:59', '2024-02-23 19:05:05', 1);
INSERT INTO `comment` VALUES (1760984086641700866, '333', 1754526766348963841, 'zzc', 1760192904428855298, 0, 0, '2024-02-23 19:04:58', '2024-02-23 19:05:03', 1);
INSERT INTO `comment` VALUES (1760984278354948098, '666', 1754526766348963841, 'zzc', 1760192904428855298, 1760982671974264833, 0, '2024-02-23 19:05:44', '2024-02-23 19:05:44', 0);
INSERT INTO `comment` VALUES (1760984802416455682, '66666', 1754526766348963841, 'zzc', 1760192904428855298, 1760984278354948098, 0, '2024-02-23 19:07:49', '2024-02-23 19:07:54', 1);
INSERT INTO `comment` VALUES (1761468706722177025, 'git差点把我代码搞没了', 1754526766348963841, 'zzc', 1761463012149211138, 0, 0, '2024-02-25 03:10:40', '2024-02-25 03:10:40', 0);
INSERT INTO `comment` VALUES (1761468749541826561, '我真的服了', 1754526766348963841, 'zzc', 1761463012149211138, 1761468706722177025, 0, '2024-02-25 03:10:51', '2024-02-25 03:10:51', 0);
INSERT INTO `comment` VALUES (1761634969385721858, '111', 1754526766348963841, 'zzc', 1761634940675710978, 0, 0, '2024-02-25 14:11:20', '2024-02-25 14:11:29', 1);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `notice_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公告内容',
  `sort` int NOT NULL COMMENT '排序',
  `status` int NOT NULL COMMENT '公告状态#1：正常，2：禁用',
  `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, 'QuickInit Pro前端初始化模板 v1.0 发布', '首页\n文章列表\n管理页面：用户管理+文章管理+评论管理+通知管理\n个人页面：个人设置+个人中心\n接口文档', 1, 1, '2024-02-13 17:08:51', '2024-02-26 04:46:17', 'zzc', 'zzc', '2024-02-12 04:46:17', '2024-02-23 02:39:18', 0);
INSERT INTO `notice` VALUES (2, '测试', '111', 999, 2, '2024-02-13 17:13:57', '2024-02-27 17:13:57', 'zzc', 'zzc', '2024-02-13 17:14:04', '2024-02-13 17:58:26', 0);

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人电话',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货详细地址',
  `totalPrice` double NOT NULL COMMENT '总价',
  `count` int NOT NULL COMMENT '总数',
  `payMethod` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付方式',
  `status` int NOT NULL DEFAULT 0 COMMENT '状态：0-未支付，1-已支付，2-已取消，3-已关闭，4-已完成',
  `orderTime` datetime NOT NULL COMMENT '下单时间',
  `payTime` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `userId` bigint NOT NULL COMMENT '用户Id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1763111838375256067 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (1762777748652544001, 'zzc', '13531111111', '广东省深圳市宝安区宝安大道233号', 41495, 5, '微信支付', 1, '2024-02-28 17:52:21', '2024-02-28 17:52:22', 1754526766348963841, '2024-02-28 17:52:20', '2024-02-28 17:52:20', 0);
INSERT INTO `order` VALUES (1762778463366139905, '111', '13531111222', '广东省汕头市澄海区', 8299, 1, '微信支付', 1, '2024-02-28 17:55:11', '2024-02-28 17:55:15', 1754526766348963841, '2024-02-28 17:55:11', '2024-02-28 17:55:11', 0);
INSERT INTO `order` VALUES (1762781329954717698, 'zzc', '13531111111', '广东省深圳市宝安区宝安大道233号', 8299, 1, '支付宝支付', 2, '2024-02-28 18:06:35', NULL, 1754526766348963841, '2024-02-28 18:06:34', '2024-02-28 18:06:34', 0);
INSERT INTO `order` VALUES (1763108053636653058, 'zzc', '13531111111', '广东省深圳市宝安区宝安大道233号', 8299, 1, '微信支付', 1, '2024-02-29 15:44:52', '2024-02-29 15:44:53', 1754526766348963841, '2024-02-29 15:44:51', '2024-02-29 15:44:51', 0);
INSERT INTO `order` VALUES (1763110525209976833, 'zzc', '13531111111', '广东省深圳市宝安区宝安大道233号', 33196, 4, '微信支付', 1, '2024-02-29 15:54:41', '2024-02-29 15:54:45', 1754526766348963841, '2024-02-29 15:54:40', '2024-02-29 15:54:40', 0);
INSERT INTO `order` VALUES (1763110961891549186, 'zzc', '13531111111', '广东省深圳市宝安区宝安大道233号', 33196, 4, '微信支付', 1, '2024-02-29 15:56:25', '2024-02-29 15:56:27', 1754526766348963841, '2024-02-29 15:56:25', '2024-02-29 15:56:25', 0);
INSERT INTO `order` VALUES (1763111211200978946, 'zzc', '13531111111', '广东省深圳市宝安区宝安大道233号', 8299, 1, '微信支付', 1, '2024-02-29 15:57:24', '2024-02-29 15:57:26', 1754526766348963841, '2024-02-29 15:57:24', '2024-02-29 15:57:24', 0);
INSERT INTO `order` VALUES (1763111838375256066, 'zzc', '13531111111', '广东省深圳市宝安区宝安大道233号', 8299, 1, '微信支付', 1, '2024-02-29 15:59:54', '2024-02-29 15:59:56', 1754526766348963841, '2024-02-29 15:59:53', '2024-02-29 15:59:53', 0);

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单详情ID',
  `orderId` bigint NOT NULL COMMENT '订单ID',
  `productId` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL COMMENT '商品数量',
  `price` double NOT NULL COMMENT '商品单价',
  `total` bigint NOT NULL COMMENT '小计金额',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_order_detail_order_id`(`orderId` ASC) USING BTREE,
  CONSTRAINT `fk_order_detail_order_id` FOREIGN KEY (`orderId`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1763111838375256068 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_detail
-- ----------------------------
INSERT INTO `order_detail` VALUES (1762777748719652866, 1762777748652544001, 1762763565068206082, 5, 8299, 8299);
INSERT INTO `order_detail` VALUES (1762778463433248770, 1762778463366139905, 1762763565068206082, 1, 8299, 8299);
INSERT INTO `order_detail` VALUES (1762781330084741121, 1762781329954717698, 1762763565068206082, 1, 8299, 8299);
INSERT INTO `order_detail` VALUES (1763108053858951170, 1763108053636653058, 1762763565068206082, 1, 8299, 8299);
INSERT INTO `order_detail` VALUES (1763110525272891393, 1763110525209976833, 1762763565068206082, 4, 8299, 8299);
INSERT INTO `order_detail` VALUES (1763110961958658050, 1763110961891549186, 1762763565068206082, 4, 8299, 8299);
INSERT INTO `order_detail` VALUES (1763111211200978947, 1763111211200978946, 1762763565068206082, 1, 8299, 8299);
INSERT INTO `order_detail` VALUES (1763111838375256067, 1763111838375256066, 1762763565068206082, 1, 8299, 8299);

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `tags` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签列表（json 数组）',
  `thumbNum` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `favourNum` int NOT NULL DEFAULT 0 COMMENT '收藏数',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userId`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1761634940675710979 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '帖子' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1760192904428855293, '测试', '3333', '[\"文章\",\"测试\"]', 0, 0, 1755571113714577409, '2024-02-21 14:41:05', '2024-03-09 13:49:45', 0);
INSERT INTO `post` VALUES (1760192904428855298, '优雅地接口调优之批量处理优化', '必要性\r\n进行批量处理优化是接口调优的一项重要策略，它可以带来多方面的性能优势。\r\n以下是一些批量处理优化的主要原因：\r\n\r\n减少数据库访问次数： 批量处理将多个操作合并成一个批次执行，从而减少了与数据库的交互次数。数据库访问通常是接口调优中的瓶颈之一，通过批量处理可以显著降低数据库连接和关闭的开销，提高性能。\r\n\r\n降低网络开销： 数据库操作涉及网络通信，而网络通信的开销是相对较高的。通过批量处理，可以在一次通信中传递多个操作，减少了网络开销，特别是在高延迟的网络环境中更为明显。\r\n\r\n提高事务效率： 在一些需要事务的场景下，批量处理可以减少事务的开始和提交次数，从而提高整体事务的效率。减少事务的嵌套和开销对于数据库性能和并发控制有积极影响。\r\n\r\n降低资源消耗： 单独执行大量小规模的操作可能会导致过多的资源消耗，如数据库连接池中的连接数。批量处理可以减少这种资源的浪费，提高系统的稳定性。\r\n\r\n优化内存使用： 批量处理可以更好地利用内存，减少了单个操作导致的内存分配和回收开销。这对于处理大数据量时尤为重要，可以降低内存压力。\r\n\r\n提高并发度： 批量处理可以在一定程度上提高并发度。在一次批量操作中，可以并发处理多个批次，从而加速整体处理速度，特别是在多线程或异步操作的情况下。\r\n\r\n降低锁的竞争： 在涉及事务的情况下，批量处理可以降低对数据库表的锁的竞争。减少锁竞争可以提高系统的并发性，减少潜在的死锁和性能问题。\r\n\r\n提高整体性能： 通过以上方式，批量处理可以有效地提高整体接口的性能，降低系统的响应时间，提升用户体验。\r\n\r\n批量处理优化是一种综合性的性能提升手段，适用于处理大量数据的场景。在设计和实现接口时，考虑采用批量处理的策略，可以有效地降低系统的负载，提高性能，同时减少资源的消耗。', '[\"文章\",\"热门\",\"很开门\"]', 999, 99, 1754526766348963841, '2024-02-21 14:41:05', '2024-02-22 03:19:10', 0);
INSERT INTO `post` VALUES (1760351227505602562, '从聚合搜索项目视角入门Elasticsearch（搜索引擎、对比MongoDB、聚合搜索流程具体实现…）', '一、Elastic Stack 这一套技术栈包括的内容\n官网：https://www.elastic.co/cn/ 包含了数据的整合 => 提取 => 存储 => 使用，一整套。\n\nElastic Stack 是一个开源的数据分析平台，主要用于实时搜索、分析和可视化大规模的结构化和非结构化数据。它包含以下主要组件：\n\nElasticsearch：这是 Elastic Stack 的核心组件，一个分布式的实时搜索和分析引擎。Elasticsearch 负责存储、搜索和分析数据，并提供 RESTful API 以进行数据索引和查询。\nLogstash：Logstash 是一个用于数据收集、转换和发送的数据处理管道工具。它可以从多种来源（如日志文件、消息队列、数据库等）收集数据，然后进行过滤、转换和标准化，最后将数据发送到 Elasticsearch 等目标存储或分析系统。\nKibana：Kibana 是 Elastic Stack 的可视化平台，用于分析和可视化 Elasticsearch 中的数据。它提供了丰富的图表、图形和仪表板，用户可以通过 Kibana 轻松地创建定制化的数据可视化和仪表板，并进行数据分析和探索。\nBeats：Beats 是一组轻量级的数据收集器，用于收集各种类型的操作数据，并将其发送到 Elasticsearch 或 Logstash 进行处理和存储。Beats 包括多个模块，如 Filebeat 用于收集日志文件、Metricbeat 用于收集系统和服务指标、Packetbeat 用于网络数据分析等。\n这些组件共同构成了 Elastic Stack，使用户能够以高效、灵活和可扩展的方式收集、存储、搜索、分析和可视化数据。\n\n二、做聚合搜索时，可以通过ES实现哪些实用功能？\n1. 通过text、keyword字段分别实现模糊查询和精确查询\n当在 Elasticsearch 中设置索引的映射（mapping）时，如果字段的类型被定义为 text，Elasticsearch 会默认将这些字段纳入倒排索引中。\n\n**倒排索引（Inverted Index）**是 Elasticsearch 用于实现全文搜索功能的核心数据结构之一。它将每个文档中的所有单词（或者称为词条、术语）映射到包含该单词的文档列表上。这样的索引结构使得 Elasticsearch 能够快速地进行文本搜索。\n\n对于 text 类型的字段，Elasticsearch 会将文本数据分析成单词，然后将这些单词存储在倒排索引中。这样做的好处是，用户可以通过搜索查询中的单词来查找包含这些单词的文档，而不必考虑单词的顺序或大小写等问题。\n\n需要注意的是，对于需要进行精确匹配或排序的字段，通常会将字段类型定义为 keyword 类型，而不是 text 类型。keyword 类型的字段不会被纳入倒排索引中，而是以精确值进行索引，适用于需要进行精确匹配或聚合操作的场景。\n\n其中，关于倒排索引：\n\n当你在搜索引擎中进行一个查询时，比如输入了一个关键词，搜索引擎会迅速地找到所有包含这个关键词的文档。这样的速度是怎么实现的呢？这就是倒排索引发挥作用的地方。\n\n假设有一个包含了大量文档的数据库，每个文档都有一些关键词。倒排索引是一种将这些关键词映射到文档的数据结构，它的原理如下：\n\n收集关键词：首先，对于每个文档，搜索引擎会分析其内容，将关键词提取出来。这可能包括分词（将文本分成一个个单词）、去除停用词（例如 \"the\", \"a\", \"an\" 等）和其他文本预处理操作。\n建立映射：然后，对于每个关键词，搜索引擎会建立一个映射，将该关键词与包含该关键词的文档列表关联起来。这个过程叫做建立倒排索引。\n搜索过程：当你输入一个查询时，搜索引擎会立即查找包含这个查询关键词的文档列表。这个过程非常迅速，因为搜索引擎只需简单地查找这个关键词在倒排索引中的条目，然后返回与之相关联的文档列表。\n倒排索引的优点在于它的查询速度非常快，因为它不需要遍历整个文档集合，而只需查找倒排索引中的条目。此外，倒排索引还支持高级搜索功能，比如短语匹配、布尔搜索等。\n\n举个例子，假设有一个包含了多篇文章的数据库。当你搜索 \"Elasticsearch\" 时，倒排索引可以立即告诉搜索引擎哪些文章包含了 \"Elasticsearch\"，而不需要搜索引擎逐篇逐句地查找。这样就能极大地加速搜索过程。\n\n同时，倒排索引是一个需要维护的数据结构。在搜索引擎中，当有新的文档被索引、已有文档被修改或删除时，都会触发倒排索引的更新操作。\n\n具体来说，当有新的文档被索引时，搜索引擎会分析文档内容并提取关键词，然后将这些关键词与文档的标识（如文档 ID）建立映射关系，并将这些映射关系添加到倒排索引中。这样就能在搜索时快速找到包含这些关键词的文档。\n\n当已有文档被修改或删除时，搜索引擎也会相应地更新倒排索引。如果修改操作导致文档内容发生变化，那么搜索引擎需要重新分析文档内容，并更新倒排索引中与该文档相关的条目。如果文档被删除，搜索引擎则需要从倒排索引中移除与该文档相关的条目。\n\n倒排索引的维护是搜索引擎索引更新的关键部分，它确保了搜索引擎能够及时准确地响应用户的搜索请求，并保持搜索性能的稳定性。虽然倒排索引的维护会带来一定的计算和存储开销，但这是搜索引擎能够高效工作的基础。\n\n在 Elasticsearch 中，每个索引都有自己的倒排索引，因此不同的索引中的同名字段会分别构建不同的倒排索引。所以，如果你有两个不同的索引，它们都包含了同名的 text 类型字段，那么每个索引会分别构建自己的倒排索引表。\n\n当用户按照这个字段进行查询时，Elasticsearch 会在每个索引的倒排索引表中进行搜索，并返回匹配的结果。换句话说，两个不同的索引中的同名字段数据会分别被检索，并且用户会得到包含了这两个索引中相关数据的搜索结果。\n\n如果你希望将两个不同索引中同名字段的数据合并在一起进行搜索，你可以使用 Elasticsearch 提供的多索引搜索功能，或者在查询时指定多个索引。这样 Elasticsearch 就会在多个索引的倒排索引表中进行搜索，并将结果合并返回给用户。\n\n在收集关键词阶段，搜索引擎可以使用分词器来对文档内容进行分词处理，以提取出关键词。\n\n对于中文文档而言，常用的分词器包括 IK Analyzer、Jieba 分词器等。这些分词器能够根据中文语言的特点将文本分成一个个单词或词语，并且会考虑到词义、停用词等因素，以提高搜索的准确性和效率。\n\n例如，IK Analyzer 是一个常用的中文分词器，它能够根据中文语法规则将文本分成有意义的词语，同时过滤掉停用词（如 \"的\"、\"了\"、\"是\" 等），从而提高搜索的精确度。\n\n因此，在处理中文文档时，使用合适的分词器对文本进行分词处理是非常重要的，能够有效地提取出关键词，为搜索引擎的后续操作提供准确的数据。', '[\"文章\",\"ES\"]', 10, 0, 1754526766348963841, '2024-02-22 01:10:13', '2024-02-22 02:08:36', 0);
INSERT INTO `post` VALUES (1761463012149211138, 'Git & GitHub 学习路线', '介绍\nGit / GitHub 是什么？\nGit 是目前最主流的 分布式版本控制系统 （Version Control System），是团队协作开发不可或缺的工具。它可以保存和管理文件的所有更新记录、并且使用 版本号 进行区分。从而支持将编辑后的文档恢复到修改前的状态（历史版本）、对比不同版本的文件差异、防止旧版本覆盖新版本等功能。\n\nGtiHub 是目前最主流的、免费的 代码开源托管平台 ，可以理解为一个存储和管理代码的 “网盘”。所有用户或团队都可以把自己的代码上传到 GitHub 进行共享和维护、从 GitHub 下载代码等。开发者们可以从 GitHub 上白嫖代码，进行学习或引用。\n\n很多同学刚开始容易把这两个概念搞混，其实很好区分，Git 和 GitHub 的区别和关系如下：\n\nGit 是工具，而 GitHub 是平台。\n我们可以用 Git 之外的版本控制系统（比如 SVN）来向 GitHub 提交代码；同样可以用 Git 将代码提交到 GitHub 之外的其他代码托管平台（比如企业级代码托管平台 GitLab）上。\nSVN 和 Git 是同类的工具，两者的用法很相似。但目前 Git 相对更主流一些，因此只学习 Git 足够了。\n\n为什么要用 Git / GitHub？\nGit\nGit 起初是由 Linux 的创始人 Linus Torvalds 为了更好地管理 Linux 内核开发而创立的。Linux 的内核开发极其复杂，我们可以试想一下，如果有 100 个人要共同来开发这样一个大型项目、维护同一个代码目录和文件，怎么样才能够保证大家的代码不重复、不冲突、即时共享呢？难道要把代码放到 U 盘里，每个人写完代码后再拷贝到别人电脑上么？\n\n肯定不行对吧，这样做不仅麻烦，而且只能人为去解决冲突，可能会出现下面的对话：\n\nA：“哎，这行我改了，你别改！”\n\nB：“TNND，你改了也不跟我说一声。”\n\n所以 Git 的出现正是为了解决上述难题。\n\n使用 Git 工具，大家可以很方便地拉取和提交代码、及时得到代码的修改信息、对比新老代码之间的差异等，让系统代替人工来自动检查和解决冲突。从而提高团队协作开发的效率，保证代码和项目的稳定推进。\n\n除了团队开发外，我们自己做项目时也可以用 Git 来管理代码，如果一不小心改错或者删除了文件，可以快速还原。\n\n因此 Git 也是目前开发岗同学 必学 的技能，企业开发的基本功。\n\nGitHub\n既然要团队协作开发，那么大家肯定要 共享代码 ，在同一套代码的基础上进行开发。所以需要一个集中存储和管理代码的地方，所有团队成员都能访问到。而且不同于网盘，最好大家能使用 Git 工具来操作代码的上传和拉取，并且在平台上方便地浏览和对比代码。\n\n于是，有能力的公司开始自己建设公司内部的代码托管平台。但是对大多数小公司来说，他们没有这种条件自建，只能依赖其他大公司的代码托管服务。\n\n此外，在 Linux 之父以及很多优秀程序员的影响下，越来越多的程序员倡导 开源 ，认为软件开发应当是开放的，所有开发者都可以加入贡献。而开源的背后，必须要有一个开放共享的代码托管平台来支撑，而不是每个公司的代码都被封闭在自己内部的平台上。\n\n于是，以 GitHub 为代表的代码托管平台出现了。团队可以利用 GitHub 进行协作开发；开发者可以轻松阅读并参与到世界知名的开源项目中；我们每位同学也都可以免费使用 GitHub 上的代码，站在巨人的肩膀上打造自己的项目，或者通过参与开源来提升个人影响力等。\n\n此外，如今的 GitHub 还提供了更多强大的功能，比如问题讨论、在线编程、托管网站、自动构建项目等，对开发者非常有帮助。除了代码外，鱼皮也经常把一些需要团队内共享的文档上传到 GitHub 私仓和其他成员协作，免费无限制谁不爱呢？\n\n当然，GitHub 只是代码托管平台中最有代表性的一个，其他知名的代码开源托管平台有国内的 Gitee、国外的 Bitbucket 等；面向企业的私有代码托管平台有 GitLab、腾讯工蜂等。它们没有什么本质的区别，都是平台而已，会用 GitHub，就会用其他的。\n\n综上，强烈建议大家养成使用 GitHub 的习惯，感受开源的魅力。\n\n学习条件\n学过至少一门编程语言后就可以学习 Git 了\n准备开发个人完整项目前建议学习。注意是完整项目，还停留在初学编程语言写练习阶段的同学先不用学，抓紧熟悉编程语言就好。\n正式找工作前必须学习！\n学习建议\nGit 只是工具，学 Git 的时候可以轻松一些，千万不要去背命令！初学者只要先了解工作中常用命令的作用、提交代码的流程，然后跟着教程实操一遍，有个大致的印象就够了。忘了的话就掏出说明书（百度 / 文档等），随用随查即可。因此建议将专门学习 Git 的时间控制在 1 天内，一般几个小时就足够了，最快的话只用十几分钟也能上手。\n虽然现在有很多傻瓜式可视化 Git 工具，但还是建议初学 Git 的同学多自己手敲 Git 命令，可以多把自己平时写的练习代码用 Git 上传到 GitHub 上管理，用的多了自然就熟悉了。\nGit 的学习不是一蹴而就的。随着你使用 Git 管理的项目越来越多、工作经验越来越丰富，你对 Git 和 GitHub 的熟练程度才会越来越高。总之，想学好 Git，实践是关键。\n学习路线\n⭐️ - 重点知识 / 强烈推荐\n\n😐 - 了解即可', '[\"Git\",\"GitHub\",\"文章\"]', 0, 0, 1756735712212049922, '2024-02-25 02:48:03', '2024-02-25 02:48:03', 0);
INSERT INTO `post` VALUES (1761464524250308610, '11', '111', '[]', 0, 0, 1756735712212049922, '2024-02-25 02:54:03', '2024-02-25 02:59:04', 1);
INSERT INTO `post` VALUES (1761464736553394178, '1111', '介绍\nGit / GitHub 是什么？\nGit 是目前最主流的 分布式版本控制系统 （Version Control System），是团队协作开发不可或缺的工具。它可以保存和管理文件的所有更新记录、并且使用 版本号 进行区分。从而支持将编辑后的文档恢复到修改前的状态（历史版本）、对比不同版本的文件差异、防止旧版本覆盖新版本等功能。\n\nGtiHub 是目前最主流的、免费的 代码开源托管平台 ，可以理解为一个存储和管理代码的 “网盘”。所有用户或团队都可以把自己的代码上传到 GitHub 进行共享和维护、从 GitHub 下载代码等。开发者们可以从 GitHub 上白嫖代码，进行学习或引用。\n\n很多同学刚开始容易把这两个概念搞混，其实很好区分，Git 和 GitHub 的区别和关系如下：\n\nGit 是工具，而 GitHub 是平台。\n我们可以用 Git 之外的版本控制系统（比如 SVN）来向 GitHub 提交代码；同样可以用 Git 将代码提交到 GitHub 之外的其他代码托管平台（比如企业级代码托管平台 GitLab）上。\nSVN 和 Git 是同类的工具，两者的用法很相似。但目前 Git 相对更主流一些，因此只学习 Git 足够了。\n\n为什么要用 Git / GitHub？\nGit\nGit 起初是由 Linux 的创始人 Linus Torvalds 为了更好地管理 Linux 内核开发而创立的。Linux 的内核开发极其复杂，我们可以试想一下，如果有 100 个人要共同来开发这样一个大型项目、维护同一个代码目录和文件，怎么样才能够保证大家的代码不重复、不冲突、即时共享呢？难道要把代码放到 U 盘里，每个人写完代码后再拷贝到别人电脑上么？\n\n肯定不行对吧，这样做不仅麻烦，而且只能人为去解决冲突，可能会出现下面的对话：\n\nA：“哎，这行我改了，你别改！”\n\nB：“TNND，你改了也不跟我说一声。”\n\n所以 Git 的出现正是为了解决上述难题。\n\n使用 Git 工具，大家可以很方便地拉取和提交代码、及时得到代码的修改信息、对比新老代码之间的差异等，让系统代替人工来自动检查和解决冲突。从而提高团队协作开发的效率，保证代码和项目的稳定推进。\n\n除了团队开发外，我们自己做项目时也可以用 Git 来管理代码，如果一不小心改错或者删除了文件，可以快速还原。\n\n因此 Git 也是目前开发岗同学 必学 的技能，企业开发的基本功。\n\nGitHub\n既然要团队协作开发，那么大家肯定要 共享代码 ，在同一套代码的基础上进行开发。所以需要一个集中存储和管理代码的地方，所有团队成员都能访问到。而且不同于网盘，最好大家能使用 Git 工具来操作代码的上传和拉取，并且在平台上方便地浏览和对比代码。\n\n于是，有能力的公司开始自己建设公司内部的代码托管平台。但是对大多数小公司来说，他们没有这种条件自建，只能依赖其他大公司的代码托管服务。\n\n此外，在 Linux 之父以及很多优秀程序员的影响下，越来越多的程序员倡导 开源 ，认为软件开发应当是开放的，所有开发者都可以加入贡献。而开源的背后，必须要有一个开放共享的代码托管平台来支撑，而不是每个公司的代码都被封闭在自己内部的平台上。\n\n于是，以 GitHub 为代表的代码托管平台出现了。团队可以利用 GitHub 进行协作开发；开发者可以轻松阅读并参与到世界知名的开源项目中；我们每位同学也都可以免费使用 GitHub 上的代码，站在巨人的肩膀上打造自己的项目，或者通过参与开源来提升个人影响力等。\n\n此外，如今的 GitHub 还提供了更多强大的功能，比如问题讨论、在线编程、托管网站、自动构建项目等，对开发者非常有帮助。除了代码外，鱼皮也经常把一些需要团队内共享的文档上传到 GitHub 私仓和其他成员协作，免费无限制谁不爱呢？\n\n当然，GitHub 只是代码托管平台中最有代表性的一个，其他知名的代码开源托管平台有国内的 Gitee、国外的 Bitbucket 等；面向企业的私有代码托管平台有 GitLab、腾讯工蜂等。它们没有什么本质的区别，都是平台而已，会用 GitHub，就会用其他的。\n\n综上，强烈建议大家养成使用 GitHub 的习惯，感受开源的魅力。\n\n学习条件\n学过至少一门编程语言后就可以学习 Git 了\n准备开发个人完整项目前建议学习。注意是完整项目，还停留在初学编程语言写练习阶段的同学先不用学，抓紧熟悉编程语言就好。\n正式找工作前必须学习！\n学习建议\nGit 只是工具，学 Git 的时候可以轻松一些，千万不要去背命令！初学者只要先了解工作中常用命令的作用、提交代码的流程，然后跟着教程实操一遍，有个大致的印象就够了。忘了的话就掏出说明书（百度 / 文档等），随用随查即可。因此建议将专门学习 Git 的时间控制在 1 天内，一般几个小时就足够了，最快的话只用十几分钟也能上手。\n虽然现在有很多傻瓜式可视化 Git 工具，但还是建议初学 Git 的同学多自己手敲 Git 命令，可以多把自己平时写的练习代码用 Git 上传到 GitHub 上管理，用的多了自然就熟悉了。\nGit 的学习不是一蹴而就的。随着你使用 Git 管理的项目越来越多、工作经验越来越丰富，你对 Git 和 GitHub 的熟练程度才会越来越高。总之，想学好 Git，实践是关键。\n学习路线\n⭐️ - 重点知识 / 强烈推荐\n\n😐 - 了解即可', '[]', 0, 0, 1756735712212049922, '2024-02-25 02:54:54', '2024-02-25 02:59:08', 1);
INSERT INTO `post` VALUES (1761634940675710978, '1111', '111', '[\"11\"]', 0, 0, 1754526766348963841, '2024-02-25 14:11:14', '2024-02-25 14:11:29', 1);

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `images` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品图片',
  `title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述',
  `tags` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签列表（json 数组）',
  `price` double NOT NULL COMMENT '价格',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存',
  `isShelves` tinyint NOT NULL DEFAULT 1 COMMENT '是否上架',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1762763565068206083 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '产品' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1761645664613462018, 'https://zcc-1305301692.cos.ap-guangzhou.myqcloud.com/avatar/1754526766348963841/zv8ihggy-avtar.png', '22222', '111', '[\"测试\",\"111\"]', 999.99, 20, 1, '2024-02-25 14:53:50', '2024-02-27 14:06:14', 1);
INSERT INTO `product` VALUES (1761648239714807809, 'https://zcc-1305301692.cos.ap-guangzhou.myqcloud.com/avatar/1754526766348963841/FIteIm04-%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20220620183333.jpg', 'test', '1111', '[\'热门\']', 999.99, 100, 1, '2024-02-25 15:04:04', '2024-02-25 22:44:49', 1);
INSERT INTO `product` VALUES (1761650767869292546, 'https://zcc-1305301692.cos.ap-guangzhou.myqcloud.com/avatar/1754526766348963841/FIteIm04-%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20220620183333.jpg', 'test2', '1111', '[\'热门\']', 999.99, 100, 1, '2024-02-25 15:14:07', '2024-02-27 14:06:11', 1);
INSERT INTO `product` VALUES (1761774034642886658, 'https://img13.360buyimg.com/n1/s450x450_jfs/t1/150380/8/30334/31538/65531576Fc18207a9/163191ae98d461c0.jpg.avif', 'Apple iPhone 15 Pro Max', '继承了苹果创新精神的苹果iPhone 15 Pro Max，将科技与美学完美融合。其6.9英寸超视网膜XDR显示屏展现出绚丽细腻的色彩，配备A16仿生芯片，提供强大性能支持。拥有卓越的摄影能力，三摄系统搭配LiDAR扫描仪，实现更广阔的视野和更清晰的细节。支持5G网络，让您尽情畅游互联网的海洋。更有改进的电池管理系统，长续航让您无需担忧电量问题。iPhone 15 Pro Max，不仅是一款手机，更是您生活与工作的强大助手，引领着智能手机的未来潮流。', '[\"Apple\",\"手机\"]', 9999.99, 999, 1, '2024-02-25 23:23:56', '2024-02-27 14:09:13', 0);
INSERT INTO `product` VALUES (1762763565068206082, 'https://img10.360buyimg.com/n1/s450x450_jfs/t1/183854/26/38217/46154/6500d63dF19809a0a/f043008c71aafcb3.jpg.avif', 'Apple iPhone 15 Pro', '继承了苹果创新精神的苹果iPhone 15 Pro Max，将科技与美学完美融合。其6.9英寸超视网膜XDR显示屏展现出绚丽细腻的色彩，配备A16仿生芯片，提供强大性能支持。拥有卓越的摄影能力，三摄系统搭配LiDAR扫描仪，实现更广阔的视野和更清晰的细节。支持5G网络，让您尽情畅游互联网的海洋。更有改进的电池管理系统，长续航让您无需担忧电量问题。iPhone 15 Pro Max，不仅是一款手机，更是您生活与工作的强大助手，引领着智能手机的未来潮流。', '[\"Apple\"]', 8299, 999, 1, '2024-02-28 16:55:59', '2024-02-28 16:57:10', 0);

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `linkId` bigint NOT NULL COMMENT '关联评分id',
  `score` double NOT NULL DEFAULT 0 COMMENT '分数',
  `appraisal` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '评价',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `markUserId` bigint NULL DEFAULT NULL COMMENT '评分用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userId`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1766359315949871106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评分' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of score
-- ----------------------------
INSERT INTO `score` VALUES (1766331249726873602, 1764963105175240705, 98.7, '写的不错', 1754526766348963841, 1755571113714577409, '2024-03-09 13:12:41', '2024-03-09 15:03:50', 1);
INSERT INTO `score` VALUES (1766345430270218241, 1764837492481372162, 59, '一坨答辩', 1755571113714577409, 1754526766348963841, '2024-03-09 14:09:02', '2024-03-09 14:09:02', 0);
INSERT INTO `score` VALUES (1766359299839549442, 1764819347968401409, 100, '满分！\n', 1754526766348963841, 1756735712212049922, '2024-03-09 15:04:09', '2024-03-09 15:04:09', 0);
INSERT INTO `score` VALUES (1766359315949871105, 1764821777615462401, 90.5, '写得不错，继续保持', 1756735712212049922, 1754526766348963841, '2024-03-09 15:04:12', '2024-03-09 15:04:12', 0);

-- ----------------------------
-- Table structure for statistics
-- ----------------------------
DROP TABLE IF EXISTS `statistics`;
CREATE TABLE `statistics`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '网站信息统计id',
  `visit_count` int NULL DEFAULT 0 COMMENT '访问次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of statistics
-- ----------------------------
INSERT INTO `statistics` VALUES (1, 327);

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` bigint NOT NULL COMMENT '进度id',
  `taskName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `userId` bigint NOT NULL COMMENT '用户id',
  `principalId` bigint NOT NULL COMMENT '负责人id',
  `linkId` bigint NOT NULL COMMENT '关联id',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '进度状态',
  `progress` double(10, 2) NOT NULL COMMENT '任务完成百分比',
  `priority` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优先级：高、中、低',
  `startTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `endTime` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (1764819347968401409, '333', 1754526766348963841, 1756735712212049922, 1760351227505602562, '已完成', 100.00, '高', '2024-02-19 14:41:05', '2024-03-06 22:34:01', '2024-03-05 09:04:55', '2024-03-07 22:34:05', 0);
INSERT INTO `task` VALUES (1764821777615462401, '测试任务2', 1756735712212049922, 1754526766348963841, 1760192904428855298, '已完成', 100.00, '低', '2024-02-20 06:41:05', '2024-02-23 02:05:32', '2024-03-05 09:14:35', '2024-03-05 15:03:14', 0);
INSERT INTO `task` VALUES (1764837492481372162, '测试任务3', 1755571113714577409, 1754526766348963841, 1761463012149211138, '已完成', 100.00, '低', '2024-02-20 22:41:05', '2024-02-23 18:05:32', '2024-03-05 10:17:01', '2024-03-05 10:25:28', 0);
INSERT INTO `task` VALUES (1764963105175240705, '测试任务1', 1755571113714577409, 1754526766348963841, 1760192904428855293, '进行中', 25.00, '高', '2024-03-04 02:36:02', '2024-03-30 02:36:05', '2024-03-05 18:36:10', '2024-03-07 16:44:56', 0);
INSERT INTO `task` VALUES (1765660053557559297, '11111', 1754526766348963841, 1754526766348963841, 1760351227505602562, '进行中', 65.00, '低', '2024-03-05 08:49:01', '2024-03-05 19:45:23', '2024-03-07 16:45:35', '2024-03-07 17:41:45', 0);
INSERT INTO `task` VALUES (1765944059230490626, '我的任务1', 1754526766348963841, 1756735712212049922, 1761463012149211138, '未开始', 0.00, '高', '2024-03-07 16:00:01', '2024-03-14 16:33:55', '2024-03-08 11:34:08', NULL, 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint NOT NULL COMMENT '用户id，主键',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码(加密)',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户角色',
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码(加密)',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '性别',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号码',
  `status` int NOT NULL DEFAULT 1 COMMENT '账号状态 #1：正常, 2：禁用',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '个人简介',
  `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '家庭住址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1754526766348963841, 'admin', 'cd479aeb29460fa85e65a4c70db65ac1', 'admin', 'https://zcc-1305301692.cos.ap-guangzhou.myqcloud.com/avatar/1754526766348963841/FIteIm04-微信图片_20220620183333.jpg', 'zzc', '男', 'admin@163.com', '13531111111', 1, '未来优秀Java工程师', '广东省深圳市宝安区宝安大道233号', '2024-02-05 23:25:53', '2024-02-25 23:04:57', 0);
INSERT INTO `user` VALUES (1755571113714577409, 'test', 'cd479aeb29460fa85e65a4c70db65ac1', 'user', 'https://zcc-1305301692.cos.ap-guangzhou.myqcloud.com/avatar/1754526766348963841/QSBVqYU7-avtar.png', 'test', '女', 'user@163.com', '13531201111', 1, '小哥哥一枚呀', '广东省汕头市金平区外马路4836号', '2024-02-08 20:35:45', '2024-02-12 03:32:20', 0);
INSERT INTO `user` VALUES (1756735712212049922, 'user', 'cd479aeb29460fa85e65a4c70db65ac1', 'user', 'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png', 'user', '保密', 'user111@163.com', '13531201111', 1, '111', '广东省xx市xx区xx街道xx镇xx村xx号', '2024-02-12 01:43:27', '2024-02-25 02:34:09', 0);

SET FOREIGN_KEY_CHECKS = 1;
