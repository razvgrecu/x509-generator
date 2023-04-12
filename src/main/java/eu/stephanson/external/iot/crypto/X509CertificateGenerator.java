package eu.stephanson.external.iot.crypto;

import eu.stephanson.external.iot.crypto.domain.GeneratedCertificate;
import eu.stephanson.external.iot.properties.CryptoConfigurationProperties;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.lingala.zip4j.ZipFile;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@Singleton
public class X509CertificateGenerator {
    private final CryptoConfigurationProperties configurationProperties;

    @Inject
    public X509CertificateGenerator(final CryptoConfigurationProperties cryptoConfigurationProperties) {
        this.configurationProperties = cryptoConfigurationProperties;
    }

    @NotNull
    public GeneratedCertificate generate(
            final String commonName,
            final KeyPair keys
    ) throws
            NoSuchAlgorithmException,
            IOException,
            OperatorCreationException,
            CertificateException {
        final X500Name subject = new X500NameBuilder(BCStyle.INSTANCE)
                .addRDN(BCStyle.CN, commonName)
                .build();

        final BigInteger serial = new BigInteger(160, SecureRandom.getInstance("SHA1PRNG"));

        final JcaX509v3CertificateBuilder certificate = new JcaX509v3CertificateBuilder(
                subject,
                serial,
                Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant()),
                Date.from(LocalDate.of(configurationProperties.getExpirationYear(), 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant()),
                subject,
                keys.getPublic()
        );
        certificate
                .addExtension(Extension.extendedKeyUsage, false, "clientAuth".getBytes(StandardCharsets.UTF_8));
        final ContentSigner signer = new JcaContentSignerBuilder("SHA256withECDSA").build(
                keys.getPrivate()
        );

        final X509CertificateHolder holder = certificate.build(signer);

        final JcaX509CertificateConverter converter = new JcaX509CertificateConverter()
                .setProvider(new BouncyCastleProvider());

        final X509Certificate x509Certificate = converter.getCertificate(holder);
        final var certificateWriter = new StringWriter();
        final var privateKeyWriter = new StringWriter();
        final var certificateContent = new StringBuilder();
        final var privateKeyContent = new StringBuilder();
        final var zipFile = new ZipFile(configurationProperties.getOutputFolder() + "/contents.zip");


        try (final var certificatePemWriter = new JcaPEMWriter(certificateWriter)) {
            certificatePemWriter.writeObject(x509Certificate);
            final var content = certificateWriter.toString();
            final var path = Files.writeString(Paths.get(configurationProperties.getOutputFolder(), "cert.pem"), content);
            zipFile.addFile(path.toFile());
            certificateContent.append(content);
        } catch (Exception exception) {
            zipFile.close();
            // do nada momentarily
        }

        try (final var privateKeyPemWriter = new JcaPEMWriter(privateKeyWriter)) {
            privateKeyPemWriter.writeObject(keys.getPrivate());
            final var content = privateKeyWriter.toString();
            final var path = Files.writeString(Paths.get(configurationProperties.getOutputFolder(), "key.pem"), content);
            zipFile.addFile(path.toFile());
            privateKeyContent.append(content);
        } catch (Exception exception) {
            zipFile.close();
            // do nada momentarily
        }

        zipFile.close();

        return new GeneratedCertificate(
                certificateContent.toString(),
                privateKeyContent.toString(),
                zipFile
        );
    }

}
