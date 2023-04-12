package eu.stephanson.external.iot.crypto.domain;

import net.lingala.zip4j.ZipFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeneratedCertificate {
    @NotNull
    private final String certificateContent;
    @NotNull
    private final String privateKeyContent;

    @Nullable
    private final ZipFile zip;

    public GeneratedCertificate(
            @NotNull String certificateContent,
            @NotNull String privateKeyContent,
            @Nullable ZipFile zip
    ) {
        this.certificateContent = certificateContent;
        this.privateKeyContent = privateKeyContent;
        this.zip = zip;
    }

    @Nullable
    public ZipFile zip() {
        return zip;
    }

    @NotNull
    public String certificate() {
        return certificateContent;
    }

    @NotNull
    public String privateKey() {
        return privateKeyContent;
    }
}
