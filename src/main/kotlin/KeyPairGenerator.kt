import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Key
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.ECGenParameterSpec
import java.util.*

fun generateKeyPair(): KeyPair {
    val keyPairGenerator = KeyPairGenerator
        .getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME)
        .apply { initialize(ECGenParameterSpec("prime256v1"), RANDOM_INSTANCE) }

    return keyPairGenerator.generateKeyPair()
}

private object Mappers {
    fun bytesToBase64(bytes: ByteArray): String = Base64.getEncoder().encodeToString(bytes)
    fun keyToBase64(key: Key): String = bytesToBase64(key.encoded)
}