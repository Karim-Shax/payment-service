insert into account_info (id,accountInfo)
values (1,'87770981997');

insert into expense_limit (id, currency, expense_category, limit_date_time, limit_sum, account_info_id)
values (1, 'USD', 'SERVICE', '2022-12-14 19:00:24.000000', 1000.00, 1);
insert into expense_limit (id, currency, expense_category, limit_date_time, limit_sum, account_info_id)
values (2, 'USD', 'PRODUCT', '2022-12-14 19:00:24.000000', 1000.00, 1);