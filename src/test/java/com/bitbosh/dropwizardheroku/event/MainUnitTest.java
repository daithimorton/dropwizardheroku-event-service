package com.bitbosh.dropwizardheroku.event;

import static org.junit.Assert.assertTrue;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import com.bitbosh.dropwizardheroku.event.ApplicationConfiguration;
import com.bitbosh.dropwizardheroku.event.Main;
import com.bitbosh.dropwizardheroku.event.api.EventResource;

import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Environment;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class MainUnitTest {

  @Test
  public void main_verifyMainCall_IfApplicationStarted() throws Exception {
    String expected = "test";

    // Mock the ctor for DropwizardHerokuApplication super class Application<DropwizardHerokuConfiguration>
    new MockUp<Application<ApplicationConfiguration>>() {

      @Mock
      public void $init() {

      }
    };

    // Mock the ctor for DropwizardHerokuApplication
    new MockUp<Main>() {

      @Mock
      public void run(String... args) {
        String actual = args[0];

        // Assert args contains expected when called from main()
        assertTrue(expected.equals(actual));
      }

    };

    Main.main(new String[] { expected });
  }

  @Test
  public void run_verifyRunCall_IfApplicationStarted(@Mocked ApplicationConfiguration configuration,
      @Mocked Environment environment, @Mocked JerseyEnvironment jerseyEnv, @Mocked ServletEnvironment servletEnv, @Mocked FilterRegistration.Dynamic dynFilter) throws Exception {

    // Mock the ctor for DropwizardHerokuApplication super class Application<DropwizardHerokuConfiguration>
    new MockUp<Application<ApplicationConfiguration>>() {

      @Mock
      public void $init() {
      }
    };

    new MockUp<JerseyEnvironment>() {

      @Mock
      public void register(Object component) {
      }
    };

    new MockUp<Environment>() {
      @Mock
      public JerseyEnvironment jersey() {
        return jerseyEnv;
      }
      
      @Mock
      public ServletEnvironment servlets() {
    	  return servletEnv;
      }
    };
    
    new MockUp<ServletEnvironment>() {        
        @Mock
        public FilterRegistration.Dynamic addFilter(String name, Class<? extends Filter> klass) {
			return dynFilter;        
        }
      };

    new MockUp<DBIFactory>() {

      @Mock
      public DBI build(Environment environment, PooledDataSourceFactory configuration, String name) {
        return null;
      }
    };

    new MockUp<EventResource>() {
      @Mock
      public void $init(DBI jdbi) {
      }
    };

    Main app = new Main();
    app.run(configuration, environment);
  }
}
