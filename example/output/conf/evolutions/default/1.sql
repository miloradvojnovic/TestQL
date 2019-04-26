# --- !Ups

CREATE TABLE categories(
  id                SERIAL      NOT NULL PRIMARY KEY,
  name              VARCHAR(30) NOT NULL,
  super_category_id SERIAL      NOT NULL,

  CONSTRAINT categories_super_category_id_fk FOREIGN KEY (super_category_id) REFERENCES categories (id)
);

CREATE TABLE users (
  id          SERIAL      NOT NULL PRIMARY KEY,
  username    VARCHAR(30) NOT NULL UNIQUE,
  password    VARCHAR(30) NOT NULL,
  first_name  VARCHAR(30) NOT NULL,
  last_name   VARCHAR(30) NOT NULL,
  address     VARCHAR(30) NOT NULL,
  phone       VARCHAR(30) NOT NULL,
  email       VARCHAR(30) NOT NULL,
  money       NUMERIC     NOT NULL
);

CREATE TABLE shopping_carts (
  id          SERIAL      NOT NULL PRIMARY KEY,
  user_id     SERIAL      NOT NULL,

  CONSTRAINT shopping_carts_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE products (
  id           SERIAL      NOT NULL PRIMARY KEY,
  manufacturer VARCHAR(30) NOT NULL,
  price        NUMERIC     NOT NULL,
  description  VARCHAR(30) NOT NULL,
  product_url  VARCHAR(30) NOT NULL,
  quantity     INTEGER     NOT NULL,
  category_id  SERIAL      NOT NULL,

  CONSTRAINT products_category_id_fk FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE orders(
  id               SERIAL      NOT NULL PRIMARY KEY,
  total_price      NUMERIC     NOT NULL,
  user_id          SERIAL      NOT NULL,

  CONSTRAINT orders_order_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE order_items(
  id               SERIAL      NOT NULL PRIMARY KEY,
  price            NUMERIC     NOT NULL,
  product_id       SERIAL      NOT NULL,
  shopping_cart_id SERIAL,
  order_id         SERIAL,

  CONSTRAINT order_items_product_id_fk FOREIGN KEY (product_id) REFERENCES products (id),
  CONSTRAINT order_items_shopping_cart_id_fk FOREIGN KEY (shopping_cart_id) REFERENCES shopping_carts (id),
  CONSTRAINT order_items_order_id_fk FOREIGN KEY (order_id) REFERENCES orders (id)
);


# --- !Downs

DROP TABLE order_items;

DROP TABLE orders;

DROP TABLE products;

DROP TABLE shopping_carts;

DROP TABLE users;

DROP TABLE categories;
