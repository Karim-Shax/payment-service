create table account_info
(
    id      bigserial    not null,
    account varchar(255) not null,
    primary key (id)
);
create table expense_limits
(
    id                       bigserial not null,
    limit_currency_shortname varchar(10)    default 'USD',
    expense_category         varchar(255),
    limit_date_time          TIMESTAMP,
    limit_sum                numeric(19, 2) default 0.00,
    limit_temporary          numeric(19, 2) default 0.00,
    account_info_id          int8,
    primary key (id)
);

create table payment_transactions
(
    id                  bigserial      not null,
    account_to          varchar(20)    not null,
    currency_short_name varchar(10)    not null,
    date_time           timestamp,
    expense_category    varchar(20)    not null,
    limit_exceeded      SMALLINT,
    transaction_sum     numeric(19, 2) not null,
    account_info_id     int8,
    primary key (id)
);

alter table if exists account_info add constraint account_info_unique unique (account);
alter table if exists expense_limits add constraint account_and_category_unique unique (account_info_id, expense_category);
alter table if exists expense_limits add constraint account_info_id_fk foreign key (account_info_id) references account_info;
alter table if exists payment_transactions add constraint account_info_id_fk foreign key (account_info_id) references account_info;