package com.kcompany.billing.ui

import com.android.billingclient.api.ProductDetails
import com.kcompany.billing.billing.Product

data class ScreenState(
    val products: List<Product> = emptyList()
)