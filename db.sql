create sequence role_seq start 1;

create table "role"
(
    id           bigint primary key default nextval('role_seq'),
    code varchar(20),
    name varchar(20),
    created_by bigint,
    created_date timestamp,
    updated_by bigint,
    updated_date timestamp
);


create sequence user_seq start 1;

create table "user"
(
    id           bigint primary key default nextval('user_seq'),
    email        varchar(50) unique,
    password     varchar(200),
    phone        varchar(20),
    name         varchar(100),
    avatar       varchar(300),
    address      varchar(500),
    linked       boolean,
    paypal_id    varchar(300),
    status       boolean,
    role_id      bigint,
    created_by   bigint,
    created_date timestamp,
    updated_by   bigint,
    updated_date timestamp,
    constraint fk_role_id foreign key (role_id) references "role"(id)
);



create sequence hotel_seq start 1;

create table "hotel"
(
    id           bigint primary key default nextval('hotel_seq'),
    account_id   bigint,
    name         varchar(300),
    address      varchar(500),
    city         varchar(300),
    email        varchar(50) unique,
    phone        varchar(20),
    description  text,
    rate         numeric,
    created_by   bigint,
    created_date timestamp,
    updated_by   bigint,
    updated_date timestamp,
    constraint fk_user_id foreign key (account_id) references "user" (id)
);

create sequence hotel_amenities_seq start 1;

create table "hotel_amenities"
(
    id           bigint primary key default nextval('hotel_amenities_seq'),
    name         varchar(200),
    hotel_id     bigint,
    created_by   bigint,
    created_date timestamp,
    updated_by   bigint,
    updated_date timestamp,
    constraint fk_hotel_id foreign key (hotel_id) references "hotel" (id)
);

create sequence hotel_image_seq start 1;

create table "hotel_image"
(
    id           bigint primary key default nextval('hotel_image_seq'),
    hotel_id     bigint,
    image_link   varchar(500),
    created_by   bigint,
    created_date timestamp,
    updated_by   bigint,
    updated_date timestamp,
    constraint fk_hotel_id_2 foreign key (hotel_id) references "hotel" (id)
);


create sequence room_seq start 1;
create table "room"
(
    id           bigint primary key default nextval('room_seq'),
    hotel_id     bigint,
    name         varchar(100),
    prepay       numeric,
    status       varchar(100),
    description  text,
    price        numeric,
    area         numeric,
    created_by   bigint,
    created_date timestamp,
    updated_by   bigint,
    updated_date timestamp,
    constraint fk_hotel_id_4 foreign key (hotel_id) references "hotel" (id)
);

create sequence room_amenities_seq start 1;
create table "room_amenities"
(
    id           bigint primary key default nextval('room_amenities_seq'),
    name         varchar(200),
    room_id      bigint,
    created_by   bigint,
    created_date timestamp,
    updated_by   bigint,
    updated_date timestamp,
    constraint room_id foreign key (room_id) references "room" (id)
);

create sequence room_image_seq start 1;

create table "room_image"
(
    id           bigint primary key default nextval('room_image_seq'),
    room_id      bigint,
    image_link   varchar(500),
    created_by   bigint,
    created_date timestamp,
    updated_by   bigint,
    updated_date timestamp,
    constraint room_id_2 foreign key (room_id) references "room" (id)
);

create sequence rule_seq start 1;

create table "rule"
(
    id           bigint primary key default nextval('rule_seq'),
    room_id      bigint,
    name         varchar(300),
    created_by   bigint,
    created_date timestamp,
    updated_by   bigint,
    updated_date timestamp,
    constraint room_id_4 foreign key (room_id) references "room" (id)
);

create sequence rating_seq start 1;

create table "rating"
(
    id           bigint primary key default nextval('rating_seq'),
    hotel_id     bigint,
    rate         numeric,
    code         varchar(100),
    name         varchar(100),
    text         text,
    created_by   bigint,
    created_date timestamp,
    updated_by   bigint,
    updated_date timestamp,
    constraint fk_hotel_id_3 foreign key (hotel_id) references "hotel" (id)
);


create sequence guest_seq start 1;

create table "guest"
(
    id bigint primary key default nextval('guest_seq'),
    adult smallint,
    children smallint,
    created_by    bigint,
    created_date  timestamp,
    updated_by    bigint,
    updated_date  timestamp
);

create sequence reservation_seq start 1;

create table "reservation"
(
    id            bigint primary key default nextval('reservation_seq'),
    room_id       bigint,
    guest_id      bigint,
    cancel_reason text,
    customer_id   bigint,
    email         varchar(50),
    code          varchar(100),
    name          varchar(100),
    address       varchar(500),
    note          varchar(500),
    check_in      timestamp,
    check_out     timestamp,
    cost          numeric,
    status        varchar(20),
    phone         varchar(20),
    created_by    bigint,
    created_date  timestamp,
    updated_by    bigint,
    updated_date  timestamp,
    constraint room_id_3 foreign key (room_id) references "room" (id),
    constraint fk_guest foreign key (guest_id) references "guest"(id)
);


INSERT INTO public.role (id, code, name, created_by, created_date, updated_by, updated_date) VALUES (1, 'USER', 'User', null, now(), null, now());
INSERT INTO public.role (id, code, name, created_by, created_date, updated_by, updated_date) VALUES (2, 'ADMIN', 'Admin', null, now(), null, now());
INSERT INTO public.role (id, code, name, created_by, created_date, updated_by, updated_date) VALUES (3, 'SUPER_ADMIN', 'Super Admin', null, now(), null, now());
