package ir.keyvanadili.karmakhodro.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM cars ORDER BY id DESC")
    fun getAllCars(): Flow<List<Car>>

    @Query("SELECT * FROM cars WHERE id = :carId")
    suspend fun getCarById(carId: Long): Car?

    @Query("SELECT * FROM cars WHERE id = :carId")
    fun getCarByIdFlow(carId: Long): Flow<Car?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(car: Car): Long

    @Update
    suspend fun update(car: Car)

    @Delete
    suspend fun delete(car: Car)

    @Query("DELETE FROM cars")
    suspend fun deleteAll()
}
