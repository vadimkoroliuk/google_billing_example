package com.kcompany.billing.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.kcompany.billing.di.DI
import com.kcompany.billing.ui.theme.BillingExampleTheme

class MainActivity : ComponentActivity() {

    val billing = DI.getDependencies().appModule.billingModule.billing

    private lateinit var viewModel: MainScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class)
        enableEdgeToEdge()


        setContent {
            BillingExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    innerPadding.calculateTopPadding()
                    Greeting(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    viewModel: MainScreenViewModel,
) {
    val screenState = viewModel.screenState.collectAsState()
    Spacer(modifier = Modifier.height(height = 32.dp))
    Text(
        text = "Hello ${screenState.value.products}!",
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .verticalScroll(state = rememberScrollState())
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BillingExampleTheme {
        // Greeting("Android")
    }
}