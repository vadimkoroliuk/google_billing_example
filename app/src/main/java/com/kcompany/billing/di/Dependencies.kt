package com.kcompany.billing.di

import android.app.Application

class Dependencies(
    private val app: Application
) {

    val appModule by lazy {
        AppModule(app = app)
    }
}