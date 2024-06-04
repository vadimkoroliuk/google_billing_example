package com.kcompany.billing.billing

import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.kcompany.billing.billing.errors.BillingUnavailableException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BillingConnector(private val billingClient: BillingClient) {

    companion object {
        private const val RETRY_MAX_COUNT = 3
    }

    val isConnected = AtomicBoolean(false)

    private var connectionTry = 0

    private val mutex = Mutex()

    suspend fun withConnectedBillingClient(): BillingClient {
        if (billingClient.isReady) {
            return billingClient
        }
        mutex.withLock {
            while (connectionTry <= RETRY_MAX_COUNT) {
                connectionTry++
                if (runCatching { connectBillingClient(billingClient) }.isSuccess) {
                    return billingClient
                }
            }
            throw BillingUnavailableException()
        }
    }

    private suspend fun connectBillingClient(billingClient: BillingClient): BillingClient {
        return suspendCoroutine {
            if (billingClient.isReady) {
                it.resume(billingClient)
            } else {
                billingClient.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(result: BillingResult) {
                        try {
                            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                                isConnected.set(true)
                                Log.e("!!!", "onBillingSetupFinished: connected", )
                                it.resume(billingClient)
                            } else {
                                isConnected.set(false)
                                Log.e("!!!", "onBillingSetupFinished: not connected", )
                                it.resumeWithException(BillingUnavailableException())
                            }
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        isConnected.set(false)
                        Log.e("!!!", "onBillingServiceDisconnected", )
                        Unit
                    }
                })
            }
        }
    }
}
