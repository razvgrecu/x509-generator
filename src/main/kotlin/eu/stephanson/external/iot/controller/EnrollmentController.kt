package eu.stephanson.external.iot.controller

import eu.stephanson.external.iot.azure.AzureEnrollmentService
import eu.stephanson.external.iot.crypto.EllipticCurveKeyGenerator
import eu.stephanson.external.iot.crypto.X509CertificateGenerator
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.server.types.files.SystemFile
import jakarta.inject.Singleton

@Controller(
    value = "/api",
    consumes = ["application/json"],
)
@Singleton
class EnrollmentController(
    private val azureEnrollmentService: AzureEnrollmentService,
    private val x509CertificateGenerator: X509CertificateGenerator,
    private val ellipticCurveKeyGenerator: EllipticCurveKeyGenerator,
) {

    @Post("/v1/certificate")
    fun create(@Body request: CreateCertificateRequest): CreateCertificateResponse {
        val keyPair = this.ellipticCurveKeyGenerator.generate()
        val generatedCertificate = x509CertificateGenerator.generate(request.commonName, keyPair)
        return CreateCertificateResponse(
            certificateContent = generatedCertificate.certificate(),
            privateKeyContent = generatedCertificate.privateKey()
        )
    }

    @Post("/v2/certificate")
    fun downloadCertificateAndPrivateKey(@Body request: CreateCertificateRequest): SystemFile {
        val keys = ellipticCurveKeyGenerator.generate();
        val certificate = x509CertificateGenerator.generate(request.commonName, keys);
        return SystemFile(certificate.zip()!!.file)
    }

    @Post("/v1/enrollment")
    fun createAndEnroll(@Body request: CreateCertificateRequest): CreateCertificateAndEnrollmentResponse {
        val keyPair = this.ellipticCurveKeyGenerator.generate()
        val certificate = this.x509CertificateGenerator.generate(request.commonName, keyPair)
        val enrollment = azureEnrollmentService.enroll(request.commonName, certificate.certificate())
        return CreateCertificateAndEnrollmentResponse(
            certificateContent = certificate.certificate(),
            deviceId = enrollment.deviceId,
            registrationId = enrollment.registrationId,
            deviceProvisioningStatus = enrollment.provisioningStatus,
            privateKeyContent = certificate.privateKey()
        )

    }

    @Post("/v2/enrollment")
    fun enroll(@Body request: CreateEnrollmentRequest): CreateEnrollmentResponse {
        val enrollment = azureEnrollmentService.enroll(request.commonName, request.certificateContent)
        return CreateEnrollmentResponse(
            certificateContent = request.certificateContent,
            privateKeyContent = request.privateKeyContent,
            deviceId = enrollment.deviceId,
            deviceProvisioningStatus = enrollment.provisioningStatus,
            registrationId = enrollment.registrationId
        )
    }

}
