package org.jlplayel.royalty.config;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = SpringConfiguration.class)
public abstract class JerseySpringTest
{
    private JerseyTest jerseyTest;

    public final WebTarget target(final String path)
    {
        return jerseyTest.target(path);
    }

    @Before
    public void setup() throws Exception
    {
        jerseyTest.setUp();
    }

    @After
    public void tearDown() throws Exception
    {
        jerseyTest.tearDown();
    }

    @Autowired
    public void setApplicationContext(final ApplicationContext context)
    {
        jerseyTest = new JerseyTest()
        {
            @Override
            protected Application configure()
            {
                ResourceConfig application = JerseySpringTest.this.configure();
                application.property("contextConfig", context);

                return application;
            }
        };
    }

    protected abstract ResourceConfig configure();
}
