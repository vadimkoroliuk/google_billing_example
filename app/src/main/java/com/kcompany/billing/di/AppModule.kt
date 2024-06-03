package com.kcompany.billing.di

import android.app.Application
import com.kcompany.billing.billing.Billing

class AppModule(app: Application) {

    val billing by lazy {
        Billing(app = app)
    }
}