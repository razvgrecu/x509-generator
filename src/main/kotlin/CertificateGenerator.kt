import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x500.X500NameBuilder
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.asn1.x509.*
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.jcajce.JcaPEMWriter
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.io.IOException
import java.io.StringWriter
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Path
import java.security.KeyPair
import java.security.cert.X509Certificate
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*


@Throws(IOException::class)
fun generateCert(ownerName: String, keyPair: KeyPair): X509Certificate {

    val subject: X500Name = X500NameBuilder(BCStyle.INSTANCE)
        .addRDN(BCStyle.CN, ownerName)
        .build();

    val serial = BigInteger(160, RANDOM_INSTANCE);


    val certificate = JcaX509v3CertificateBuilder(
        subject,
        serial,
        Date.from(LocalDate.of(2020, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant()),
        Date.from(LocalDate.of(2050, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant()),
        subject,
        keyPair.public
    ).apply {
        addExtension(Extension.subjectKeyIdentifier, false, "dev".encodeToByteArray())
        addExtension(Extension.extendedKeyUsage, false, "clientAuth".encodeToByteArray())
    }

    val signer = JcaContentSignerBuilder("SHA256withECDSA").build(keyPair.private)
    val holder = certificate.build(signer)

    val converter = JcaX509CertificateConverter().apply { setProvider(BouncyCastleProvider()) }
    val x509 = converter.getCertificate(holder)

    val stringWriterCert = StringWriter()
    val stringWriterKey = StringWriter()

    JcaPEMWriter(stringWriterCert)
        .also { it.writeObject(x509) }
        .also { it.close() }
        .also { println("Certificate intro") }
        .also { Files.writeString(Path.of("", "cert.pem").toAbsolutePath(), stringWriterCert.toString())}

    JcaPEMWriter(stringWriterKey)
        .also { it.writeObject(keyPair.private) }
        .also { it.close() }
        .also { println("Key intro") }
        .also { Files.writeString(Path.of("", "key.pem").toAbsolutePath(), stringWriterKey.toString())}



    return x509
}