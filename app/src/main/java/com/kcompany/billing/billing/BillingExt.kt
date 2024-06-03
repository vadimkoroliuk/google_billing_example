package com.kcompany.billing.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult

fun BillingResult.isSuccess() = responseCode == BillingClient.BillingResponseCode.OK