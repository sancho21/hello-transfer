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
        int port = 8989;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        JerseyConfiguration configuration = JerseyConfiguration.builder()
                .addPackage(AccountController.class.getPackage().getName())
                .addPort(port)
                .build();

        List<com.google.inject.Module> modules = new ArrayList<>();
        modules.add(new JerseyModule(configuration));
        modules.add(new AccountModule());

        Guice.createInjector(modules)
                .getInstance(JerseyServer.class).start();
    }

}
