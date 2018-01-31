Demo project for Spring Boot, Mybatis, Redis
----------------------------------------------------
### Dependencies:
```xml
<dependency>
  <groupId>org.mybatis.spring.boot</groupId>
  <artifactId>mybatis-spring-boot-starter</artifactId>
  <version>1.3.1</version>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <scope>runtime</scope>
</dependency>
```
### Architecture:
* Domain
* Repository
* Service
* Controller
### Mariadb Table:
```sql
CREATE TABLE `user` (
	`sid` INT(11) NOT NULL AUTO_INCREMENT,
	`enabled` TINYINT(1) NOT NULL DEFAULT '0',
	`created` DATETIME NOT NULL,
	`created_by` INT(11) NOT NULL,
	`last_modified` DATETIME NULL DEFAULT NULL,
	`last_modified_by` INT(11) NULL DEFAULT NULL,
	`primary_org` INT(11) NULL DEFAULT NULL,
	`id` VARCHAR(45) NOT NULL,
	`name` VARCHAR(45) NOT NULL,
	`custom_settings` VARCHAR(45) NULL DEFAULT NULL,
	`email` VARCHAR(40) NULL DEFAULT NULL,
	PRIMARY KEY (`sid`),
	UNIQUE INDEX `UX_user__id` (`id`),
	INDEX `IDX_user__primary_org` (`primary_org`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=5
;

INSERT INTO `user` (`sid`, `enabled`, `created`, `created_by`, `last_modified`, `last_modified_by`, `primary_org`, `id`, `name`, `custom_settings`, `email`) VALUES
	(1, 1, '2018-01-29 15:56:07', 1, NULL, NULL, NULL, 'admin', 'admin_name', NULL, NULL),
	(2, 1, '2018-01-29 15:59:15', 1, NULL, NULL, NULL, 'user_01', 'user_01_name', NULL, NULL),
	(3, 0, '2018-01-29 15:59:34', 1, NULL, NULL, NULL, 'user_02', 'user_02_name', NULL, NULL),
	(4, 1, '2018-09-18 03:32:54', 2, NULL, NULL, NULL, 'user_04', 'user_04_name', NULL, NULL);
```
### Manual Test:
* (GET)localhost:8080/user/all
* (GET)localhost:8080/user/findBySid/{sid}
* (GET)localhost:8080/user/findById/{id}
* (POST)localhost:8080/user/add
* (PUT)localhost:8080/user/update
* (DELETE)localhost:8080/user/delete/{sid}
----
### Redis:
* Dependency:
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
  <version>1.5.9.RELEASE</version>
</dependency>
```
* Configure: application.properties
```shell
## Redis
## Database Index(default: 0)
spring.redis.database=0
spring.redis.host=vm
spring.redis.port=52031
## PW（default: empty）
spring.redis.password=
## Max pool connections(negative is unlimited)
spring.redis.pool.max-active=8
## Max pool waitting-time(negative is unlimited)
spring.redis.pool.max-wait=-1
## Max pool idel connections
spring.redis.pool.max-idle=8
## Min pool idel connections
spring.redis.pool.min-idle=0
## connection timeout(ms)
spring.redis.timeout=0
```
* Cache Aside Pattern:
  * Hits the cache: return it.
  * Not hits the cache: query it from database, if successful, add it to cache.
  * Update: first update to database, if successful, delete the cache.
  * Create: No need to add to the cache.
* Code Snippet:
```java
@Autowired
private RedisTemplate redisTemplate;
  
ValueOperations<String, User> operations = redisTemplate.opsForValue();
// Hits cache
if (redisTemplate.hasKey(key)) {
  return operations.get(key);
}

// Add to cache
operations.set(key, object, 10, TimeUnit.SECONDS);

// Delete cached
redisTemplate.delete(key)
```
----------------------------------------------------
### TODO:
* Study how to design a cached architecture:
  * Use a different key for each method: This will cache the same object repeatedly, which results in wasted cache memory / space and potential performance issues, and make it more difficult to delete or update the cache.
  * Cached for individual objects: This will make the program structure more complex.
* Transaction effects
* What situations is applied to:
  * redisTemplate.opsForValue();
  * redisTemplate.opsForHash();
  * redisTemplate.opsForList();
  * redisTemplate.opsForSet();
  * redisTemplate.opsForZSet();

----------------------------------------------------
### References:
* [Spring Boot 整合 Redis 实现缓存操作](https://www.bysocket.com/?p=1756)
* [mybatis/spring-boot-starter](https://github.com/mybatis/spring-boot-starter)
* [JeffLi1993/springboot-learning-example](https://github.com/JeffLi1993/springboot-learning-example/blob/master/springboot-mybatis-redis/pom.xml)
* [mybatis中Date和DateTime字段的插入](http://blog.csdn.net/qiaomu8559968/article/details/7995251)