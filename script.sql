create table attend
(
    attend_id int auto_increment
        primary key,
    doctor_id int  null,
    date      date not null,
    number    int  not null
);

create index doctor_id
    on attend (doctor_id, attend_id);

create table department
(
    office_id int auto_increment
        primary key,
    dname     varchar(20) not null,
    constraint name
        unique (office_id)
);

create table department_doctor
(
    id         int auto_increment
        primary key,
    department int null,
    doctor_id  int null
);

create index department_doctor_department_index
    on department_doctor (department);

create table menu
(
    id        bigint auto_increment
        primary key,
    parent_id bigint       null comment '父菜单ID，一级菜单为0',
    name      varchar(64)  not null,
    path      varchar(255) null comment '菜单URL',
    perms     varchar(255) null comment '授权(多个用逗号分隔，如：user:list,user:create',
    component varchar(255) null,
    type      int          not null comment '类型  0：目录  1：菜单  2：按钮',
    icon      varchar(32)  null comment '菜单图标',
    order_num int          null comment '排序',
    created   datetime     null,
    updated   datetime     null,
    status    int          not null,
    constraint name
        unique (name, id)
)
    charset = utf8;

create table `order`
(
    order_id         int auto_increment
        primary key,
    user_id          int                  null comment '预约的用户',
    attend_id        int                  null comment '预约哪天哪个医生',
    register_status  tinyint(1) default 0 null comment '是否报到',
    diagnosis_status tinyint(1) default 0 null comment '是否诊断',
    diagnosis        varchar(2000)        null comment '诊断书'
);

create index attend_id
    on `order` (attend_id, order_id);

create table role
(
    id          bigint auto_increment
        primary key,
    name        varchar(64)          not null,
    role        varchar(64)          not null,
    description varchar(64)          null comment '备注',
    status      tinyint(1) default 1 not null,
    created     datetime             null,
    updated     datetime             null,
    constraint code
        unique (role)
)
    charset = utf8;

create table role_menu
(
    id      bigint auto_increment
        primary key,
    role_id bigint not null,
    menu_id bigint not null
);

create index role_menu_role_id_index
    on role_menu (role_id);

create table user
(
    id          int auto_increment
        primary key,
    avatar      varchar(255)         null comment '头像',
    username    varchar(20)          not null,
    password    varchar(200)         not null,
    name        varchar(20)          null,
    id_card     varchar(20)          null,
    phone       varchar(20)          null,
    description varchar(255)         null,
    status      tinyint(1) default 1 not null,
    cov         tinyint(1) default 0 null comment '是否感染新冠肺炎',
    created     datetime             null,
    updated     datetime             null,
    constraint username
        unique (username, id)
);

create table user_role
(
    id      bigint auto_increment
        primary key,
    user_id bigint not null,
    role_id bigint not null
);

create index user_role_user_id_index
    on user_role (user_id);


