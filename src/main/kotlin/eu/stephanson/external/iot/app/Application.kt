package eu.stephanson.external.iot.app

import io.micronaut.runtime.Micronaut.run
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

fun main(args: Array<String>) {
    Security.addProvider(BouncyCastleProvider())
    run(*args)
}

