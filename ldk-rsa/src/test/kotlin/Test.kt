/*
 * Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.lgou2w.ldk.rsa.RSASecurity
import com.lgou2w.ldk.rsa.RSAUtils
import org.junit.Ignore
import org.junit.Test
import java.io.File
import java.util.*

class Test {

    @Test
    @Ignore // ignore
    fun test_rsa() {

        val dir = File("../key")
        val privateKeyFile = File(dir, "pkcs8_private.key")
        val publicKeyFile = File(dir, "public.key")

        val privateKey = RSAUtils.decodePrivateKey(privateKeyFile.readBytes())
        val publicKey = RSAUtils.decodePublicKey(publicKeyFile.readBytes())

        val encrypted = RSAUtils.encrypt(privateKey, "Hi, I'm server.".toByteArray())
        println("Encrypted: ${encrypted.toString(Charsets.UTF_8)}")
        val decrypted = RSAUtils.decrypt(publicKey, encrypted)
        println("Decrypted: " + decrypted.toString(Charsets.UTF_8))
    }

    @Test
    @Ignore // ignore
    fun test_rsa_sign() {

        val desktop = File("../desktop")
        val dir = File(desktop,  "key/")

        val publicKeyFile = File(dir, "public.key")
        val publicKey = RSAUtils.decodePublicKey(publicKeyFile.readBytes())

        val software = File(desktop, "software.exe")
        val signature = File(desktop, "software.exe.signature")
        val result = RSAUtils.verify(publicKey, software, signature)

        println("Verify: $result")
    }

    @Test
    fun testRSASecurity() {
        fun base64(data: ByteArray) = Base64.getEncoder().encodeToString(data)
        val rsaPair = RSASecurity.fromKeyPairGenerator(RSASecurity.BIT_2048, RSASecurity.SIGNATURE_SHA1)
        val rsaPrivate = rsaPair.first
        val rsaPublic = rsaPair.second

        // Test Encrypt and Decrypt
        val msg = "Hello World!".toByteArray(Charsets.UTF_8)
        val encrypted = rsaPrivate.encrypt(msg) // private encrypt
        println("Private Encrypt: ${base64(encrypted)}")
        val decrypted = rsaPublic.decrypt(encrypted) // public decrypt
        println("Public Decrypt: ${String(decrypted, Charsets.UTF_8)}")

        // Test Signature and Verify
        val signature = rsaPrivate.signature(msg) // private signature
        println("Private Signature: ${base64(signature)}")
        val verify = rsaPublic.verify(msg, signature) // public verify
        println("Public Verify: $verify")
    }

    @Test
    fun testRSASecurity2() {
        // test key, don't used
        val pkcs8NotEncryptedBase64EncodedPrivateKey = """
            -----BEGIN PRIVATE KEY-----
            MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDqP6/ytjWfPwOc
            B9Bzgg6tm2LQoj4gj2YNP8rFnAABYe4m2CdUJIuwkHR631YgVH7CSKofjNwLE2Bx
            S6zN1foeeYuZCouQ2udyFpXNwJAGrj39zVS/AyHmZbZWLAXL3BKJ+UHyJ9VH7/Wp
            tIU/S04pquhcJ2UVsobeJtpEMRTiGBvPJZmU5FqUnmJR6g8TQRiTlJalKHo7rU6N
            CQeNdly7Guor89JXjZ/PdNzB03ltLlY7CoWl3JsTghnvnJ3amwxnbLiKpUwvmWz0
            1RIJogjinLulq9iOwR6xjrQ+OXyOugkcFR/bmLSQo5EUuVe1x7/k8C42C1T6drSX
            MFvW9gghAgMBAAECggEBAIEs1a4WZMOIUEk+1eick9OQmJFMps31xeUQhJUsTaR1
            iIrLMATqkk2vm0wW1LvHXI9u2sHDP6Sr7scLYCOyF9euVxjyA7zBulfjtIBd7A6D
            D5RiIpWi+JLBDCfA5wFgPqjvVzpeVPLylJoc/k3FDTlVEgWfg9LPPChNbJemKUnz
            y1vJdMmxLXLTwH1GSbnL34KRdehNipk7xKh59SFrEfjdt41G4K5ugVyDhZDU55rK
            55ijpibv41LDrPoyFavUBv7uZJMIHHedZS+1M5d2wfytGJlsDj4Ljf0Pe/Bzi03n
            uaMvbkoEtOya69lusfAGnhRp4pCQ7q0mwxDpRH57e3ECgYEA+mQVCca5KrR1cC5v
            eFnegnRzfLWpN2qzB6zL2ifmyTL+0N58DFj7aq8tD3BOjS5yidwoEQvMz1vXH6Xz
            F4PftjUDqfNWnpVL4Uwy8Qv1RIT7G3NihDIomf9ztgPe2PxpIFFSOEewBriIIAq0
            gkaJcvahKJ0M+mbBiU7AZzscryMCgYEA738I2crIcr6TSgYwnXeU5heHYnVv4h7E
            SWVc9PzWBQLFXV5dv6qmnBJG7/C9gNUALifHoanNFLdOfxsAmMyXGwOXpFsXmZKG
            sKfLcfcrcHGVzSUifFOSn+RqqXPKj50syVIFggu92F1WAVfOhXv9ZnBHNXcUvkU9
            bU3YyER3YesCgYAFK1XH2uNtv0YBZ+QDPw+577+GnpV8wGYrzWRz/4jjNHtQQpeb
            BadDH5S1keL9RS02LWZiW7UBvuqlC58GKEHRGC0YMB7krT5gy1rt4QpSPNCWU36A
            LizqaPyxkEjkf+mHskYZRVmb+elNiAsL+XrS9caD+SIQdVifhhGwz+aaswKBgQCu
            QitpdJ/WxY2b7dcPrEO+C16pqDNJPwOpN8SkSRdcPADpB9GEtSFXLAFLckogZFsm
            OjGKEmey7BGEcFxF0Y8cy8dZgiIwzqmL/fsMqECGJ09NHfjyp0hnaKanNyYqyr0c
            mr6BOhPfS0JZ5c7R1z6we3+Eimmga/bfAiZ6yT5oEwKBgQCk6zSq1dUSsB6mN5er
            7CIOhQrU3t0nNPWywQXqQeCollw7Ir39EV8WE5Y8BXZ26WFccY54SSymicKioa2m
            63Y/z51uOobAPeMutmkX7kcs1Wsmh6Yto7IRBJVCi2dWJ3cRdsVNQ9gxbOfEI7wV
            uE2wcqhosdEvWh2nc9qeGE8Q3A==
            -----END PRIVATE KEY-----
        """.trimIndent()
        val x509Base64EncodedPublicKey = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6j+v8rY1nz8DnAfQc4IO
            rZti0KI+II9mDT/KxZwAAWHuJtgnVCSLsJB0et9WIFR+wkiqH4zcCxNgcUuszdX6
            HnmLmQqLkNrnchaVzcCQBq49/c1UvwMh5mW2ViwFy9wSiflB8ifVR+/1qbSFP0tO
            KaroXCdlFbKG3ibaRDEU4hgbzyWZlORalJ5iUeoPE0EYk5SWpSh6O61OjQkHjXZc
            uxrqK/PSV42fz3TcwdN5bS5WOwqFpdybE4IZ75yd2psMZ2y4iqVML5ls9NUSCaII
            4py7pavYjsEesY60Pjl8jroJHBUf25i0kKORFLlXtce/5PAuNgtU+na0lzBb1vYI
            IQIDAQAB
            -----END PUBLIC KEY-----
        """.trimIndent()

        val signatureAlgorithm = RSASecurity.SIGNATURE_SHA1
        val rsaPrivate = RSASecurity.fromEncodedPrivateKey(pkcs8NotEncryptedBase64EncodedPrivateKey, signatureAlgorithm)
        val rsaPublic = RSASecurity.fromEncodedPublicKey(x509Base64EncodedPublicKey, signatureAlgorithm)
        fun base64(data: ByteArray) = Base64.getEncoder().encodeToString(data)

        // Test Encrypt and Decrypt
        val msg = "Hello World!".toByteArray(Charsets.UTF_8)
        val encrypted = rsaPrivate.encrypt(msg) // private encrypt
        println("Private Encrypt: ${base64(encrypted)}")
        val decrypted = rsaPublic.decrypt(encrypted) // public decrypt
        println("Public Decrypt: ${String(decrypted, Charsets.UTF_8)}")

        // Test Signature and Verify
        val signature = rsaPrivate.signature(msg) // private signature
        println("Private Signature: ${base64(signature)}")
        val verify = rsaPublic.verify(msg, signature) // public verify
        println("Public Verify: $verify")
    }
}
