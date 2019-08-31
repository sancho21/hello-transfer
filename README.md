## Running the server

Run id.web.michsan.hellotransfer.Application 


## Testing

1. Create 2 accounts
   ```
   curl -v http://localhost:8989/accounts -X POST -d '{"accountHolder":"Ichsan", "balance":100}' -H "Content-Type: application/json"
   curl -v http://localhost:8989/accounts -X POST -d '{"accountHolder":"John", "balance":50}' -H "Content-Type: application/json"
   ```
    
2. Transfer money:
   ```
   curl -v http://localhost:8989/accounts/xxxx-xxxx-xxxx-xxxx/transfer -X POST -d '{"beneficiary":"xxxx-xxxx-xxxx-xxxx", "amount":40}' -H "Content-Type: application/json"
   ```
    
3. Check balance:
   ```
   curl -v http://localhost:8989/accounts/xxxx-xxxx-xxxx-xxxx
   ```
