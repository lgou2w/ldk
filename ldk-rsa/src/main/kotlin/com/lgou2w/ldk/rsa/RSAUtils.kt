/*
 * Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)
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
import java.io.File
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 * ## RSAUtils
 *
 * * 2048-bit `RSA` and `SHA512` encryption, decryption and verify tool class.
 * * 2048 位 `RSA` 和 `SHA512` 的加解密校验工具类.
 *
 * @author lgou2w
 */
object RSAUtils {

    private const val BIT = 2048
    private const val RSA = "RSA"
    private const val EMPTY = ""
    private const val NEWLINE = "\r"
    private const val NEWLINE2 = "\n"
    private const val SIGNATURE = "SHA512withRSA"
    private const val DECRYPT_BLOCK_SIZE = BIT / 8
    private const val ENCRYPT_BLOCK_SIZE = BIT / 8 - 11
    private const val PRIVATE_KEY_HEADER = "-----BEGIN PRIVATE KEY-----"
    private const val PRIVATE_KEY_ENDER = "-----END PRIVATE KEY-----"
    private const val PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----"
    private const val PUBLIC_KEY_ENDER = "-----END PUBLIC KEY-----"

    @JvmStatic
    @Throws(IOException::class)
    fun generateKeyPair(): KeyPair = try {
        val keyPairGenerator = KeyPairGenerator.getInstance(RSA)
        keyPairGenerator.initialize(BIT, SecureRandom())
        keyPairGenerator.genKeyPair()
    } catch (e: Exception) {
        throw IOException("Unable to generate key pair.", e)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun decodePublicKey(encodedKey: ByteArray): PublicKey = try {
        KeyFactory.getInstance(RSA).generatePublic(X509EncodedKeySpec(encodedKey))
    } catch (e: GeneralSecurityException) {
        throw IOException("Unable to decrypt the public key.", e)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun decodePublicKey(encodedKey: String): PublicKey = try {
        val value = encodedKey
            .replace(NEWLINE, EMPTY)
            .replace(NEWLINE2, EMPTY)
            .replace(PUBLIC_KEY_HEADER, EMPTY)
            .replace(PUBLIC_KEY_ENDER, EMPTY)
        decodePublicKey(Base64.getDecoder().decode(value))
    } catch (e: Exception) {
        throw IOException("Unable to decrypt the public key.", e.cause ?: e)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun decodePrivateKey(encodedKey: ByteArray): PrivateKey = try {
        KeyFactory.getInstance(RSA).generatePrivate(PKCS8EncodedKeySpec(encodedKey))
    } catch (e: GeneralSecurityException) {
        throw IOException("Unable to decrypt the private key.", e)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun decodePrivateKey(encodedKey: String): PrivateKey = try {
        val value = encodedKey
            .replace(NEWLINE, EMPTY)
            .replace(NEWLINE2, EMPTY)
            .replace(PRIVATE_KEY_HEADER, EMPTY)
            .replace(PRIVATE_KEY_ENDER, EMPTY)
        decodePrivateKey(Base64.getDecoder().decode(value))
    } catch (e: Exception) {
        throw IOException("Unable to decrypt the private key.", e.cause ?: e)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun signature(privateKey: PrivateKey, data: ByteArray): ByteArray = try {
        val signature = Signature.getInstance(SIGNATURE)
        signature.initSign(privateKey)
        signature.update(data)
        signature.sign()
    } catch (e: Exception) {
        throw IOException("Unable to signature the data.", e)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun signature(privateKey: PrivateKey, file: File): ByteArray = try {
        signature(privateKey, file.readBytes())
    } catch (e: Exception) {
        throw IOException("Unable to signature the data.", e.cause ?: e)
    }

    @JvmStatic
    fun verify(publicKey: PublicKey, data: ByteArray, sign: ByteArray): Boolean = try {
        val signature = Signature.getInstance(SIGNATURE)
        signature.initVerify(publicKey)
        signature.update(data)
        signature.verify(sign)
    } catch (e: Exception) {
        false
    }

    @JvmStatic
    fun verify(publicKey: PublicKey, file: File, signFile: File): Boolean = try {
        verify(publicKey, file.readBytes(), signFile.readBytes())
    } catch (e: Exception) {
        false
    }

    @JvmStatic
    @Throws(IOException::class)
    fun encrypt(key: Key, data: ByteArray): ByteArray = runEncryption(Cipher.ENCRYPT_MODE, key, data)

    @JvmStatic
    @Throws(IOException::class)
    fun decrypt(key: Key, data: ByteArray): ByteArray = runEncryption(Cipher.DECRYPT_MODE, key, data)

    @JvmStatic
    @Throws(IOException::class, UnsupportedOperationException::class)
    private fun runEncryption(mode: Int, key: Key, data: ByteArray): ByteArray = try {
        val cipher = Cipher.getInstance(RSA)
        cipher.init(mode, key)
        val maxBlockSize = when (mode) {
            Cipher.ENCRYPT_MODE -> ENCRYPT_BLOCK_SIZE
            Cipher.DECRYPT_MODE -> DECRYPT_BLOCK_SIZE
            else -> throw UnsupportedOperationException("Unsupported encryption mode: $mode.")
        }
        val dataLength = data.size
        val output = ByteArrayOutputStream()
        var cache: ByteArray
        var offset = 0
        var index = 0
        while (dataLength - offset > 0) {
            cache = if (dataLength - offset > maxBlockSize) cipher.doFinal(data, offset, maxBlockSize)
            else cipher.doFinal(data, offset, dataLength - offset)
            output.write(cache)
            offset = ++index * maxBlockSize
        }
        val result = output.toByteArray()
        output.close()
        result
    } catch (e: GeneralSecurityException) {
        throw IOException("Unable to run encryption.", e)
    }
}
