package eu.stephanson.external.iot.crypto;

import eu.stephanson.external.iot.properties.CryptoConfigurationProperties;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

@Singleton
public class EllipticCurveKeyGenerator {

    private static final String ALGORITHM = "ECDSA";
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";

    private final CryptoConfigurationProperties configurationProperties;

    @Inject
    public EllipticCurveKeyGenerator(final CryptoConfigurationProperties properties) {
        this.configurationProperties = properties;
    }

    public KeyPair generate() throws
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            NoSuchProviderException {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance(
                ALGORITHM,
                BouncyCastleProvider.PROVIDER_NAME
        );

        generator.initialize(
                new ECGenParameterSpec(configurationProperties.getEllipticCurve()),
                SecureRandom.getInstance(RANDOM_ALGORITHM)
        );

        return generator.generateKeyPair();
    }
}
