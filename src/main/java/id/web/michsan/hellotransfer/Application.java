package id.web.michsan.hellotransfer;

import com.google.inject.Guice;
import id.web.michsan.hellotransfer.web.AccountController;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.JerseyServer;
import io.logz.guice.jersey.configuration.JerseyConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Application {

    public static void main(String... args) throws Exception {
        JerseyConfiguration configuration = JerseyConfiguration.builder()
                .addPackage(AccountController.class.getPackage().getName())
                .addPort(8989)
                .build();

        List<com.google.inject.Module> modules = new ArrayList<>();
        modules.add(new JerseyModule(configuration));
        modules.add(new AccountModule());

        Guice.createInjector(modules)
                .getInstance(JerseyServer.class).start();
    }

    /*
    To test:
    0. Create 2 accounts
        curl -v http://localhost:8989/accounts -X POST -d '{"accountHolder":"Ichsan", "balance":100}' -H "Content-Type: application/json"
        curl -v http://localhost:8989/accounts -X POST -d '{"accountHolder":"John", "balance":50}' -H "Content-Type: application/json"

    1. Transfer money:
        curl -v http://localhost:8989/accounts/xxxx-xxxx-xxxx-xxxx/transfer -X POST -d '{"beneficiary":"xxxx-xxxx-xxxx-xxxx", "amount":40}' -H "Content-Type: application/json"

    2. Check balance:
        curl -v http://localhost:8989/accounts/xxxx-xxxx-xxxx-xxxx

    */
}
