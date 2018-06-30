package in.clouthink.daas.security.token.sample.spi.impl;

import in.clouthink.daas.security.token.core.Role;

public class SampleRole implements Role {

    private String name;

    public SampleRole() {
    }

    public SampleRole(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
