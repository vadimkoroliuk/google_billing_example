package com.kcompany.billing.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails

fun BillingResult.isSuccess() = responseCode == BillingClient.BillingResponseCode.OK

fun ProductDetails.getOfferDetails(): ProductDetails.SubscriptionOfferDetails? {
    return this.subscriptionOfferDetails?.first()
}