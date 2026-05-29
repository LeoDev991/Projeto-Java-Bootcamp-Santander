create table users (
    id uuid primary key,
    email varchar(120) not null unique,
    password_hash varchar(255) not null
);

create table user_roles (
    user_id uuid not null references users(id),
    role varchar(30) not null,
    primary key (user_id, role)
);

create table customers (
    id uuid primary key,
    name varchar(120) not null,
    email varchar(120) not null unique,
    document varchar(16) not null,
    active boolean not null,
    created_at timestamp with time zone not null
);

create table products (
    id uuid primary key,
    name varchar(100) not null,
    description varchar(500) not null,
    price numeric(15, 2) not null,
    stock_quantity integer not null,
    active boolean not null,
    version bigint
);

create table orders (
    id uuid primary key,
    customer_id uuid not null references customers(id),
    status varchar(30) not null,
    payment_method varchar(30) not null,
    shipping_method varchar(30) not null,
    items_total numeric(15, 2) not null,
    shipping_total numeric(15, 2) not null,
    grand_total numeric(15, 2) not null,
    created_at timestamp with time zone not null
);

create table order_items (
    id uuid primary key,
    order_id uuid not null references orders(id),
    product_id uuid not null references products(id),
    quantity integer not null,
    unit_price numeric(15, 2) not null,
    total numeric(15, 2) not null
);

create index idx_products_name on products (name);
create index idx_products_active_price on products (active, price);
create index idx_orders_customer_status on orders (customer_id, status);
create index idx_order_items_order on order_items (order_id);
