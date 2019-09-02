package id.web.michsan.hellotransfer;

import io.logz.guice.jersey.JerseyServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static id.web.michsan.hellotransfer.HttpUtils.readResponse;
import static id.web.michsan.hellotransfer.HttpUtils.writeRequestBody;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Functional tests
 */
public class MoneyTransferFuncTest {

    private static final int PORT = 8787;
    private static final String BASE_URL = "http://localhost:" + PORT;
    private static JerseyServer server;

    @BeforeAll
    public static void beforeClass() throws Exception {
        server = Application.createJerseyServer(PORT);
        server.start();
    }

    @AfterAll
    public static void afterClass() throws Exception {
        server.stop();
    }

    @Test
    @DisplayName("Should be able to transfer money when source balance is sufficient")
    public void transferMoney_sufficientBalance() throws Exception {
        // Given that source account has enough balance for money transfer
        String acc01 = createAccountWithBalance("Mr Foo", 50);
        String acc02 = createAccountWithBalance("Mr Bar", 70);

        // When transferring the money
        HttpResponse response = transferMoney(acc01, 20, acc02);

        // Then it should be successful (no exception thrown)
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should fail transferring money when source balance is insufficient")
    public void transferMoney_insufficientBalance() throws Exception {
        // Given that source account has enough balance for money transfer
        String acc01 = createAccountWithBalance("Mr Foo", 50);
        String acc02 = createAccountWithBalance("Mr Bar", 70);

        // When transferring the money
        HttpResponse response = transferMoney(acc01, 70, acc02);

        // Then it should fail
        assertThat(response.getStatus()).isEqualTo(400);
    }

    private String createAccountWithBalance(String holder, double balance) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL + "/accounts").openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        String requestBody = String.format("{\"accountHolder\":\"%s\", \"balance\":%f}", holder, balance);
        writeRequestBody(con, requestBody);

        HttpResponse response = readResponse(con);
        if (response.getStatus() != 201) {
            throw new IllegalStateException("Failed to create account " + holder + " caused by " + response.getResponseBody());
        }

        String newResourceUrl = response.getHeaderFields().get("Location").get(0);
        String accountNo = newResourceUrl.substring(newResourceUrl.lastIndexOf('/') + 1);
        return accountNo;
    }

    private HttpResponse transferMoney(String sourceAccount, double amount, String beneficiaryAccount) throws Exception {
        HttpURLConnection con =
                (HttpURLConnection) new URL(BASE_URL + "/accounts/" + sourceAccount + "/transfer").openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        String requestBody = String.format("{\"beneficiary\":\"%s\", \"amount\":%f}", beneficiaryAccount, amount);
        writeRequestBody(con, requestBody);

        return readResponse(con);
    }

}
