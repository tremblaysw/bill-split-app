package ca.tremblay95.billsplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.tremblay95.billsplit.presentation.create_split.CreateSplitDestination
import ca.tremblay95.billsplit.presentation.create_split.CreateSplitScreen
import ca.tremblay95.billsplit.presentation.split_list.SplitListDestination
import ca.tremblay95.billsplit.presentation.split_list.SplitListScreen
import ca.tremblay95.billsplit.ui.theme.BillSplitAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BillSplitAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController : NavHostController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = SplitListDestination.route
                    ) {
                        composable(SplitListDestination.route) {
                            SplitListScreen(
                                navigateToCreateSplitScreen = {
                                    navController.navigate(CreateSplitDestination.route)
                                },
                                navigateToSplitDetailsScreen = { }
                            )
                        }
                        composable(CreateSplitDestination.route) {
                            CreateSplitScreen(
                                navigateBack = { navController.popBackStack() },
                                onNavigateUp = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}
