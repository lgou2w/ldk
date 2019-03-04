/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.rsa

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature
import java.security.interfaces.RSAKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

/**
 * ## RSASecurity
 *
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
@Suppress("MemberVisibilityCanBePrivate")
class RSASecurity private constructor(
        val key: Key,
        val signatureAlgorithm: String
) {

    @Suppress("unused")
    companion object {

        const val RSA = "RSA"

        @Deprecated("UNSAFE")
        const val BIT_512 = 0x200
        @Deprecated("UNSAFE")
        const val BIT_1024 = 0x400
        const val BIT_2048 = 0x800
        const val BIT_3072 = 0xC00
        const val BIT_4096 = 0x1000
        const val BIT_7680 = 0x1E00
        const val BIT_15360 = 0x3C00

        const val SIGNATURE_MD2 = "MD2WithRSA"
        const val SIGNATURE_MD5 = "MD5WithRSA"
        const val SIGNATURE_SHA1 = "SHA1WithRSA"
        const val SIGNATURE_SHA224 = "SHA224WithRSA"
        const val SIGNATURE_SHA256 = "SHA256WithRSA"
        const val SIGNATURE_SHA384 = "SHA384WithRSA"
        const val SIGNATURE_SHA512 = "SHA512WithRSA"

        private const val PKCS8_PRIVATE_KEY_BEGIN = "-----BEGIN PRIVATE KEY-----"
        private const val PKCS8_PRIVATE_KEY_END = "-----END PRIVATE KEY-----"
        private const val X509_PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----"
        private const val X509_PUBLIC_KEY_END = "-----END PUBLIC KEY-----"
        private const val NEWLINE = "\r"
        private const val NEWLINE2 = "\n"
        private const val EMPTY = ""

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromKey(rsaKey: Key, signatureAlgorithm: String): RSASecurity {
            return RSASecurity(rsaKey, signatureAlgorithm)
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromEncodedKey(pkcs8OrX509Base64EncodedKey: String, signatureAlgorithm: String): RSASecurity {
            val key = decodeKey(pkcs8OrX509Base64EncodedKey)
            return RSASecurity(key, signatureAlgorithm)
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromEncodedPrivateKey(pkcs8NotEncryptedBase64EncodedPrivateKey: String, signatureAlgorithm: String): RSASecurity {
            val privateKey = decodePrivateKey(pkcs8NotEncryptedBase64EncodedPrivateKey)
            return RSASecurity(privateKey, signatureAlgorithm)
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromEncodedPrivateKey(pkcs8EncodedPrivateKey: ByteArray, signatureAlgorithm: String): RSASecurity {
            val privateKey = decodePrivateKey(pkcs8EncodedPrivateKey)
            return RSASecurity(privateKey, signatureAlgorithm)
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromEncodedPublicKey(x509Base64EncodedPublicKey: String, signatureAlgorithm: String): RSASecurity {
            val publicKey = decodePublicKey(x509Base64EncodedPublicKey)
            return RSASecurity(publicKey, signatureAlgorithm)
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromEncodedPublicKey(x509EncodedPublicKey: ByteArray, signatureAlgorithm: String): RSASecurity {
            val publicKey = decodePublicKey(x509EncodedPublicKey)
            return RSASecurity(publicKey, signatureAlgorithm)
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromKeyPair(rsaKeyPair: KeyPair, signatureAlgorithm: String): Pair<RSASecurity, RSASecurity> {
            val rsaPrivateSecurity = RSASecurity(rsaKeyPair.private, signatureAlgorithm)
            val rsaPublicSecurity = RSASecurity(rsaKeyPair.public, signatureAlgorithm)
            return rsaPrivateSecurity to rsaPublicSecurity
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromKeyPairGenerator(bit: Int, signatureAlgorithm: String): Pair<RSASecurity, RSASecurity> {
            @Suppress("DEPRECATION")
            if (bit < BIT_512)
                throw IllegalArgumentException("RSA keys must be at least 512 bits long.")
            val rsaKeyPair = try {
                val keyPairGenerator = KeyPairGenerator.getInstance(RSA)
                keyPairGenerator.initialize(bit, SecureRandom())
                keyPairGenerator.genKeyPair()
            } catch (e: Exception) {
                throw IllegalArgumentException("Unable to generate key pair.", e)
            }
            return fromKeyPair(rsaKeyPair, signatureAlgorithm)
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun decodeKey(pkcs8OrX509Base64EncodedKey: String): Key {
            val noNewline = pkcs8OrX509Base64EncodedKey.replace(NEWLINE, EMPTY).replace(NEWLINE2, EMPTY)
            return if (noNewline.startsWith(PKCS8_PRIVATE_KEY_BEGIN) && noNewline.endsWith(PKCS8_PRIVATE_KEY_END))
                decodePrivateKey(noNewline)
            else if (noNewline.startsWith(X509_PUBLIC_KEY_BEGIN) && noNewline.endsWith(X509_PUBLIC_KEY_END))
                decodePublicKey(noNewline)
            else throw IllegalArgumentException("Invalid private or public key.")
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun decodePrivateKey(pkcs8NotEncryptedBase64EncodedPrivateKey: String): PrivateKey {
            val noNewline = pkcs8NotEncryptedBase64EncodedPrivateKey.replace(NEWLINE, EMPTY).replace(NEWLINE2, EMPTY)
            if (!noNewline.startsWith(PKCS8_PRIVATE_KEY_BEGIN) ||
                !noNewline.endsWith(PKCS8_PRIVATE_KEY_END)) {
                throw IllegalArgumentException("Invalid private key header or end.")
            }
            val base64EncodedKey = noNewline
                .replace(PKCS8_PRIVATE_KEY_BEGIN, EMPTY)
                .replace(PKCS8_PRIVATE_KEY_END, EMPTY)
            val pkcs8EncodedPrivateKey = Base64.getDecoder().decode(base64EncodedKey)
            return decodePrivateKey(pkcs8EncodedPrivateKey)
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun decodePublicKey(x509Base64EncodedPublicKey: String): PublicKey {
            val noNewline = x509Base64EncodedPublicKey.replace(NEWLINE, EMPTY).replace(NEWLINE2, EMPTY)
            if (!noNewline.startsWith(X509_PUBLIC_KEY_BEGIN) ||
                !noNewline.endsWith(X509_PUBLIC_KEY_END)) {
                throw IllegalArgumentException("Invalid public key header or end.")
            }
            val base64EncodedKey = noNewline
                .replace(X509_PUBLIC_KEY_BEGIN, EMPTY)
                .replace(X509_PUBLIC_KEY_END, EMPTY)
            val x509EncodedPublicKey = Base64.getDecoder().decode(base64EncodedKey)
            return decodePublicKey(x509EncodedPublicKey)
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun decodePrivateKey(pkcs8EncodedPrivateKey: ByteArray): PrivateKey {
            return try {
                KeyFactory.getInstance(RSA).generatePrivate(PKCS8EncodedKeySpec(pkcs8EncodedPrivateKey))
            } catch (e: GeneralSecurityException) {
                throw IllegalArgumentException("Not a valid PKCS#8 format private key.", e)
            }
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun decodePublicKey(x509EncodedPublicKey: ByteArray): PublicKey {
            return try {
                KeyFactory.getInstance(RSA).generatePublic(X509EncodedKeySpec(x509EncodedPublicKey))
            } catch (e: GeneralSecurityException) {
                throw IllegalArgumentException("Not a valid X.509 format public key.", e)
            }
        }
    }

    /**
     * RSA key bit length
     */
    val bit : Int

    init {

        // The key must be an RSA public or private key
        if (key !is RSAKey)
            throw IllegalArgumentException("Not a valid RSA public or private key.")

        // Perform a digital signature algorithm detection
        try {
            Signature.getInstance(signatureAlgorithm)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException("Invalid signature algorithm: $signatureAlgorithm", e)
        }

        this.bit = key.modulus.bitLength()
    }

    /**************************************************************************
     *
     * Encrypt or Decrypt
     *
     **************************************************************************/

    private val decryptBlockSize = (bit shr 3)
    private val encryptBlockSize = (bit shr 3) - 0x0B

    @Throws(IOException::class)
    fun encrypt(data: ByteArray) = runEncryption(Cipher.ENCRYPT_MODE, encryptBlockSize, data)

    @Throws(IOException::class)
    fun decrypt(data: ByteArray) = runEncryption(Cipher.DECRYPT_MODE, decryptBlockSize, data)

    @Throws(IOException::class)
    private fun runEncryption(mode: Int, blockSize: Int, data: ByteArray): ByteArray = try {
        val cipher = Cipher.getInstance(RSA).apply { init(mode, key) }
        val dataLength = data.size
        val output = ByteArrayOutputStream()
        var cache: ByteArray
        var offset = 0
        var index = 0
        while (dataLength - offset > 0) {
            cache = if (dataLength - offset > blockSize) cipher.doFinal(data, offset, blockSize)
            else cipher.doFinal(data, offset, dataLength - offset)
            output.write(cache)
            offset = ++index * blockSize
        }
        val result = output.toByteArray()
        output.close()
        result
    } catch (e: GeneralSecurityException) {
        throw IOException("Unable to run encryption.", e)
    }

    /**************************************************************************
     *
     * Signature or Verify
     *
     **************************************************************************/

    val isPrivateKey = key is PrivateKey

    @Throws(IOException::class, UnsupportedOperationException::class)
    fun signature(data: ByteArray): ByteArray {
        if (!isPrivateKey)
            throw UnsupportedOperationException("Only the RSA private key can be signature.")
        return try {
            val signature = Signature.getInstance(signatureAlgorithm)
            signature.initSign(key as PrivateKey)
            signature.update(data)
            signature.sign()
        } catch (e: Exception) {
            throw IOException("Unable to signature the data.", e)
        }
    }

    @Throws(UnsupportedOperationException::class)
    fun verify(data: ByteArray, sign: ByteArray): Boolean {
        if (isPrivateKey)
            throw UnsupportedOperationException("Only the RSA public key can be signature verify.")
        return try {
            val signature = Signature.getInstance(signatureAlgorithm)
            signature.initVerify(key as PublicKey)
            signature.update(data)
            signature.verify(sign)
        } catch (e: Exception) {
            false // if error
        }
    }

    /**************************************************************************
     *
     * Important
     *
     **************************************************************************/

    override fun toString(): String {
        return "RSASecurity(bit=$bit, signature=$signatureAlgorithm, isPrivateKey=$isPrivateKey)"
    }
}
