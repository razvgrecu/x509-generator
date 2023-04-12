package eu.stephanson.external.iot.properties;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("crypto")
public class CryptoConfigurationProperties {
    private String outputFolder;
    private String ellipticCurve;
    private Integer expirationYear;

    public CryptoConfigurationProperties() {
    }

    public CryptoConfigurationProperties(String outputFolder, String ellipticCurve, Integer expirationYear) {
        this.outputFolder = outputFolder;
        this.ellipticCurve = ellipticCurve;
        this.expirationYear = expirationYear;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public CryptoConfigurationProperties setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
        return this;
    }

    public String getEllipticCurve() {
        return ellipticCurve;
    }

    public CryptoConfigurationProperties setEllipticCurve(String ellipticCurve) {
        this.ellipticCurve = ellipticCurve;
        return this;
    }

    public Integer getExpirationYear() {
        return expirationYear;
    }

    public CryptoConfigurationProperties setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }
}
