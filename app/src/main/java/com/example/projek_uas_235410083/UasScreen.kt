package com.example.projek_uas_235410083

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.projek_uas_235410083.data.DataSource
import com.example.projek_uas_235410083.ui.OrderSummaryScreen
import com.example.projek_uas_235410083.ui.OrderViewModel
import com.example.projek_uas_235410083.ui.SelectOptionScreen
import com.example.projek_uas_235410083.ui.StartOrderScreen

/** enum yang mewakili layar di aplikasi*/
enum class UasScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Flavor(title = R.string.choose_flavor),
    Pickup(title = R.string.choose_pickup_date),
    Summary(title = R.string.order_summary)
}

/**Composable menampilkan TopBar dan menampilkan tombol kembali
jika navigasi kembali dimungkinkan. */
@Composable
fun UasAppBar(
    currentScreen: UasScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun UasApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    /**Dapatkan entri back stack saat ini*/
    val backStackEntry by navController.currentBackStackEntryAsState()

    /** Dapatkan nama layar saat ini*/
    val currentScreen = UasScreen.valueOf(
        backStackEntry?.destination?.route ?: UasScreen.Start.name
    )

    Scaffold(
        topBar = {
            UasAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = UasScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            /**composable untuk layar mulai / layar pertama*/
            composable(route = UasScreen.Start.name) {

                /**  fungsi yang memanggil navigate()
                 * saat pengguna menekan tombol pada layar Start, Flavor, dan Pickup*/
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions,
                    onNextButtonClicked = {
                        viewModel.setQuantity(it)
                        navController.navigate(UasScreen.Flavor.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = UasScreen.Flavor.name) {
                val context = LocalContext.current
                SelectOptionScreen(
                    subtotal = uiState.price,

                    /**  parameter onNextButtonClicked di layar rasa, cukup teruskan lambda yang
                     * memanggil navigate(), dengan meneruskan CupcakeScreen.Pickup.name untuk route*/
                    onNextButtonClicked = { navController.navigate(UasScreen.Pickup.name) },

                    /** Teruskan lambda kosong untuk onCancelButtonClicked yang akan Anda
                     * implementasikan berikutnya.*/
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    options = DataSource.flavors.map { id -> context.resources.getString(id) },
                    onSelectionChanged = { viewModel.setFlavor(it) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = UasScreen.Pickup.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    onNextButtonClicked = { navController.navigate(UasScreen.Summary.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    options = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = UasScreen.Summary.name) {
                val context = LocalContext.current

                /** OrderSummaryScreen, meneruskan lambda kosong untuk onCancelButtonClicked dan
                 * onSendButtonClicked. Parameter subject dan summary akan diteruskan ke
                 * onSendButtonClicked */
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    onSendButtonClicked = { subject: String, summary: String ->
                        shareOrder(context, subject = subject, summary = summary)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

/** Mengatur ulang [OrderUIState] dan muncul ke [UasScreen.start] */
private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(UasScreen.Start.name, inclusive = false)
}

/** Membuat intent untuk berbagi detail pesanan*/
private fun shareOrder(context: Context, subject: String, summary: String) {
/**Buat implicit intent ACTION_SEND dengan detail pesanan di intent extras*/
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
