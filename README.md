# Hello Transfer

A simple application to simulate money transfer between 2 accounts.


## Build the app

```sh
mvn clean package
```


## Running the server

```sh
java -jar target\hello-transfer-1.0-SNAPSHOT.jar 8989
```


## Testing

1. Create 2 accounts
   ```sh
   curl -v http://localhost:8989/accounts -X POST -d '{"accountHolder":"Ichsan", "balance":100}' -H "Content-Type: application/json"
   curl -v http://localhost:8989/accounts -X POST -d '{"accountHolder":"John", "balance":50}' -H "Content-Type: application/json"
   ```
    
2. Transfer money:
   ```sh
   curl -v http://localhost:8989/accounts/xxxx-xxxx-xxxx-xxxx/transfer -X POST -d '{"beneficiary":"xxxx-xxxx-xxxx-xxxx", "amount":40}' -H "Content-Type: application/json"
   ```
    
3. Check balance:
   ```sh
   curl -v http://localhost:8989/accounts/xxxx-xxxx-xxxx-xxxx
   ```
