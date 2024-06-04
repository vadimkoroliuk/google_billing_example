package com.kcompany.billing.billing

data class Product(
    val productId: String,
    val title: String,
    val description: String,
    val name: String,
    val productType: String,
    val offerId: String?,
    val offerToken: String,
)
