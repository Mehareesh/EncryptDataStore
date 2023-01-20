package com.meruga.encryptdatastore

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import com.plcoding.androidcrypto.ui.theme.AndroidCryptoTheme
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : ComponentActivity() {

    private val Context.dataStore by dataStore(
        fileName = "user-settings.json",
        serializer = UserSettingsSerializer(CryptoManager())
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncryptDataStoreTheme {
                var userName by remember {
                    mutableStateOf("")
                }
                var password by remember {
                    mutableStateOf("")
                }
                var settings by remember {
                    mutableStateOf(UserSettings())
                }

                val scope = rememberCoroutineScope()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    TextField(
                        value = userName,
                        onValueChange = { username = it},
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {Text (text = "Password")}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(onClick = {
                            scope.launch {
                                dataStore.updateData {
                                    userName = userName
                                    password = password
                                }
                            }
                        }) {
                            Text(text = "Save")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            scope.launch {
                                settings = dataStore.data.fiest()
                            }

                        }) {
                            Text(text = settings.toString())
                        }
                    }
                }

            }
        }
    }
}