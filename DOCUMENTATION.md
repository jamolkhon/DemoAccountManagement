# Running
`baseUrl` parameter in AppModule should be replaced with a working Backend endpoint url for this project. For example: http://192.168.0.1:8080/ (with trailing slash)

# Observations/Assumptions
Repeated `update` requests with the same `Transaction-Id` are accepted (even with altered `account_id` and `amount` values) but have no effect on the account balance.

There are no requirements on minimum and maximum balance.

# Offline support
Both successful and failed `update` requests are saved in the local database. Failed requests can be retried manually in any order and local database is updated accordingly based on the result of the request.
