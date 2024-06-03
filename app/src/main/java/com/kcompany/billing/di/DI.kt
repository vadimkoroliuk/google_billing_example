package com.kcompany.billing.di

import android.app.Application
import com.kcompany.billing.ext.isMainProcess

object DI {

    private var dependencies: Dependencies? = null

    fun init(app: Application) {
        initDependencies(app)
    }

    @JvmStatic
    fun getDependencies(): Dependencies {
        return dependencies ?: error("DI must be init first")
    }

    private fun initDependencies(app: Application) {
        if (app.isMainProcess()) {
            dependencies = Dependencies(app = app)
        }
    }
}