package com.example.cupcake.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.R
import com.example.cupcake.data.DataSource
import com.example.cupcake.ui.viewmodels.OrderViewModel

@Composable
fun CupcakeApp(
    orderViewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.Start.name
    )
    Scaffold(
        topBar = {
            CupcakeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) {innerPadding->
        val orderUiState by orderViewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = Screens.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = Screens.Start.name
            ){
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions,
                    onNextButtonClicked = {
                        orderViewModel.setQuantity(it)
                        navController.navigate(Screens.Flavor.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(
                route = Screens.Flavor.name
            ){
                val context = LocalContext.current
                SelectOptionScreen(
                    subtotal = orderUiState.price,
                    options = DataSource.flavors.map { id->
                        context.resources.getString(id)
                    },
                    onSelectionChanged = {
                        orderViewModel.setFlavor(it)
                    },
                    modifier = Modifier.fillMaxHeight(),
                    onNextButtonClicked = {
                        navController.navigate(Screens.Pickup.name)
                    },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(orderViewModel,navController)
                    }
                )
            }
            composable(
                route = Screens.Pickup.name
            ){
                SelectOptionScreen(
                    subtotal = orderUiState.price,
                    options = orderUiState.pickupOptions,
                    onSelectionChanged = {
                        orderViewModel.setPickupDate(it)
                    },
                    modifier = Modifier.fillMaxHeight(),
                    onNextButtonClicked = {
                        navController.navigate(Screens.Summary.name)
                    },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(orderViewModel,navController)
                    }
                )
            }
            composable(
                route = Screens.Summary.name
            ){
                val context = LocalContext.current
                SummaryScreen(
                    orderUiState = orderUiState,
                    modifier = Modifier.fillMaxHeight(),
                    onSendButtonClicked = {subject:String,summary:String->
                        shareOrder(context,subject,summary)
                    },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(orderViewModel,navController)
                    }
                )
            }
        }
    }
}

private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(Screens.Start.name, inclusive = false)
}

private fun shareOrder(
    context: Context,
    subject: String,
    summary: String
){
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }

    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_cupcake_order)
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupcakeAppBar(
    currentScreen: Screens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = currentScreen.title)) },
        modifier = modifier,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            if(canNavigateBack){
                IconButton(
                    onClick = navigateUp
                ){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        }
    )
}
