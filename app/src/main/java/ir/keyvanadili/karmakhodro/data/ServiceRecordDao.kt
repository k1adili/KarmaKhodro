package ir.keyvanadili.karmakhodro.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceRecordDao {
    @Query("SELECT * FROM service_records WHERE carId = :carId ORDER BY dateMillis DESC")
    fun getRecordsForCar(carId: Long): Flow<List<ServiceRecord>>

    @Query("SELECT * FROM service_records ORDER BY dateMillis DESC")
    fun getAllRecords(): Flow<List<ServiceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: ServiceRecord): Long

    @Update
    suspend fun update(record: ServiceRecord)

    @Delete
    suspend fun delete(record: ServiceRecord)

    @Query("DELETE FROM service_records")
    suspend fun deleteAll()
}
