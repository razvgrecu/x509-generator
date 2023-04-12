package eu.stephanson.external.iot.controller

import com.microsoft.azure.sdk.iot.provisioning.service.configs.ProvisioningStatus

data class CreateCertificateRequest(val commonName: String)
data class CreateCertificateResponse(
    val certificateContent: String,
    val privateKeyContent: String
)

data class CreateCertificateAndEnrollmentResponse(
    val certificateContent: String,
    val deviceId: String,
    val registrationId: String,
    val deviceProvisioningStatus: ProvisioningStatus,
    val privateKeyContent: String,
)

data class CreateEnrollmentRequest(
    val certificateContent: String,
    val privateKeyContent: String,
    val commonName: String
)

typealias CreateEnrollmentResponse = CreateCertificateAndEnrollmentResponse
