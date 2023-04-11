import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

fun main(args: Array<String>) {
    Security.addProvider(BouncyCastleProvider())
    generateCert(args.first(), generateKeyPair())
}