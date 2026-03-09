package com.yustar.dashboard.presentation.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yustar.core.ui.PokeApp_JetpackComposeTheme
import com.yustar.core.ui.Red60
import com.yustar.core.ui.widget.TextInput
import com.yustar.dashboard.R
import com.yustar.dashboard.presentation.event.ProfileUiEvent
import com.yustar.dashboard.presentation.state.ProfileUiState
import com.yustar.dashboard.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.error) {
        if (uiState.error.isNotEmpty()) {
            Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(ProfileUiEvent.ClearError)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, context.getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
            viewModel.onEvent(ProfileUiEvent.ResetSuccess)
        }
    }

    ProfileContent(
        paddingValues = paddingValues,
        uiState = uiState,
        onFirstNameChanged = { viewModel.onEvent(ProfileUiEvent.OnFirstNameChanged(it)) },
        onLastNameChanged = { viewModel.onEvent(ProfileUiEvent.OnLastNameChanged(it)) },
        onAddressChanged = { viewModel.onEvent(ProfileUiEvent.OnAddressChanged(it)) },
        onPhoneNumberChanged = { viewModel.onEvent(ProfileUiEvent.OnPhoneNumberChanged(it)) },
        onSaveClick = { viewModel.onEvent(ProfileUiEvent.OnSaveClick) }
    )
}

@Composable
fun ProfileContent(
    paddingValues: PaddingValues,
    uiState: ProfileUiState,
    onFirstNameChanged: (String) -> Unit = {},
    onLastNameChanged: (String) -> Unit = {},
    onAddressChanged: (String) -> Unit = {},
    onPhoneNumberChanged: (String) -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .safeDrawingPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                text = stringResource(R.string.update_profile)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                TextInput(
                    stringResource(R.string.first_name),
                    uiState.firstName, stringResource(R.string.input_first_name),
                    onValueChange = onFirstNameChanged
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextInput(
                    stringResource(R.string.last_name),
                    uiState.lastName, stringResource(R.string.input_last_name),
                    onValueChange = onLastNameChanged
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextInput(
                    stringResource(R.string.address),
                    uiState.address, stringResource(R.string.input_address),
                    onValueChange = onAddressChanged
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextInput(
                    stringResource(R.string.phone_number),
                    uiState.phoneNumber, stringResource(R.string.input_phone_number),
                    keyboardType = KeyboardType.Phone,
                    onValueChange = onPhoneNumberChanged
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Red60),
                    enabled = !uiState.isLoading,
                    onClick = onSaveClick,
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .height(24.dp)
                                        .width(24.dp),
                                    color = MaterialTheme.colorScheme.background
                                )
                            } else {
                                Text(
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.background,
                                    text = stringResource(R.string.save)
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_4)
@Composable
fun NightModePreviewProfileScreen() {
    PokeApp_JetpackComposeTheme {
        ProfileContent(PaddingValues(), ProfileUiState())
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun LightModePreviewProfileScreen() {
    PokeApp_JetpackComposeTheme {
        ProfileContent(PaddingValues(), ProfileUiState())
    }
}
