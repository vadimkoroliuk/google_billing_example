package com.kcompany.billing.billing

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class Billing(
    private val billingConnector: BillingConnector,
    private val billingPurchasesUpdatedListener: BillingPurchasesUpdatedListener,
    private val purchaseAutoAcknowledge: PurchaseAutoAcknowledge,
) {

    private val _userState = MutableStateFlow<UserState>(value = UserState.Premium)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private suspend fun getSubsPurchasesList(type: String): List<Purchase> {
        val result =
            billingConnector.withConnectedBillingClient()
                .queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder()
                        .setProductType(type)
                        .build()
                )
        return if (result.billingResult.isSuccess()) {
            result.purchasesList
        } else {
            emptyList()
        }
    }

    suspend fun getSubscriptionDetails(id: String, type: String): Result<ProductDetails> {
        val result = billingConnector.withConnectedBillingClient().queryProductDetails(
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(id)
                            .setProductType(type)
                            .build()
                    )
                )
                .build()
        )
        return if (result.billingResult.isSuccess()) {
            val product = result.productDetailsList?.firstOrNull()
            if (product == null) {
                Result.failure(exception = NullPointerException("Product is null"))
            } else {
                Result.success(value = product)
            }

        } else {
            Result.failure(exception = IllegalStateException("Get subscription details"))
        }
    }

    suspend fun getSubsPurchases(): List<Purchase> =
        getSubsPurchasesList(BillingClient.ProductType.SUBS)

    suspend fun startBillingFlow(
        activity: Activity, productDetails: ProductDetails
    ) {
        val client = billingConnector.withConnectedBillingClient()
        val productDetailsParamsList = if (productDetails.subscriptionOfferDetails != null) {
            val offerToken = productDetails.subscriptionOfferDetails?.first()?.offerToken!!
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken)
                    .build()
            )
        } else {
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()
            )
        }

        client.launchBillingFlow(
            activity,
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()
        )
        withContext(Dispatchers.Main) {
            billingPurchasesUpdatedListener.purchases.collect {
                purchaseAutoAcknowledge.execute(client, it)
            }
        }
    }

    fun getIsPremium(): Boolean {
        return userState.value is UserState.Premium
    }

    fun setUserState(value: UserState) {
        _userState.update {
            value
        }
    }
}