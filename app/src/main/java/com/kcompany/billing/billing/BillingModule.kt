package com.kcompany.billing.billing

import android.app.Application
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PendingPurchasesParams

class BillingModule(app: Application) {

    private val billingPurchasesUpdatedListener by lazy {
        BillingPurchasesUpdatedListener()
    }

    private val billingClient by lazy {
        BillingClient
            .newBuilder(app)
            .setListener(billingPurchasesUpdatedListener)
            .enablePendingPurchases(PendingPurchasesParams.newBuilder().build())
            .build()
    }

    val billing by lazy {
        Billing(
            billingConnector = BillingConnector(billingClient = billingClient),
            billingPurchasesUpdatedListener = billingPurchasesUpdatedListener,
            purchaseAutoAcknowledge = PurchaseAutoAcknowledge(billingClient = billingClient),
        )
    }
}