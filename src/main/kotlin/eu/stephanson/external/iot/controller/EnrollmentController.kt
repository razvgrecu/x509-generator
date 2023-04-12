package eu.stephanson.external.iot.controller

import eu.stephanson.external.iot.azure.AzureEnrollmentService
import eu.stephanson.external.iot.crypto.EllipticCurveKeyGenerator
import eu.stephanson.external.iot.crypto.X509CertificateGenerator
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jakarta.inject.Singleton

@Controller(
    value = "/api/v1/certificate",
    consumes = ["application/json"],
    produces = ["application/json"],
)
@Singleton
class EnrollmentController(
    private val azureEnrollmentService: AzureEnrollmentService,
    private val x509CertificateGenerator: X509CertificateGenerator,
    private val ellipticCurveKeyGenerator: EllipticCurveKeyGenerator,
) {
    @Post
    fun create(@Body request: CreateCertificateRequest): CreateCertificateResponse {
        val keyPair = this.ellipticCurveKeyGenerator.generate()
        return x509CertificateGenerator.generate(request.commonName, keyPair)
            .orEmpty()
            .let(::CreateCertificateResponse)
    }

    @Post("/enroll")
    fun createAndEnroll(@Body request: CreateCertificateRequest): CreateCertificateAndEnrollmentResponse {
        val keyPair = this.ellipticCurveKeyGenerator.generate()
        val certificate = this.x509CertificateGenerator.generate(request.commonName, keyPair).orEmpty()
        val enrollment = azureEnrollmentService.enroll(request.commonName, certificate)
        return CreateCertificateAndEnrollmentResponse(
            certificateContent = certificate,
            deviceId = enrollment.deviceId,
            registrationId = enrollment.registrationId,
            deviceProisioningStatus = enrollment.provisioningStatus
        )

    }
}
