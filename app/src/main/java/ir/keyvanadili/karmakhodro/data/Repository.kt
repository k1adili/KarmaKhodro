package ir.keyvanadili.karmakhodro.data

import kotlinx.coroutines.flow.Flow

class Repository(
    private val carDao: CarDao,
    private val serviceRecordDao: ServiceRecordDao
) {
    // خودروها
    fun getAllCars(): Flow<List<Car>> = carDao.getAllCars()
    suspend fun getCarById(id: Long): Car? = carDao.getCarById(id)
    suspend fun addCar(car: Car): Long = carDao.insert(car)
    suspend fun updateCar(car: Car) = carDao.update(car)
    suspend fun deleteCar(car: Car) = carDao.delete(car)

    // رکوردهای سرویس
    fun getRecordsForCar(carId: Long): Flow<List<ServiceRecord>> = serviceRecordDao.getRecordsForCar(carId)
    fun getAllRecords(): Flow<List<ServiceRecord>> = serviceRecordDao.getAllRecords()
    suspend fun addRecord(record: ServiceRecord): Long = serviceRecordDao.insert(record)
    suspend fun updateRecord(record: ServiceRecord) = serviceRecordDao.update(record)
    suspend fun deleteRecord(record: ServiceRecord) = serviceRecordDao.delete(record)

    // برای پشتیبان‌گیری/بازیابی کامل
    suspend fun replaceAllData(cars: List<Car>, records: List<ServiceRecord>) {
        carDao.deleteAll() // با CASCADE رکوردهای سرویس هم پاک می‌شوند
        cars.forEach { carDao.insert(it) }
        records.forEach { serviceRecordDao.insert(it) }
    }
}
