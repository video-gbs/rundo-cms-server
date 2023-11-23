create table rundo_template
(
    id            bigint auto_increment
        primary key,
    template_name varchar(100) not null,
    update_time   datetime     null,
    create_time   datetime     null,
    constraint uni_template_name
        unique (template_name)
);

create table rundo_template_detail
(
    id           bigint auto_increment
        primary key,
    template_id  bigint            not null,
    date_type    tinyint           not null,
    start_time   time              not null,
    end_time     time              not null,
    is_next_day  tinyint default 0 null,
    enable_timer tinyint default 0 not null,
    create_time  datetime          null,
    update_time  datetime          null
);

create table rundo_template_use
(
    id               bigint auto_increment
        primary key,
    template_id      bigint            not null,
    service_name     varchar(100)      not null,
    service_use_mark varchar(100)      not null,
    enable_timer     tinyint default 0 not null,
    create_time      datetime          null,
    update_time      datetime          null,
    is_init_timer    tinyint default 0 not null,
    constraint uni_service_name_mark
        unique (service_name, service_use_mark)
);

