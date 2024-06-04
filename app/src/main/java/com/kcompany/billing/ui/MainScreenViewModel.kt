package com.kcompany.billing.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kcompany.billing.billing.Product
import com.kcompany.billing.billing.Products
import com.kcompany.billing.billing.getOfferDetails
import com.kcompany.billing.di.DI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel() : ViewModel() {

    val billing = DI.getDependencies().appModule.billingModule.billing

    val screenState = MutableStateFlow(value = ScreenState(products = emptyList()))

    init {
        viewModelScope.launch {
            delay(3000)
            billing.getSubscriptionDetails(id = Products.SUB_ID).also { productDetails ->
                val value = productDetails.getOrNull()
                val offerDetails = value?.getOfferDetails()
                if (value != null && offerDetails != null) {
                    value.productId
                    val product = Product(
                        productId = value.productId,
                        title = value.title,
                        description = value.description,
                        name = value.name,
                        productType = value.productType,
                        offerId = offerDetails.offerId,
                        offerToken = offerDetails.offerToken
                    )
                    Log.e("!!!!", "onCreate: ${product}")
                    screenState.update {
                        it.copy(products = listOf(product))
                    }
                }
            }
        }
    }
}