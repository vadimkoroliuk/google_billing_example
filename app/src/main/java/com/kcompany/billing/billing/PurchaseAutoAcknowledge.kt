package com.kcompany.billing.billing

import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase

class PurchaseAutoAcknowledge(
    private val billingClient: BillingClient,
) {

    fun execute(billingClient: BillingClient, purchaseList: List<Purchase>) {
        purchaseList.filter { !it.isAcknowledged }.forEach { purchase ->
            billingClient.acknowledgePurchase(
                AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken)
                    .build()
            ) {}
        }
    }
}
