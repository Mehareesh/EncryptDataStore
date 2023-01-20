package com.meruga.encryptdatastore

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.io.DataOutput
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalSerializationApi::class)
@RequiresApi(Build.VERSION_CODES.M)
class UserSettingsSerializer(
    private val cryptoManager: CryptoManager
    ): Serializer<UserSettings> {

        override val defaultValue: UserSettings
            get() = UserSettings()

        override suspend fun readForm(input: InputStream): UserSettings {
            val decryptedBytes = CryptoManager.decrypt(input)
            return try {
                Json.decodeFromString(
                    deserializer = UserSettings.serializer(),
                    string = decryptedBytes.decodeToString()
                )
            } catch (e: SerializationException) {
                e.printStackTrace()
                defaultValue
            }

        }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        cryptoManager.encrypt(
            bytes = Json.encodeToString(
                serializer = UserSettings.serializer(),
                value = t
            ).encodeToByteArray(),
            outputStream = output
        )

    }
}