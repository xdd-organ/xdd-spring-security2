drop database if exists security;
create database security default character set utf8;

use security3;

drop table if exists role_privilege;
drop table if exists account_role;

drop table if exists role;
drop table if exists account;
drop table if exists url;
drop table if exists privilege;

create table account
(
   id                  int not null auto_increment,
   login               varchar(20),
   name                varchar(20),
   pass                varchar(20),
   primary key (id)
);

create table role
(
	id int not null auto_increment,
	name varchar(200),
	detail varchar(200),
	primary key(id)
);


create table privilege
(
    id int not null auto_increment,
    name varchar(200),
    primary key(id)
);
/* 一个privilege对应多个url地址  */
create table url
(
    id int not null auto_increment,
    url varchar(200),
    pid int,
    primary key(id)
);

create table privilege_role
(
	pid int,
	rid int,
	primary key(rid,pid)
);

create table account_role(
	aid int,
	rid int,
	primary key(aid,rid)
);
/******************添加用户与角色测试数据*************************/
INSERT INTO account (login,name,pass) VALUES ('test01','张三','test01');
INSERT INTO account (login,name,pass) VALUES ('test02','李四','test02');
INSERT INTO role (name,detail) VALUES ('ADMIN','管理员账户');
INSERT INTO role (name,detail) VALUES ('USER','普通账户');
INSERT INTO account_role (aid,rid) VALUES (1,1);
INSERT INTO account_role (aid,rid) VALUES (2,2);
/****************************************************************/

/******************添加资源与角色测试数据*************************/
INSERT INTO privilege (name) VALUES ('管理员页面');
INSERT INTO privilege (name) VALUES ('用户页面');

INSERT INTO url (url,pid) VALUES ('/admin/save.jsp',1);
INSERT INTO url (url,pid) VALUES ('/admin/save2.jsp',1);
INSERT INTO url (url,pid) VALUES ('/user/save.jsp',2);
INSERT INTO url (url,pid) VALUES ('/user/save2.jsp',2);

INSERT INTO privilege_role (pid,rid) VALUES (1,1);
INSERT INTO privilege_role (pid,rid) VALUES (2,1);
INSERT INTO privilege_role (pid,rid) VALUES (2,2);
/****************************************************************/

SELECT * FROM account_role;
SELECT * FROM privilege_role;
SELECT * FROM account;
SELECT * FROM role;
SELECT * FROM privilege;
SELECT * FROM url;

/* spring security 记住我，cookie保存到数据库*/
create table persistent_logins (username varchar(64) not null,
    series varchar(64) primary key,
    token varchar(64) not null,
    last_used timestamp not null
);