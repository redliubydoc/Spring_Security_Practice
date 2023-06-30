drop table trefresh_token;
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

create table trefresh_token (
    token       varchar(50)     primary key,
    user_id     varchar(50)     not null,
    expires_at  timestamp       not null,

    constraint unique_token unique(
        token
    ),

    foreign key (
        user_id
    ) references tuser (
        user_id
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

insert into trefresh_token (token, user_id, expires_at) values ('token_001', 'user_001', sysdate + 15);
insert into trefresh_token (token, user_id, expires_at) values ('token_002', 'user_002', sysdate + 15);
insert into trefresh_token (token, user_id, expires_at) values ('token_003', 'user_003', sysdate + 15);
insert into trefresh_token (token, user_id, expires_at) values ('token_004', 'user_001', sysdate + 15);
insert into trefresh_token (token, user_id, expires_at) values ('token_005', 'user_002', sysdate + 15);
insert into trefresh_token (token, user_id, expires_at) values ('token_006', 'user_003', sysdate + 15);
insert into trefresh_token (token, user_id, expires_at) values ('token_007', 'user_001', sysdate + 15);
insert into trefresh_token (token, user_id, expires_at) values ('token_008', 'user_002', sysdate + 15);
insert into trefresh_token (token, user_id, expires_at) values ('token_009', 'user_003', sysdate + 15);
insert into trefresh_token (token, user_id, expires_at) values ('token_010', 'user_001', sysdate + 15);
insert into trefresh_token (token, user_id, expires_at) values ('token_011', 'user_002', sysdate + 15);
insert into trefresh_token (token, user_id, expires_at) values ('token_012', 'user_003', sysdate + 15);

-- procedure to add new refresh token
create or replace procedure proc_add_token(
    ip_token                    in  trefresh_token.token%type,
    ip_user_id                  in  trefresh_token.user_id%type,
    ip_expires_at               in  trefresh_token.expires_at%type,
    ip_max_token_count_per_user in  number
) as
    v_max_token_count_per_user number := 0;
begin
    /**
    * Check how many tokens are the there for the given user
    */
    select
        count(*) as token_count
    into
        v_max_token_count_per_user
    from
        trefresh_token
    where
        user_id = ip_user_id;

    if v_max_token_count_per_user >= ip_max_token_count_per_user
    then
        /**
        * Delete the oldest token
        */
        delete from
            trefresh_token
        where
            user_id = ip_user_id
        and expires_at = (
            select
                min(expires_at)
            from
                trefresh_token
            where
                user_id = ip_user_id
        );
    end if;

    /**
    * Insert the given required record
    */

    insert into trefresh_token(
        token,
        user_id,
        expires_at
    ) values (
        ip_token,
        ip_user_id,
        ip_expires_at
    );
end;
/

call proc_add_token('token_010', 'user_002', sysdate, 5);

select
    tuser.user_id,
    tuser.password,
    trole.role_nm
from
    tuser,
    trole,
    tuser_role
where
    tuser.user_id = tuser_role.user_id and
    trole.role_id = tuser_role.role_id
order by
    user_id;

select 
    token,
    user_id,
    expires_at
from
    trefresh_token
where 
    user_id = 'user_001';

select 
    token,
    user_id,
    expires_at
from
    trefresh_token
order by
    user_id,
    expires_at;

insert into trefresh_token (
    token,
    user_id,
    expires_at
) values (
    'token_100',
    'user_001',
    sysdate + 15
);


delete from
    trefresh_token
where
    user_id = ?
    and token = ?;

-- number of token for a particular user
select
    count(token) as token_count
from
    trefresh_token
where
    user_id = ?

-- delete oldest token for a particular user
delete from
    trefresh_token
where
    user_id = ?
    and expires_at = (
        select
            min(expires_at)
        from
            trefresh_token
        where
            user_id = ?
    )