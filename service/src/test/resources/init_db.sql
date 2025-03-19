create table if not exists item
(
    id serial primary key,
    name varchar,
    description varchar,
    quantity bigint,
    price bigint,
    discount_price bigint,
    created_date timestamp,
    category_id bigint
);

create table if not exists category (
    id serial primary key,
    name varchar
);

create table if not exists employee (
    id serial primary key,
    name varchar,
    position varchar,
    salary bigint,
    started_date timestamp,
    end_date timestamp,
    department varchar,
    email varchar
);

create table if not exists transport (
  id varchar primary key,
  accepted_date timestamp,
  delivery_date timestamp,
  company varchar,
  brand varchar,
  serial varchar,
  mark varchar
);

create table if not exists delivery (
    id varchar primary key,
    transport_id varchar,
    created_date timestamp,
    qty bigint,
    username varchar
);

create table if not exists item_delivery_link (
    id varchar primary key,
    delivery_id varchar,
    item_id varchar,
    quantity bigint
)
-- insert into item(name, description, quantity, price, created_date, category_id) values (
--                                                                                    'Item 1',
--                                                                                   'Item 1 description',
--                                                                                   100,
--                                                                                   1000,
--                                                                                   '2020-01-01 00:00:00',
--                                                                                   1
--                                                                                   )