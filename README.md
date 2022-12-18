# payment-service
Прототип микросервиса, без разграничений доступа к API, который
будет интегрирован в существующую банковскую систему. Микросервис должен:
1. Получать информацию о каждой расходной операции в тенге (KZT) или рублях
   (RUB) в реальном времени и сохранять ее в своей собственной базе данных (БД);
2. Хранить месячный лимит по расходам в долларах США (USD) раздельно для двух
   категорий расходов: товаров и услуг. Если не установлен, принимать лимит равным
   0;
3. Запрашивать данные биржевых курсов валютной пары KZT/USD по дневному
   интервалу (1day/daily) и хранить их в собственной базе данных. При расчете курсов
   использовать данные закрытия (close). В случае, если таковые недоступны на
   текущий день (это может быть текущий день, в рамках которого идут торги, или
   выходной день), то использовать данные последнего закрытия (previous_close);
4. Помечать транзакции, превысившие месячный лимит операций (технический флаг
   limit_exceeded);
5. Дать возможность клиенту установить новый лимит. При установлении нового
   лимита микросервисом автоматически выставляется текущая дата, не позволяя
   выставить ее в прошедшем или будущем времени;
6. По запросу клиента возвращать список транзакций, превысивших лимит, с
   указанием лимита, который был превышен (дата установления, сумма лимита,
   валюта (USD)).

Чтобы запустить нужно указать переменные окружения и заполнить:
* PG_db= ; PG_password= ; PG_username= ; PG_PORT= ; PG_SERVER=
* scheduler.enabled планирование задач 
* rate.api.enabled отвечает за результат метода для отправки запросов на внешний сервис 
* Для запуска тестов нужна отдельная бд, нужно указать в application-test.properties


   
 





