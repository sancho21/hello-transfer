package id.web.michsan.hellotransfer.command;

public class UnknownAccountException extends RuntimeException {
    public UnknownAccountException(String accountNo) {
        super("Unknown account: " + accountNo);
    }
}
