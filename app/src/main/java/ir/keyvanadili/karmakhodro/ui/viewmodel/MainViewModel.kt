package ir.keyvanadili.karmakhodro.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ir.keyvanadili.karmakhodro.data.Car
import ir.keyvanadili.karmakhodro.data.Repository
import ir.keyvanadili.karmakhodro.data.ServiceRecord
import ir.keyvanadili.karmakhodro.notification.NotificationHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

    fun addRecord(context: Context, car: Car, record: ServiceRecord, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addRecord(record)
            checkReminderForRecord(context, car, record, car.currentMileage)
            onDone()
        }
    }

    fun updateRecord(context: Context, car: Car, record: ServiceRecord) {
        viewModelScope.launch {
            repository.updateRecord(record)
            checkReminderForRecord(context, car, record, car.currentMileage)
        }
    }

    fun deleteRecord(record: ServiceRecord) {
        viewModelScope.launch { repository.deleteRecord(record) }
    }

    suspend fun getCarById(id: Long): Car? = repository.getCarById(id)

    /**
     * ثبت کیلومتر فعلی جدید برای یک خودرو و بررسی اینکه آیا به سررسید
     * کیلومتر سرویسِ ثبت‌شده در یکی از رویدادها رسیده یا خیر؛ در صورت رسیدن، نوتیف ارسال می‌شود.
     */
    fun updateCarMileage(context: Context, car: Car, newMileage: Int, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val updated = car.copy(currentMileage = newMileage)
            repository.updateCar(updated)

            val records = repository.getRecordsForCar(car.id).first()
            records.forEach { record ->
                checkReminderForRecord(context, updated, record, newMileage)
            }
            onDone()
        }
    }

    private suspend fun checkReminderForRecord(
        context: Context,
        car: Car,
        record: ServiceRecord,
        currentMileage: Int
    ) {
        val due = record.nextServiceMileage
        if (due != null && !record.nextServiceNotified && currentMileage >= due) {
            NotificationHelper.showServiceDueNotification(
                context = context,
                notificationId = record.id.toInt(),
                carName = car.name,
                currentMileage = currentMileage,
                dueMileage = due
            )
            repository.updateRecord(record.copy(nextServiceNotified = true))
        }
    }

    class Factory(private val repository: Repository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }
}
