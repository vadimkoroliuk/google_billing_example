package com.kcompany.billing.billing

sealed interface UserState {

    data object Premium : UserState

    data object Free : UserState
}