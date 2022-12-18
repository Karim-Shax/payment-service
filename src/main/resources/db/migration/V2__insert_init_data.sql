insert into account_info (id, account)
values (1, '87770981997');

insert into expense_limits (id, limit_currency_shortname, expense_category, limit_date_time, limit_sum, limit_temporary,
                            account_info_id)
values (1, 'USD', 'SERVICE', '2022-12-14 19:00:24+06', 1000.00, 1000.00, 1),
       (2, 'USD', 'PRODUCT', '2022-12-14 19:00:24+06', 1000.05, 1000.00, 1);