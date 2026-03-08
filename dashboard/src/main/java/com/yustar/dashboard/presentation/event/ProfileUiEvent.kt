package com.yustar.dashboard.presentation.event

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

sealed class ProfileUiEvent {
    data class OnFirstNameChanged(val firstName: String) : ProfileUiEvent()
    data class OnLastNameChanged(val lastName: String) : ProfileUiEvent()
    data class OnAddressChanged(val address: String) : ProfileUiEvent()
    data class OnPhoneNumberChanged(val phoneNumber: String) : ProfileUiEvent()
    object OnSaveClick : ProfileUiEvent()
    object ClearError : ProfileUiEvent()
    object ResetSuccess : ProfileUiEvent()
}
