package id.web.michsan.hellotransfer.web;

import id.web.michsan.hellotransfer.command.*;
import id.web.michsan.hellotransfer.model.Account;
import id.web.michsan.hellotransfer.query.AccountQueryHandler;
import id.web.michsan.hellotransfer.util.jdbc.IncorrectResultSizeException;
import lombok.Setter;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.net.URI;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountController {

    @Setter
    @Inject
    private AccountCommandHandler accountCommandHandler;
    @Setter
    @Inject
    private AccountQueryHandler accountQueryHandler;

    @POST
    public Response createAccount(CreateAccountRequest request) {
        CreateAccountCommand command = new CreateAccountCommand(request.accountHolder, request.balance);
        Account result = accountCommandHandler.handle(command);
        return Response.created(URI.create("/accounts/" + result.getNumber())).entity(result).build();
    }

    // Transfer money then give a tracking number
    @POST
    @Path("{accountNo}/transfer")
    public Response transferMoney(@PathParam("accountNo") String sourceAccountNo, MoneyTransferRequest request) {
        MoneyTransferCommand command = new MoneyTransferCommand(sourceAccountNo, request.beneficiary, request.amount);
        try {
            accountCommandHandler.handle(command);
            return Response.ok().build();
        } catch (InsufficientBalanceException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Source balance is not sufficient to do transfer").build();
        } catch (AccountNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Account no " + e.getAccountNumber() + " not found").build();
        }
    }

    @GET
    @Path("{accountNo}")
    public Response checkBalance(@PathParam("accountNo") String accountNo) {
        try {
            return Response.ok(accountQueryHandler.showAccountSummary(accountNo)).build();
        } catch (IncorrectResultSizeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Account no " + accountNo + " not found").build();
        }
    }

    private static class CreateAccountRequest {
        @Setter
        private String accountHolder;
        @Setter
        private BigDecimal balance;
    }

    private static class MoneyTransferRequest {
        @Setter
        private String beneficiary;
        @Setter
        private BigDecimal amount;
    }
}
