insert into account_info (id, account)
values (1, '12345678910');

insert into account_info (id, account)
values (2, '99999999999');

insert into expense_limits (id, limit_currency_shortname, expense_category, limit_date_time, limit_sum, limit_temporary,
                            account_info_id)
values (1, 'USD', 'SERVICE', '2022-12-11 10:00:24.000000', 2000.00, 2000.00, 1),
       (2, 'USD', 'PRODUCT', '2022-12-11 11:00:24.000000', 1000.00, 1000.00, 1),
       (3, 'USD', 'PRODUCT', '2022-12-12 11:00:24.000000', 3000.00, 3000.00, 2);


insert into payment_transactions (id, account_to, currency_short_name, date_time, expense_category, limit_exceeded,
                                  transaction_sum, account_info_id)
values (1, '1111111111', 'KZT', '2022-12-14 10:00:24.000000', 'PRODUCT', 0, 25000.00, 1),
       (2, '2222222222', 'RUB', '2022-12-14 10:10:24.000000', 'SERVICE', 1, 23000.00, 1),
       (3, '3333333333', 'KZT', '2022-12-14 10:10:24.000000', 'PRODUCT', 1, 25000.00, 1),
       (4, '4444444444', 'RUB', '2022-12-14 10:11:24.000000', 'SERVICE', 0, 25000.00, 1),
       (5, '5555555555', 'KZT', '2022-12-14 10:12:24.000000', 'PRODUCT', 0, 25000.00, 2);