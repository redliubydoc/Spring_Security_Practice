/**
 * Schema Creation
 */

create user schema_fonebook identified by schema_fonebook;
alter user schema_fonebook quota unlimited on users;

grant 
    create session,
    create table,
    create procedure,
    create type,
    create trigger,
    create sequence
to
    schema_fonebook;

/**
 * Tables
 */

drop table tcontact_tag;
drop table tcontact_email;
drop table tcontact_phone;
drop table tuser_contact;
drop table tcontact;
drop table tuser;

create table tuser(
    user_id     varchar2(50)    not null,
    firstname   varchar2(50)    not null,
    lastname    varchar2(50)    not null,
    email_id    varchar2(200)   not null,

    constraint pk_tuser primary key(
        user_id
    )
);

create table tcontact(
    contact_id  number          not null,
    firstname   varchar2(50)    not null,
    lastname    varchar2(50)    not null,
    description clob,

    constraint pk_tcontact primary key(
        contact_id
    )
);

create table tuser_contact(
    user_id     varchar2(50)    not null,
    contact_id  number          not null,

    constraint uq_user_id_contact_id unique(
        user_id,
        contact_id
    ),

    constraint fk_tuser_tuser_contact foreign key(
        user_id
    ) references tuser(
        user_id
    ) on delete cascade,

    constraint fk_tcontact_tuser_contact foreign key(
        contact_id
    ) references tcontact(
        contact_id
    ) on delete cascade
);

create table tcontact_phone(
    isd_code    number          not null,
    phone_no    varchar2(10)    not null,
    contact_id  number          not null,

    constraint fk_tcontact_tcontact_phone foreign key(
        contact_id
    ) references tcontact(
        contact_id
    ) on delete cascade
);

create table tcontact_email(
    email_id   varchar2(200) not null,
    contact_id number        not null,

    constraint fk_tcontact_tcontact_email foreign key(
        contact_id
    ) references tcontact(
        contact_id
    ) on delete cascade
);

create table tcontact_tag(
    tag_nm      varchar2(20)    not null,
    contact_id  number          not null,

    constraint fk_tcontact_tcontact_tag foreign key(
        contact_id
    ) references tcontact(
        contact_id
    ) on delete cascade
);

/**
 * Types
 */

drop type type_varr_obj_phones;
drop type type_obj_phone;
drop type type_varr_email_ids;
drop type type_varr_tag_nms;

create or replace type type_obj_phone as object (
    isd_code    number,
    phone_no    varchar2(10)
);
create or replace type type_varr_obj_phones is varray(100) of type_obj_phone;
create or replace type type_varr_email_ids is varray(100) of varchar2(200);
create or replace type type_varr_tag_nms is varray(100) of varchar2(200);

/**
 * Sequences
 */

drop sequence seq_tcontact_contact_id;

create sequence seq_tcontact_contact_id
    minvalue        1
    maxvalue        1000000000
    start with      1
    increment by    1;

/**
 * Functions and Procedures
 */

create or replace procedure proc_add_contact(
    ip_user_id      in  tuser.user_id%type,
    ip_firstname    in  tcontact.firstname%type,
    ip_lastname     in  tcontact.lastname%type,
    ip_description  in  tcontact.description%type,
    ip_phones       in  type_varr_obj_phones,
    ip_email_ids    in  type_varr_email_ids,
    ip_tag_nms      in  type_varr_tag_nms
) as
    v_contact_id number;
begin
    v_contact_id := seq_tcontact_contact_id.nextval;

    /**
     * Insert into tcontact
     */

    if ip_description is null
    then
        insert into tcontact(
            contact_id,
            firstname,
            lastname
        ) values (
            v_contact_id,
            ip_firstname,
            ip_lastname
        );
    else
        insert into tcontact(
            contact_id,
            firstname,
            lastname,
            description
        ) values (
            v_contact_id,
            ip_firstname,
            ip_lastname,
            ip_description
        );
    end if;
    
    /**
     * Insert into tuser_contact
     */

    if ip_phones is not null
    then
        insert into 
            tuser_contact(
                user_id,
                contact_id
            ) values (
                ip_user_id,
                v_contact_id
            );
    end if;

    /**
     * Insert into tcontact_phone
     */

    if ip_phones is not null
    then
        for i in ip_phones.first .. ip_phones.last
        loop
            insert into tcontact_phone(
                isd_code,
                phone_no,
                contact_id
            ) values (
                ip_phones(i).isd_code,
                ip_phones(i).phone_no,
                v_contact_id
            );
        end loop;
    end if;

    /**
     * Insert into tcontact_email
     */

    if ip_email_ids is not null
    then
        for i in ip_email_ids.first .. ip_email_ids.last
        loop
            insert into tcontact_email(
                email_id,
                contact_id
            ) values (
                ip_email_ids(i),
                v_contact_id
            );
        end loop;
    end if;

    /**
     * Insert into tcontact_tag
     */

    if ip_tag_nms is not null
    then
        for i in ip_tag_nms.first .. ip_tag_nms.last
        loop
            insert into tcontact_tag(
                tag_nm,
                contact_id
            ) values (
                ip_tag_nms(i),
                v_contact_id
            );
        end loop;
    end if;
end;
/

/**
 * Sample data
 */

insert into tuser(user_id, firstname, lastname, email_id) values ('user_001', 'michel', 'martinez', 'michel.martinez@protonmail.com');
insert into tuser(user_id, firstname, lastname, email_id) values ('user_002', 'john',   'taylor',   'john.taylor@protonmail.com');
insert into tuser(user_id, firstname, lastname, email_id) values ('user_003', 'jessica','rose',     'jessica.rose@protonmail.com');
insert into tuser(user_id, firstname, lastname, email_id) values ('user_004', 'salon',  'jones',    'salon.jones@protonmail.com');
insert into tuser(user_id, firstname, lastname, email_id) values ('user_005', 'jack',   'jones',    'jack.jones@protonmail.com');
insert into tuser(user_id, firstname, lastname, email_id) values ('user_006', 'neil',   'strong',   'neil.strong@protonmail.com');
insert into tuser(user_id, firstname, lastname, email_id) values ('user_007', 'dhiraj', 'sharma',   'dhiraj.sharma@protonmail.com');


call proc_add_contact(
    'user_001',
    'contact_001',
    'contact_001',
    null,
    type_varr_obj_phones(
        type_obj_phone(91, '6290887852'),
        type_obj_phone(92, '6290887855')
    ),
    null,
    type_varr_tag_nms('bppimt', 'cts', 'dmhss')
);

insert into tcontact values ('001', 'dsd', 'sdsd', null);
insert into tcontact values ('002', 'dsd', 'sdsd', null);

select
    tuser_contact.user_id,
    tcontact.contact_id,
    tcontact.firstname,
    tcontact.lastname,
    tcontact.description,
    '(+' || tcontact_phone.isd_code || ')' || tcontact_phone.phone_no,
    tcontact_email.email_id,
    tcontact_tag.tag_nm
from
    tuser_contact
inner join tcontact on
    tuser_contact.contact_id = tcontact.contact_id
left outer join tcontact_phone on
    tcontact.contact_id = tcontact_phone.contact_id
left outer join tcontact_email on
    tcontact.contact_id = tcontact_email.contact_id
left outer join tcontact_tag on
            tcontact.contact_id = tcontact_tag.contact_id;

select
    *
from (
    select
        tuser_contact.user_id,
        tcontact.contact_id,
        tcontact.firstname,
        tcontact.lastname,
        tcontact.description,
        '(+' || tcontact_phone.isd_code || ')' || tcontact_phone.phone_no as phone_no,
        tcontact_email.email_id,
        tcontact_tag.tag_nm
    from
        tuser_contact
            inner join tcontact on
        tuser_contact.contact_id = tcontact.contact_id
            left outer join tcontact_phone on
        tcontact.contact_id = tcontact_phone.contact_id
            left outer join tcontact_email on
        tcontact.contact_id = tcontact_email.contact_id
            left outer join tcontact_tag on
        tcontact.contact_id = tcontact_tag.contact_id
    ) contacts
where
    user_id in (
        'user_001'
    );

select
    user_id,
    firstname,
    lastname,
    email_id
from
    tuser
where
    user_id = ?;