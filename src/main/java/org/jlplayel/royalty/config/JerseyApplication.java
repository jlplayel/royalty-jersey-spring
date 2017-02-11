package org.jlplayel.royalty.config;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyApplication extends ResourceConfig {
    public JerseyApplication() {
        packages("org.jlplayel.royalty.controller;org.jlplayel.royalty.errorHandler");
    }
}
