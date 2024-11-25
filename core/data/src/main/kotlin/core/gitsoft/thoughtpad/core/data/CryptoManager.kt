
/*
* Copyright 2024 Denis Githuku
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package core.gitsoft.thoughtpad.core.data

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

internal class CryptoManager {

    // Get a reference to the keystore api
    private val keystore = KeyStore.getInstance(KeystoreType).apply { load(null) }

    // Generate key to encrypt and decrypt secret
    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM)
            .apply {
                init(
                    KeyGenParameterSpec.Builder(
                            keystoreAlias,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                        )
                        .setBlockModes(BLOCK_MODE)
                        .setEncryptionPaddings(PADDING)
                        .setUserAuthenticationRequired(false)
                        .setRandomizedEncryptionRequired(true)
                        .build()
                )
            }
            .generateKey()
    }

    // Fetch key and create new one if does not exist
    private fun getKey(): SecretKey {
        val existingKey = keystore.getEntry(keystoreAlias, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    // Create an encryption cipher with a fresh IV
    private fun getEncryptionCipher(): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply { init(Cipher.ENCRYPT_MODE, getKey()) }
    }

    // Create an initialization vector -> initial state of our decryption (randomized sequence of
    // bytes)
    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    companion object {
        private const val KeystoreType = "AndroidKeystore"
        private const val keystoreAlias = "secret"
        private val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

    fun encrypt(byteArray: ByteArray, outputStream: OutputStream): ByteArray {
        val encryptionCipher = getEncryptionCipher()
        val encryptedBytes = encryptionCipher.doFinal(byteArray)

        /*
        Put the encrypted bytes in a stream so that when we need to decrypt our cipher
        we need the iv value
         */
        outputStream.use {
            it.write(encryptionCipher.iv.size)
            it.write(encryptionCipher.iv)
            it.write(encryptedBytes.size)
            it.write(encryptedBytes)
        }
        return encryptedBytes
    }

    fun decrypt(inputStream: InputStream): ByteArray {
        return inputStream.use {
            val ivSize = it.read()
            val iv = ByteArray(ivSize)
            it.read(iv)

            val encryptedBytesSize = it.read()
            val encryptedBytes = ByteArray(encryptedBytesSize)
            it.read(encryptedBytes)

            getDecryptCipherForIv(iv).doFinal(encryptedBytes)
        }
    }
}
