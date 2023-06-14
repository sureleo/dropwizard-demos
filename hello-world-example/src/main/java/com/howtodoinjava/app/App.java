package com.howtodoinjava.app;

import com.howtodoinjava.app.config.ApplicationConfiguration;
import com.howtodoinjava.app.config.ApplicationHealthCheck;
import com.howtodoinjava.app.logger.LoggingRequestFilter;
import com.howtodoinjava.app.repository.EmployeeRepository;
import com.howtodoinjava.app.web.APIController;
import com.howtodoinjava.app.web.EmployeeController;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import jakarta.ws.rs.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application<ApplicationConfiguration> {

  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

  @Override
  public void initialize(Bootstrap<ApplicationConfiguration> b) {
  }

  @Override
  public void run(ApplicationConfiguration c, Environment e) {

    LOGGER.info("Registering Jersey Client");
    final Client client = new JerseyClientBuilder(e)
        .using(c.getJerseyClientConfiguration())
        .build(getName());
    e.jersey().register(new APIController(client));

    LOGGER.info("Registering REST resources");
    e.jersey().register(new EmployeeController(e.getValidator(), new EmployeeRepository()));

    LOGGER.info("Registering Application Health Check");
    e.healthChecks().register("application", new ApplicationHealthCheck(client));

    e.jersey().register(new LoggingRequestFilter());
  }

  public static void main(String[] args) throws Exception {
    new App().run(args);
  }
}
