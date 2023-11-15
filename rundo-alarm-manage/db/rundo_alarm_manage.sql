create table rundo_alarm_event
(
    id          bigint auto_increment
        primary key,
    event_name  varchar(50)  not null,
    event_code  varchar(100) not null,
    event_sort  int          not null,
    event_desc  varchar(250) null,
    create_time datetime     null,
    update_time datetime     null,
    constraint uni_event_code
        unique (event_code),
    constraint uni_event_name
        unique (event_name)
);

create table rundo_alarm_msg
(
    id               bigint auto_increment
        primary key,
    channel_id       bigint       not null,
    alarm_code       varchar(100) not null,
    alarm_level      tinyint      not null,
    alarm_start_time datetime     not null,
    alarm_end_time   datetime     null,
    alarm_state      tinyint      not null,
    alarm_interval   int          not null,
    alarm_desc       varchar(250) null,
    video_url        varchar(500) null,
    video_state      tinyint      null,
    video_length     int          null,
    video_stream_id  varchar(100) null,
    image_url        varchar(500) null,
    image_state      tinyint      null,
    create_time      datetime     null,
    update_time      datetime     null
);

create table rundo_alarm_msg_error
(
    id              bigint auto_increment
        primary key,
    alarm_msg_id    bigint       not null,
    alarm_file_type tinyint      not null,
    error_msg       varchar(500) not null,
    create_time     datetime     not null
);

create table rundo_alarm_scheme
(
    id          bigint auto_increment
        primary key,
    scheme_name varchar(50) not null,
    template_id bigint      not null,
    disabled    tinyint     not null,
    create_time datetime    null,
    update_time datetime    null,
    constraint uni_scheme_name
        unique (scheme_name)
);

create table rundo_alarm_scheme_channel
(
    id           bigint auto_increment
        primary key,
    scheme_id    bigint            not null,
    channel_id   bigint            not null,
    deploy_state tinyint default 0 not null,
    create_time  datetime          null,
    update_time  datetime          null,
    constraint uni_channel_id
        unique (channel_id)
);

create table rundo_alarm_scheme_event
(
    id              bigint auto_increment
        primary key,
    scheme_id       bigint       not null,
    event_code      varchar(100) not null,
    event_level     int          not null,
    event_interval  int          not null,
    enable_video    tinyint      not null,
    enable_photo    tinyint      not null,
    video_length    int          null,
    video_has_audio tinyint      null,
    update_time     datetime     null,
    create_time     datetime     null,
    constraint uni_scheme_event_id
        unique (scheme_id)
);

