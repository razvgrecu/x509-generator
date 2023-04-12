package eu.stephanson.external.iot.properties;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("azure")
public class AzureConfigurationProperties {
    private String hubHost;
    private String connection;

    public AzureConfigurationProperties() {
    }

    public AzureConfigurationProperties(final String hubHost, final String connection) {
        this.hubHost = hubHost;
        this.connection = connection;
    }

    public String hubHost() {
        return hubHost;
    }

    public String connection() {
        return connection;
    }

    public AzureConfigurationProperties setHubHost(String hubHost) {
        this.hubHost = hubHost;
        return this;
    }

    public AzureConfigurationProperties setConnection(String connection) {
        this.connection = connection;
        return this;
    }
}
