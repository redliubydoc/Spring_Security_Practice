drop table tuser_role;
drop table tuser;
drop table trole;

create table tuser (
    user_id     varchar(50)     primary key,
    password    varchar(50)     unique not null
);

create table trole (
    role_id     varchar(3)      primary key,
    role_nm     varchar(50)     unique not null
);

create table tuser_role (
    user_id     varchar(50)     not null,
    role_id     varchar(3)      not null,

    primary key (
        user_id,
        role_id
    ),

    foreign key (
        user_id
    ) references tuser (
        user_id
    ) on delete cascade,

    foreign key (
        role_id
    ) references trole (
        role_id
    ) on delete cascade
);

-- dummy data
insert into tuser (user_id, password) values ('user_001', 'user_001');
insert into tuser (user_id, password) values ('user_002', 'user_002');
insert into tuser (user_id, password) values ('user_003', 'user_003');
insert into tuser (user_id, password) values ('user_004', 'user_004');
insert into tuser (user_id, password) values ('user_005', 'user_005');
insert into tuser (user_id, password) values ('user_006', 'user_006');
insert into tuser (user_id, password) values ('user_007', 'user_007');

insert into trole (role_id, role_nm) values ('ADM', 'ADMIN');
insert into trole (role_id, role_nm) values ('USR', 'USER');

insert into tuser_role (user_id, role_id) values ('user_001', 'ADM');
insert into tuser_role (user_id, role_id) values ('user_002', 'ADM');
insert into tuser_role (user_id, role_id) values ('user_003', 'USR');
insert into tuser_role (user_id, role_id) values ('user_004', 'USR');
insert into tuser_role (user_id, role_id) values ('user_005', 'USR');
insert into tuser_role (user_id, role_id) values ('user_006', 'USR');
insert into tuser_role (user_id, role_id) values ('user_007', 'USR');

select
    tuser.user_id,
    tuser.password,
    trole.role_nm
from
    sys.tuser,
    sys.trole,
    sys.tuser_role
where
    tuser.user_id = tuser_role.user_id and
    trole.role_id = tuser_role.role_id
order by
    user_id;
