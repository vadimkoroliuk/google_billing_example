package com.kcompany.billing

import android.app.Application
import com.kcompany.billing.di.DI

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DI.init(app = this)
    }
}