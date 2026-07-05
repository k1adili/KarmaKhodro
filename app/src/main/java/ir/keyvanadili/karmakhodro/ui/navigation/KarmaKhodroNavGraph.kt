package ir.keyvanadili.karmakhodro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.keyvanadili.karmakhodro.data.Car
import ir.keyvanadili.karmakhodro.data.Repository
import ir.keyvanadili.karmakhodro.data.ServiceRecord
import ir.keyvanadili.karmakhodro.ui.screens.CarDetailScreen
import ir.keyvanadili.karmakhodro.ui.screens.CarFormScreen
import ir.keyvanadili.karmakhodro.ui.screens.CarListScreen
import ir.keyvanadili.karmakhodro.ui.screens.ServiceFormScreen
import ir.keyvanadili.karmakhodro.ui.screens.SettingsScreen
import ir.keyvanadili.karmakhodro.ui.viewmodel.MainViewModel

private object Routes {
    const val CAR_LIST = "car_list"
    const val CAR_ADD = "car_add"
    const val CAR_EDIT = "car_edit/{carId}"
    const val CAR_DETAIL = "car_detail/{carId}"
    const val RECORD_ADD = "record_add/{carId}"
    const val RECORD_EDIT = "record_edit/{carId}/{recordId}"
    const val SETTINGS = "settings"

    fun carEdit(carId: Long) = "car_edit/$carId"
    fun carDetail(carId: Long) = "car_detail/$carId"
    fun recordAdd(carId: Long) = "record_add/$carId"
    fun recordEdit(carId: Long, recordId: Long) = "record_edit/$carId/$recordId"
}

@Composable
fun KarmaKhodroNavGraph(repository: Repository) {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory(repository))
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Routes.CAR_LIST) {

        composable(Routes.CAR_LIST) {
            val cars by viewModel.cars.collectAsState()
            CarListScreen(
                cars = cars,
                onAddCarClick = { navController.navigate(Routes.CAR_ADD) },
                onCarClick = { car -> navController.navigate(Routes.carDetail(car.id)) },
                onSettingsClick = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable(Routes.CAR_ADD) {
            CarFormScreen(
                existingCar = null,
                onBack = { navController.popBackStack() },
                onSave = { car ->
                    viewModel.addCar(car) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(
            route = Routes.CAR_EDIT,
            arguments = listOf(navArgument("carId") { type = NavType.LongType })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getLong("carId") ?: 0L
            val carState = produceState<Car?>(initialValue = null, carId) {
                value = viewModel.getCarById(carId)
            }
            val car = carState.value
            if (car != null) {
                CarFormScreen(
                    existingCar = car,
                    onBack = { navController.popBackStack() },
                    onSave = { updated ->
                        viewModel.updateCar(updated)
                        navController.popBackStack()
                    },
                    onDelete = { toDelete ->
                        viewModel.deleteCar(toDelete)
                        navController.popBackStack(Routes.CAR_LIST, inclusive = false)
                    }
                )
            }
        }

        composable(
            route = Routes.CAR_DETAIL,
            arguments = listOf(navArgument("carId") { type = NavType.LongType })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getLong("carId") ?: 0L
            val car by viewModel.carByIdFlow(carId).collectAsState(initial = null)
            val records by viewModel.recordsForCar(carId).collectAsState(initial = emptyList())

            if (car != null) {
                CarDetailScreen(
                    car = car!!,
                    records = records,
                    onBack = { navController.popBackStack() },
                    onEditCar = { navController.navigate(Routes.carEdit(carId)) },
                    onAddRecord = { navController.navigate(Routes.recordAdd(carId)) },
                    onRecordClick = { record -> navController.navigate(Routes.recordEdit(carId, record.id)) },
                    onUpdateMileage = { newMileage ->
                        viewModel.updateCarMileage(context, car!!, newMileage)
                    }
                )
            }
        }

        composable(
            route = Routes.RECORD_ADD,
            arguments = listOf(navArgument("carId") { type = NavType.LongType })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getLong("carId") ?: 0L
            val carState = produceState<Car?>(initialValue = null, carId) {
                value = viewModel.getCarById(carId)
            }
            val car = carState.value
            if (car != null) {
                ServiceFormScreen(
                    carId = carId,
                    existingRecord = null,
                    onBack = { navController.popBackStack() },
                    onSave = { record ->
                        viewModel.addRecord(context, car, record) {
                            navController.popBackStack()
                        }
                    }
                )
            }
        }

        composable(
            route = Routes.RECORD_EDIT,
            arguments = listOf(
                navArgument("carId") { type = NavType.LongType },
                navArgument("recordId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getLong("carId") ?: 0L
            val recordId = backStackEntry.arguments?.getLong("recordId") ?: 0L
            val carState = produceState<Car?>(initialValue = null, carId) {
                value = viewModel.getCarById(carId)
            }
            val car = carState.value
            val records by viewModel.recordsForCar(carId).collectAsState(initial = emptyList())
            val record: ServiceRecord? = records.find { it.id == recordId }

            if (record != null && car != null) {
                ServiceFormScreen(
                    carId = carId,
                    existingRecord = record,
                    onBack = { navController.popBackStack() },
                    onSave = { updated ->
                        viewModel.updateRecord(context, car, updated)
                        navController.popBackStack()
                    },
                    onDelete = { toDelete ->
                        viewModel.deleteRecord(toDelete)
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                repository = repository,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
