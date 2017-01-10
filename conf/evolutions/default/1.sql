# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table banner (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  target                        varchar(255),
  image_url                     varchar(255),
  constraint pk_banner primary key (id)
);

create table employee (
  id                            integer auto_increment not null,
  name                          varchar(255),
  constraint pk_employee primary key (id)
);

create table page (
  id                            bigint,
  title                         varchar(255),
  text                          varchar(255)
);

create table product (
  id                            bigint auto_increment not null,
  remote_id                     bigint,
  url                           varchar(255),
  name                          varchar(255),
  price                         double,
  price_formatted               varchar(255),
  discount_price                double,
  discount_price_formatted      varchar(255),
  category                      bigint,
  currency                      varchar(255),
  code                          varchar(255),
  description                   varchar(255),
  main_image                    varchar(255),
  main_image_high_res           varchar(255),
  constraint pk_product primary key (id)
);

create table region (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_region primary key (id)
);

create table shop (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  description                   varchar(255),
  url                           varchar(255),
  logo                          varchar(255),
  google_ua                     varchar(255),
  language                      varchar(255),
  currency                      varchar(255),
  flag_icon                     varchar(255),
  constraint pk_shop primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  access_token                  varchar(255),
  name                          varchar(255),
  street                        varchar(255),
  city                          varchar(255),
  house_number                  varchar(255),
  zip                           varchar(255),
  email                         varchar(255),
  phone                         varchar(255),
  gender                        varchar(255),
  country                       varchar(255),
  constraint pk_user primary key (id)
);


# --- !Downs

drop table if exists banner;

drop table if exists employee;

drop table if exists page;

drop table if exists product;

drop table if exists region;

drop table if exists shop;

drop table if exists user;

