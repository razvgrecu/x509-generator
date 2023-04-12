package eu.stephanson.external.iot.azure;

import com.microsoft.azure.sdk.iot.provisioning.service.ProvisioningServiceClient;
import eu.stephanson.external.iot.properties.AzureConfigurationProperties;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Factory
public class ProvisioningServiceClientFactory {
    private final AzureConfigurationProperties configurationProperties;
    @Inject
    ProvisioningServiceClientFactory(final AzureConfigurationProperties azureConfigurationProperties) {
        this.configurationProperties = azureConfigurationProperties;
    }

    @Bean
    @Singleton
    public ProvisioningServiceClient provisioningServiceClient() {
        return new ProvisioningServiceClient(configurationProperties.connection());
    }
}
