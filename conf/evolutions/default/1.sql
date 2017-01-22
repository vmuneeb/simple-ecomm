# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  category_id                   bigint auto_increment not null,
  name                          varchar(255),
  image                         varchar(255),
  constraint pk_category primary key (category_id)
);

create table product (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  price                         double,
  discount_price                double,
  category_id                   bigint,
  description                   varchar(255),
  main_image                    varchar(255),
  constraint pk_product primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  access_token                  varchar(255),
  name                          varchar(255),
  password                      varchar(255),
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

alter table product add constraint fk_product_category_id foreign key (category_id) references category (category_id) on delete restrict on update restrict;
create index ix_product_category_id on product (category_id);


# --- !Downs

alter table product drop foreign key fk_product_category_id;
drop index ix_product_category_id on product;

drop table if exists category;

drop table if exists product;

drop table if exists user;

