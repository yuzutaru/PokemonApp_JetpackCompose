package com.yustar.auth.presentation.widget

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yustar.core.ui.PokeApp_JetpackComposeTheme

/**
 * Created by Yustar Pramudana on 06/03/26.
 */

@Composable
fun TextInput(label: String, value: String = "", placeHolder: String, onEmailChange: (String) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = label
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onEmailChange,
            placeholder = {
                if (value.isEmpty())
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = placeHolder
                    )
            },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_4)
@Composable
fun NightModePreviewTextInput() {
    PokeApp_JetpackComposeTheme {
        TextInput(
            stringResource(com.yustar.auth.R.string.email),
            "",
            stringResource(com.yustar.auth.R.string.input_email)
        ){}
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun LightModePreviewTextInput() {
    PokeApp_JetpackComposeTheme {
        TextInput(
            stringResource(com.yustar.auth.R.string.password),
            "",
            stringResource(com.yustar.auth.R.string.input_password)
        ){}
    }
}
