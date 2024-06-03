package com.kcompany.billing.di

import android.app.Application
import com.kcompany.billing.billing.BillingModule

class AppModule(app: Application) {

    val billingModule by lazy {
        BillingModule(app = app)
    }
}