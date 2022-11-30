CREATE SCHEMA IF NOT EXISTS winwin;

create table if not exists main_category
(
    id            bigint auto_increment
        primary key,
    created_date  datetime(6)  null,
    modified_date datetime(6)  null,
    image         varchar(255) not null,
    name          varchar(255) not null
);
create table if not exists mid_category
(
    id               bigint auto_increment
        primary key,
    created_date     datetime(6)  null,
    modified_date    datetime(6)  null,
    image            varchar(255) not null,
    main_category_id bigint       not null,
    name             varchar(255) not null
);
create table if not exists sub_category
(
    id              bigint auto_increment
        primary key,
    created_date    datetime(6)  null,
    modified_date   datetime(6)  null,
    image           varchar(255) not null,
    mid_category_id bigint       not null,
    name            varchar(255) not null
);

INSERT INTO main_category (id, created_date, modified_date, image, name)
VALUES (1, null, null, 'mainCategory/1', '대1');
INSERT INTO main_category (id, created_date, modified_date, image, name)
VALUES (2, null, null, 'mainCategory/2', '대2');
INSERT INTO main_category (id, created_date, modified_date, image, name)
VALUES (3, null, null, 'mainCategory/3', '대3');
INSERT INTO mid_category (id, created_date, modified_date, image, main_category_id, name)
VALUES (1, null, null, 'midCategory/1', 1, '대1중1');
INSERT INTO mid_category (id, created_date, modified_date, image, main_category_id, name)
VALUES (2, null, null, 'midCategory/2', 1, '대1중2');
INSERT INTO mid_category (id, created_date, modified_date, image, main_category_id, name)
VALUES (3, null, null, 'midCategory/3', 1, '대1중3');
INSERT INTO mid_category (id, created_date, modified_date, image, main_category_id, name)
VALUES (4, null, null, 'midCategory/4', 2, '대2중1');
INSERT INTO mid_category (id, created_date, modified_date, image, main_category_id, name)
VALUES (5, null, null, 'midCategory/5', 2, '대2중2');
INSERT INTO mid_category (id, created_date, modified_date, image, main_category_id, name)
VALUES (6, null, null, 'midCategory/6', 2, '대2중3');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (1, null, null, 'subCategory/1', 1, '대1중1소1');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (2, null, null, 'subCategory/2', 1, '대1중1소2');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (3, null, null, 'subCategory/3', 2, '대1중2소1');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (4, null, null, 'subCategory/4', 2, '대1중2소2');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (5, null, null, 'subCategory/5', 3, '대1중3소1');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (6, null, null, 'subCategory/6', 3, '대1중3소2');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (7, null, null, 'subCategory/7', 4, '대2중1소1');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (8, null, null, 'subCategory/8', 4, '대2중1소2');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (9, null, null, 'subCategory/9', 5, '대2중2소1');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (10, null, null, 'subCategory/10', 5, '대2중2소2');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (11, null, null, 'subCategory/11', 6, '대2중3소1');
INSERT INTO sub_category (id, created_date, modified_date, image, mid_category_id, name)
VALUES (12, null, null, 'subCategory/12', 6, '대2중3소2');
