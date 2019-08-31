package id.web.michsan.hellotransfer.web;

import id.web.michsan.hellotransfer.command.AccountCommandHandler;
import id.web.michsan.hellotransfer.command.MoneyTransferCommand;
import id.web.michsan.hellotransfer.query.AccountQueryHandler;
import id.web.michsan.hellotransfer.query.BalanceDisplay;
import lombok.Setter;

import javax.ws.rs.*;
import java.math.BigDecimal;

@Path("/accounts")
public class AccountController {

    @Setter
    private AccountCommandHandler accountCommandHandler;
    @Setter
    private AccountQueryHandler accountQueryHandler;

    // Transfer money then give a tracking number
    @POST
    @Path("{accountNo}/transfer")
    public String transferMoney(@PathParam("accountNo") String sourceAccountNo,
                                @FormParam("tacc") String targetAccountNo, @FormParam("amt") BigDecimal amount) {
        MoneyTransferCommand command = new MoneyTransferCommand(sourceAccountNo, targetAccountNo, amount);
        accountCommandHandler.handle(command);
        return null;
    }

    @GET
    @Path("{accountNo}/balance")
    public BalanceDisplay checkBalance(@PathParam("accountNo") String accountNo) {
        return accountQueryHandler.showBalance(accountNo);
    }
}
