package eu.stephanson.external.iot.controller

import com.microsoft.azure.sdk.iot.provisioning.service.configs.ProvisioningStatus

data class CreateCertificateRequest(val commonName: String)
data class CreateCertificateResponse(val content: String)

data class CreateCertificateAndEnrollmentResponse(
    val certificateContent: String,
    val deviceId: String,
    val registrationId: String,
    val deviceProisioningStatus: ProvisioningStatus

)
