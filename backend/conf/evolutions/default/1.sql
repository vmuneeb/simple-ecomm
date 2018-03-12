# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table banner (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  file_name                     varchar(255),
  created_time                  datetime(6) not null,
  updated_time                  datetime(6) not null,
  constraint pk_banner primary key (id)
);

create table brand (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  file_name                     varchar(255),
  created_time                  datetime(6) not null,
  updated_time                  datetime(6) not null,
  constraint pk_brand primary key (id)
);

create table cart (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  product_count                 integer,
  total_price                   bigint,
  total_price_formatted         varchar(255),
  currency                      varchar(255),
  created_time                  datetime(6) not null,
  updated_time                  datetime(6) not null,
  constraint uq_cart_user_id unique (user_id),
  constraint pk_cart primary key (id)
);

create table cart_product (
  id                            bigint auto_increment not null,
  cart_id                       bigint,
  product_id                    bigint,
  name                          varchar(255),
  price                         double,
  discount_price                double,
  formatted_price               varchar(255),
  discount_price_formatted      varchar(255),
  quantity                      integer,
  main_image                    varchar(255),
  created_time                  datetime(6) not null,
  updated_time                  datetime(6) not null,
  constraint uq_cart_product_cart_id_product_id unique (cart_id,product_id),
  constraint pk_cart_product primary key (id)
);

create table category (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  file_name                     varchar(255),
  created_time                  datetime(6) not null,
  updated_time                  datetime(6) not null,
  constraint pk_category primary key (id)
);

create table user_order (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  status                        integer,
  total                         integer,
  total_formatted               varchar(255),
  name                          varchar(255),
  building                      varchar(255),
  street                        varchar(255),
  area                          varchar(255),
  city                          varchar(255),
  email                         varchar(255),
  phone                         varchar(255),
  note                          varchar(255),
  product_count                 integer,
  total_price                   double,
  created_time                  datetime(6) not null,
  updated_time                  datetime(6) not null,
  constraint ck_user_order_status check (status in (0,1,2,3)),
  constraint pk_user_order primary key (id)
);

create table order_product (
  id                            bigint auto_increment not null,
  order_id                      bigint,
  product_id                    bigint,
  name                          varchar(255),
  price                         double,
  discount_price                double,
  formatted_price               varchar(255),
  discount_price_formatted      varchar(255),
  quantity                      integer,
  main_image                    varchar(255),
  constraint pk_order_product primary key (id)
);

create table product (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  price                         double,
  discount_price                double,
  formatted_price               varchar(255),
  discount_price_formatted      varchar(255),
  category_id                   bigint,
  sub_category_id               bigint,
  brand_id                      bigint,
  description                   varchar(255),
  main_image                    varchar(255),
  featured                      tinyint(1) default 0,
  quantity                      integer,
  created_time                  datetime(6) not null,
  updated_time                  datetime(6) not null,
  constraint pk_product primary key (id)
);

create table reset_pass_word (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  token                         varchar(255),
  expiry                        datetime(6),
  constraint uq_reset_pass_word_user_id unique (user_id),
  constraint pk_reset_pass_word primary key (id)
);

create table sub_category (
  id                            bigint auto_increment not null,
  category_id                   bigint,
  name                          varchar(255),
  file_name                     varchar(255),
  created_time                  datetime(6) not null,
  updated_time                  datetime(6) not null,
  constraint pk_sub_category primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  access_token                  varchar(255),
  name                          varchar(255),
  password                      varchar(255),
  email                         varchar(255),
  phone                         varchar(255),
  gender                        varchar(255),
  building                      varchar(255),
  street                        varchar(255),
  area                          varchar(255),
  city                          varchar(255),
  user_type                     integer,
  created_time                  datetime(6) not null,
  updated_time                  datetime(6) not null,
  constraint ck_user_user_type check (user_type in (0,1)),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id)
);

alter table cart add constraint fk_cart_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table cart_product add constraint fk_cart_product_cart_id foreign key (cart_id) references cart (id) on delete restrict on update restrict;
create index ix_cart_product_cart_id on cart_product (cart_id);

alter table user_order add constraint fk_user_order_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_order_user_id on user_order (user_id);

alter table order_product add constraint fk_order_product_order_id foreign key (order_id) references user_order (id) on delete restrict on update restrict;
create index ix_order_product_order_id on order_product (order_id);

alter table product add constraint fk_product_category_id foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_product_category_id on product (category_id);

alter table product add constraint fk_product_sub_category_id foreign key (sub_category_id) references sub_category (id) on delete restrict on update restrict;
create index ix_product_sub_category_id on product (sub_category_id);

alter table product add constraint fk_product_brand_id foreign key (brand_id) references brand (id) on delete restrict on update restrict;
create index ix_product_brand_id on product (brand_id);

alter table reset_pass_word add constraint fk_reset_pass_word_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table sub_category add constraint fk_sub_category_category_id foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_sub_category_category_id on sub_category (category_id);


# --- !Downs

alter table cart drop foreign key fk_cart_user_id;

alter table cart_product drop foreign key fk_cart_product_cart_id;
drop index ix_cart_product_cart_id on cart_product;

alter table user_order drop foreign key fk_user_order_user_id;
drop index ix_user_order_user_id on user_order;

alter table order_product drop foreign key fk_order_product_order_id;
drop index ix_order_product_order_id on order_product;

alter table product drop foreign key fk_product_category_id;
drop index ix_product_category_id on product;

alter table product drop foreign key fk_product_sub_category_id;
drop index ix_product_sub_category_id on product;

alter table product drop foreign key fk_product_brand_id;
drop index ix_product_brand_id on product;

alter table reset_pass_word drop foreign key fk_reset_pass_word_user_id;

alter table sub_category drop foreign key fk_sub_category_category_id;
drop index ix_sub_category_category_id on sub_category;

drop table if exists banner;

drop table if exists brand;

drop table if exists cart;

drop table if exists cart_product;

drop table if exists category;

drop table if exists user_order;

drop table if exists order_product;

drop table if exists product;

drop table if exists reset_pass_word;

drop table if exists sub_category;

drop table if exists user;

