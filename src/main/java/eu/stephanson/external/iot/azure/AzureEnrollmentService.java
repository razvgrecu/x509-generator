package eu.stephanson.external.iot.azure;

import com.microsoft.azure.sdk.iot.provisioning.service.ProvisioningServiceClient;
import com.microsoft.azure.sdk.iot.provisioning.service.configs.IndividualEnrollment;
import com.microsoft.azure.sdk.iot.provisioning.service.configs.ProvisioningStatus;
import com.microsoft.azure.sdk.iot.provisioning.service.configs.X509Attestation;
import com.microsoft.azure.sdk.iot.provisioning.service.exceptions.ProvisioningServiceClientException;
import eu.stephanson.external.iot.properties.AzureConfigurationProperties;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class AzureEnrollmentService {
    private final ProvisioningServiceClient serviceClient;
    private final AzureConfigurationProperties configurationProperties;

    @Inject
    public AzureEnrollmentService(
            final ProvisioningServiceClient provisioningServiceClient,
            final AzureConfigurationProperties azureConfigurationProperties
    ) {
        this.serviceClient = provisioningServiceClient;
        this.configurationProperties = azureConfigurationProperties;
    }

    public IndividualEnrollment enroll(final String id, final String certificateContent) throws ProvisioningServiceClientException {
        final X509Attestation attestation = X509Attestation.createFromClientCertificates(certificateContent);
        final IndividualEnrollment enrollment = new IndividualEnrollment(id, attestation);
        enrollment.setIotHubHostName(configurationProperties.hubHost());
        enrollment.setProvisioningStatus(ProvisioningStatus.ENABLED);
        return serviceClient.createOrUpdateIndividualEnrollment(enrollment);
    }
}
