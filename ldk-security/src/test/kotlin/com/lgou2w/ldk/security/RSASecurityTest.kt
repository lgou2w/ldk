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

package com.lgou2w.ldk.security

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.amshove.kluent.shouldThrow
import org.junit.Test
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.interfaces.RSAKey
import java.util.Base64

class RSASecurityTest {

    companion object {
        const val TEST_SIGNATURE_ALGORITHM = "SHA1WithRSA"
        const val TEST_BIT = 1024
        val TEST_PKCS8_NOT_ENCRYPTED_BASE64_ENCODED_PRIVATE_KEY = """
            -----BEGIN PRIVATE KEY-----
            MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL2jpl9AKdXGHVGy
            cIFZupX4DM/ofmTjsuQDnttfM95dk0AGnoGhopjZU+TeryIqpo2J2JWRm4jPycd9
            1YX3rYNOPvNlOL3lw6p+HsZ2Mdq5kVyGgvD1gzN1f1MfPe2JWtPzURUEfvVHAm6j
            sVLYi8YWccfCZPlFpJwNVJ3iKc01AgMBAAECgYAuh4i6tjONkB6BfRa9phZx1XrP
            SGw6tOOfroO7r5A6bxtCCl250L7kYe6DqV+vMmfLMpdBm4hd+R25JNbPGAYqVTAe
            NFFFxWVvHtdg7aV/xNfLitXJYLO7WOczfLOW2mlX0d5J19blHbpD284eFrEMWhIT
            nKYDPZhNsEwPKw83wQJBAOiekzn+IwOtvUbWg0Vst9MyvE4PER8Kw4hED0RjCFBZ
            to676NItTCp3Y9JqLHMpZDCLjVLzZYTuy8e7lX5bBOsCQQDQsyvhHKQzrKcZHwAe
            Ep1CNJcCLL0XeME865faO6KEJUAcpxJfI5qEvG8VQicVafnK7GDrV5jKNL4Mo3wt
            Um5fAkAFJnWmBA1saG1XYyp24AhV4bqvk7/SSx+3JaKc5gBqHwJeuKRX+u7Dxe7s
            2rLvPcxdEsnnO0JVJiBW5L1TldpPAkBmqRxWme4z9SQleVhncqSk4pW93iAOIbp8
            qy+BjIA/HMvSzGR/JFZoIQFLpcTwBbd4mJp8ahSgsx0hvnHhIYFlAkEAhGrH69Iw
            EPTFjQzER/7s+1PvpMLoDjY4tZlBIpiptiJoXR6p6pouRarbkPhoz3v4P9rCVDgy
            oTAfJHpvy/Y14g==
            -----END PRIVATE KEY-----
        """.trimIndent()
        val TEST_X509_BASE64_ENCODED_PUBLIC_KEY = """
            -----BEGIN PUBLIC KEY-----
            MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9o6ZfQCnVxh1RsnCBWbqV+AzP
            6H5k47LkA57bXzPeXZNABp6BoaKY2VPk3q8iKqaNidiVkZuIz8nHfdWF962DTj7z
            ZTi95cOqfh7GdjHauZFchoLw9YMzdX9THz3tiVrT81EVBH71RwJuo7FS2IvGFnHH
            wmT5RaScDVSd4inNNQIDAQAB
            -----END PUBLIC KEY-----
        """.trimIndent()
        val TEST_PKCS8_BASE64_ENCODED_PRIVATE_KEY_BYTES = byteArrayOf(
                77,73,73,67,100,103,73,66,
                65,68,65,78,66,103,107,113,104,107,105,71,57,119,48,66,
                65,81,69,70,65,65,83,67,65,109,65,119,103,103,74,99,65,
                103,69,65,65,111,71,66,65,76,50,106,112,108,57,65,75,100,
                88,71,72,86,71,121,99,73,70,90,117,112,88,52,68,77,
                47,111,102,109,84,106,115,117,81,68,110,116,116,102,77,
                57,53,100,107,48,65,71,110,111,71,104,111,112,106,90,85,
                43,84,101,114,121,73,113,112,111,50,74,50,74,87,82,109,52,
                106,80,121,99,100,57,49,89,88,51,114,89,78,79,80,118,
                78,108,79,76,51,108,119,54,112,43,72,115,90,50,77,100,113,
                53,107,86,121,71,103,118,68,49,103,122,78,49,102,49,77,102,
                80,101,50,74,87,116,80,122,85,82,85,69,102,118,86,72,65,109,
                54,106,115,86,76,89,105,56,89,87,99,99,102,67,90,80,108,
                70,112,74,119,78,86,74,51,105,75,99,48,49,65,103,77,66,65,65,
                69,67,103,89,65,117,104,52,105,54,116,106,79,78,107,66,54,66,
                102,82,97,57,112,104,90,120,49,88,114,80,83,71,119,54,
                116,79,79,102,114,111,79,55,114,53,65,54,98,120,116,67,67,108,
                50,53,48,76,55,107,89,101,54,68,113,86,43,118,77,109,102,76,77,
                112,100,66,109,52,104,100,43,82,50,53,74,78,98,80,71,65,89,113,
                86,84,65,101,78,70,70,70,120,87,86,118,72,116,100,103,55,
                97,86,47,120,78,102,76,105,116,88,74,89,76,79,55,87,79,99,122,
                102,76,79,87,50,109,108,88,48,100,53,74,49,57,98,108,72,98,112,
                68,50,56,52,101,70,114,69,77,87,104,73,84,110,75,89,68,80,
                90,104,78,115,69,119,80,75,119,56,51,119,81,74,66,65,79,105,101,
                107,122,110,43,73,119,79,116,118,85,98,87,103,48,86,115,116,57,
                77,121,118,69,52,80,69,82,56,75,119,52,104,69,68,48,82,106,67,70,
                66,90,116,111,54,55,54,78,73,116,84,67,112,51,89,57,74,113,
                76,72,77,112,90,68,67,76,106,86,76,122,90,89,84,117,121,56,101,55,
                108,88,53,98,66,79,115,67,81,81,68,81,115,121,118,104,72,75,81,122,
                114,75,99,90,72,119,65,101,69,112,49,67,78,74,99,67,76,76,48,
                88,101,77,69,56,54,53,102,97,79,54,75,69,74,85,65,99,112,120,74,102,
                73,53,113,69,118,71,56,86,81,105,99,86,97,102,110,75,55,71,68,114,
                86,53,106,75,78,76,52,77,111,51,119,116,85,109,53,102,65,107,
                65,70,74,110,87,109,66,65,49,115,97,71,49,88,89,121,112,50,52,65,
                104,86,52,98,113,118,107,55,47,83,83,120,43,51,74,97,75,99,53,103,
                66,113,72,119,74,101,117,75,82,88,43,117,55,68,120,101,55,115,
                50,114,76,118,80,99,120,100,69,115,110,110,79,48,74,86,74,105,
                66,87,53,76,49,84,108,100,112,80,65,107,66,109,113,82,120,87,109,
                101,52,122,57,83,81,108,101,86,104,110,99,113,83,107,52,112,87,
                57,51,105,65,79,73,98,112,56,113,121,43,66,106,73,65,47,72,
                77,118,83,122,71,82,47,74,70,90,111,73,81,70,76,112,99,84,119,66,
                98,100,52,109,74,112,56,97,104,83,103,115,120,48,104,118,110,72,
                104,73,89,70,108,65,107,69,65,104,71,114,72,54,57,73,119,69,
                80,84,70,106,81,122,69,82,47,55,115,43,49,80,118,112,77,76,111,68,
                106,89,52,116,90,108,66,73,112,105,112,116,105,74,111,88,82,54,
                112,54,112,111,117,82,97,114,98,107,80,104,111,122,51,118,52,80,
                57,114,67,86,68,103,121,111,84,65,102,74,72,112,118,121,47,
                89,49,52,103,61,61)
        val TEST_X509_BASE64_ENCODED_PUBLIC_KEY_BYTES = byteArrayOf(
                77,73,71,102,77,65,48,71,67,83,113,71,83,73,98,
                51,68,81,69,66,65,81,85,65,65,52,71,78,65,68,67,66,105,81,75,66,
                103,81,67,57,111,54,90,102,81,67,110,86,120,104,49,82,115,110,
                67,66,87,98,113,86,43,65,122,80,54,72,53,107,52,55,76,107,
                65,53,55,98,88,122,80,101,88,90,78,65,66,112,54,66,111,97,75,89,
                50,86,80,107,51,113,56,105,75,113,97,78,105,100,105,86,107,90,
                117,73,122,56,110,72,102,100,87,70,57,54,50,68,84,106,55,122,
                90,84,105,57,53,99,79,113,102,104,55,71,100,106,72,97,117,90,
                70,99,104,111,76,119,57,89,77,122,100,88,57,84,72,122,51,116,105,
                86,114,84,56,49,69,86,66,72,55,49,82,119,74,117,111,55,70,83,50,73,
                118,71,70,110,72,72,119,109,84,53,82,97,83,99,68,86,83,100,52,
                105,110,78,78,81,73,68,65,81,65,66)
    }

    @Test fun `RSASecurity - fromKeyPair - validation`() {
        val generator = KeyPairGenerator.getInstance("RSA").apply { initialize(1024) }
        val keyPair = generator.genKeyPair()
        RSASecurity.fromKeyPair(keyPair, TEST_SIGNATURE_ALGORITHM) shouldNotEqual null
        RSASecurity.fromKeyPairGenerator(1024, TEST_SIGNATURE_ALGORITHM) shouldNotEqual null
        invoking { RSASecurity.fromKeyPair(keyPair, "Invalid algorithm") } shouldThrow IllegalArgumentException::class
        invoking { RSASecurity.fromKeyPairGenerator(1024, "Invalid algorithm") } shouldThrow IllegalArgumentException::class
        invoking { RSASecurity.fromKeyPairGenerator(1, "Bit length must be greater than 512") } shouldThrow IllegalArgumentException::class
    }

    @Test fun `RSASecurity - decodeKey - validation`() {
        (RSASecurity.decodePublicKey(TEST_X509_BASE64_ENCODED_PUBLIC_KEY) as RSAKey)
            .modulus.bitLength() shouldEqual TEST_BIT
        (RSASecurity.decodePrivateKey(TEST_PKCS8_NOT_ENCRYPTED_BASE64_ENCODED_PRIVATE_KEY) as RSAKey)
            .modulus.bitLength() shouldEqual TEST_BIT
        RSASecurity.decodeKey(TEST_X509_BASE64_ENCODED_PUBLIC_KEY) shouldBeInstanceOf PublicKey::class
        RSASecurity.decodeKey(TEST_PKCS8_NOT_ENCRYPTED_BASE64_ENCODED_PRIVATE_KEY) shouldBeInstanceOf PrivateKey::class
        invoking { RSASecurity.decodePublicKey("error encoded key") } shouldThrow IllegalArgumentException::class
        invoking { RSASecurity.decodePublicKey("-----BEGIN PUBLIC KEY-----error") } shouldThrow IllegalArgumentException::class
        invoking { RSASecurity.decodePublicKey(byteArrayOf(0)) } shouldThrow IllegalArgumentException::class
        invoking { RSASecurity.decodePrivateKey("error encoded key") } shouldThrow IllegalArgumentException::class
        invoking { RSASecurity.decodePrivateKey("-----BEGIN PRIVATE KEY-----error") } shouldThrow IllegalArgumentException::class
        invoking { RSASecurity.decodePrivateKey(byteArrayOf(0)) } shouldThrow IllegalArgumentException::class
        invoking { RSASecurity.decodeKey("-----BEGIN PUBLIC KEY-----error") } shouldThrow IllegalArgumentException::class
        invoking { RSASecurity.decodeKey("-----BEGIN PRIVATE KEY-----error") } shouldThrow IllegalArgumentException::class
    }

    @Test fun `RSASecurity - fromKey - validation`() {
        RSASecurity.fromEncodedPublicKey(TEST_X509_BASE64_ENCODED_PUBLIC_KEY, TEST_SIGNATURE_ALGORITHM)
            .key shouldBeInstanceOf PublicKey::class
        RSASecurity.fromEncodedPrivateKey(TEST_PKCS8_NOT_ENCRYPTED_BASE64_ENCODED_PRIVATE_KEY, TEST_SIGNATURE_ALGORITHM)
            .key shouldBeInstanceOf PrivateKey::class
        RSASecurity.fromEncodedKey(TEST_X509_BASE64_ENCODED_PUBLIC_KEY, TEST_SIGNATURE_ALGORITHM)
            .key shouldBeInstanceOf PublicKey::class
        RSASecurity.fromEncodedKey(TEST_PKCS8_NOT_ENCRYPTED_BASE64_ENCODED_PRIVATE_KEY, TEST_SIGNATURE_ALGORITHM)
            .key shouldBeInstanceOf PrivateKey::class
        val publicKeyBytes = Base64.getDecoder().decode(TEST_X509_BASE64_ENCODED_PUBLIC_KEY_BYTES)
        RSASecurity.fromEncodedPublicKey(publicKeyBytes, TEST_SIGNATURE_ALGORITHM)
            .key shouldBeInstanceOf PublicKey::class
        val privateKeyBytes = Base64.getDecoder().decode(TEST_PKCS8_BASE64_ENCODED_PRIVATE_KEY_BYTES)
        RSASecurity.fromEncodedPrivateKey(privateKeyBytes, TEST_SIGNATURE_ALGORITHM)
            .key shouldBeInstanceOf PrivateKey::class
        invoking { RSASecurity.fromEncodedPublicKey(byteArrayOf(0), TEST_SIGNATURE_ALGORITHM) } shouldThrow IllegalArgumentException::class
        invoking { RSASecurity.fromEncodedPrivateKey(byteArrayOf(0), TEST_SIGNATURE_ALGORITHM) } shouldThrow IllegalArgumentException::class
        val generator = KeyPairGenerator.getInstance("RSA").apply { initialize(1024) }
        val keyPair = generator.genKeyPair()
        RSASecurity.fromKey(keyPair.private, TEST_SIGNATURE_ALGORITHM)
            .key shouldBe keyPair.private
    }

    @Test fun `RSASecurity - method validation`() {
        val rsaSecurityPrivate = RSASecurity
            .fromEncodedKey(TEST_PKCS8_NOT_ENCRYPTED_BASE64_ENCODED_PRIVATE_KEY, TEST_SIGNATURE_ALGORITHM)
        val rsaSecurityPublic = RSASecurity
            .fromEncodedKey(TEST_X509_BASE64_ENCODED_PUBLIC_KEY, TEST_SIGNATURE_ALGORITHM)
        rsaSecurityPrivate.signatureAlgorithm shouldEqual TEST_SIGNATURE_ALGORITHM
        rsaSecurityPrivate.key shouldBeInstanceOf PrivateKey::class.java
        rsaSecurityPrivate.bit shouldEqual TEST_BIT
        rsaSecurityPrivate.isPrivateKey shouldEqual true
        rsaSecurityPublic.signatureAlgorithm shouldEqual TEST_SIGNATURE_ALGORITHM
        rsaSecurityPublic.key shouldBeInstanceOf PublicKey::class.java
        rsaSecurityPublic.bit shouldEqual TEST_BIT
        rsaSecurityPublic.isPrivateKey shouldEqual false
        rsaSecurityPrivate.toString() shouldContain "true" // isPrivateKey=true
    }

    @Test fun `RSASecurity - encryption validation`() {
        val rsaSecurityPrivate = RSASecurity
            .fromEncodedKey(TEST_PKCS8_NOT_ENCRYPTED_BASE64_ENCODED_PRIVATE_KEY, TEST_SIGNATURE_ALGORITHM)
        val rsaSecurityPublic = RSASecurity
            .fromEncodedKey(TEST_X509_BASE64_ENCODED_PUBLIC_KEY, TEST_SIGNATURE_ALGORITHM)
        // Public key encryption, private key decryption, vice versa
        val plainText = "This is a very, very long plain text, contains a variety of character encodings, and so on. . ." +
                        "这是一段非常非常长的明文本，包含了多种字符编码，等等等等。。。"
        val plainTextBytes = plainText.toByteArray(Charsets.UTF_8)
        val plainTextPublicKeyEncrypted = rsaSecurityPublic.encrypt(plainTextBytes)
        val plainTextPrivateKeyDecrypted = rsaSecurityPrivate.decrypt(plainTextPublicKeyEncrypted)
        plainTextPrivateKeyDecrypted shouldEqual plainTextBytes
        plainTextPrivateKeyDecrypted.toString(Charsets.UTF_8) shouldEqual plainText
        // Private key encryption, public key decryption
        val plainTextPrivateKeyEncrypted = rsaSecurityPrivate.encrypt(plainTextBytes)
        val plainTextPublicKeyDecrypted = rsaSecurityPublic.decrypt(plainTextPrivateKeyEncrypted)
        plainTextPublicKeyDecrypted shouldEqual plainTextBytes
        plainTextPublicKeyDecrypted.toString(Charsets.UTF_8) shouldEqual plainText
    }

    @Test fun `RSASecurity - signature and verify validation`() {
        val rsaSecurityPrivate = RSASecurity
            .fromEncodedKey(TEST_PKCS8_NOT_ENCRYPTED_BASE64_ENCODED_PRIVATE_KEY, TEST_SIGNATURE_ALGORITHM)
        val rsaSecurityPublic = RSASecurity
            .fromEncodedKey(TEST_X509_BASE64_ENCODED_PUBLIC_KEY, TEST_SIGNATURE_ALGORITHM)
        // Private key signature, public key verify
        invoking { rsaSecurityPrivate.verify(byteArrayOf(), byteArrayOf()) } shouldThrow UnsupportedOperationException::class
        invoking { rsaSecurityPublic.signature(byteArrayOf()) } shouldThrow UnsupportedOperationException::class
        // signature and verify
        val plainText = "Hello World"
        val plainTextBytes = plainText.toByteArray()
        val plaintTextSignature = rsaSecurityPrivate.signature(plainTextBytes)
        rsaSecurityPublic.verify(plainTextBytes, plaintTextSignature) shouldEqual true
        rsaSecurityPublic.verify(plainTextBytes, byteArrayOf(0)) shouldEqual false // invalid signature
    }
}
