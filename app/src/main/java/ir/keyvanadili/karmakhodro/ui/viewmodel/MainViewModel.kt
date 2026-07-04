package ir.keyvanadili.karmakhodro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ir.keyvanadili.karmakhodro.data.Car
import ir.keyvanadili.karmakhodro.data.Repository
import ir.keyvanadili.karmakhodro.data.ServiceRecord
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    val cars: StateFlow<List<Car>> = repository.getAllCars()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun recordsForCar(carId: Long) = repository.getRecordsForCar(carId)

    fun addCar(car: Car, onDone: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.addCar(car)
            onDone(id)
        }
    }

    fun updateCar(car: Car) {
        viewModelScope.launch { repository.updateCar(car) }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch { repository.deleteCar(car) }
    }

    fun addRecord(record: ServiceRecord, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addRecord(record)
            onDone()
        }
    }

    fun updateRecord(record: ServiceRecord) {
        viewModelScope.launch { repository.updateRecord(record) }
    }

    fun deleteRecord(record: ServiceRecord) {
        viewModelScope.launch { repository.deleteRecord(record) }
    }

    suspend fun getCarById(id: Long): Car? = repository.getCarById(id)

    class Factory(private val repository: Repository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }
}
