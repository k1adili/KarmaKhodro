package ir.keyvanadili.karmakhodro.backup

import android.content.Context
import android.net.Uri
import ir.keyvanadili.karmakhodro.data.Car
import ir.keyvanadili.karmakhodro.data.Repository
import ir.keyvanadili.karmakhodro.data.ServiceRecord
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * مسئول تهیه فایل پشتیبان (Backup) به فرمت JSON از کل دیتابیس
 * و بازیابی (Restore) اطلاعات از روی آن فایل.
 */
class BackupManager(private val repository: Repository) {

    suspend fun exportBackup(context: Context, uri: Uri) {
        val cars = repository.getAllCars().first()
        val records = repository.getAllRecords().first()

        val root = JSONObject()
        root.put("app", "KarmaKhodro")
        root.put("backupVersion", 1)
        root.put("exportedAt", System.currentTimeMillis())

        val carsArray = JSONArray()
        cars.forEach { car ->
            val obj = JSONObject()
            obj.put("id", car.id)
            obj.put("name", car.name)
            obj.put("brandModel", car.brandModel)
            obj.put("plateNumber", car.plateNumber)
            obj.put("year", car.year ?: JSONObject.NULL)
            obj.put("color", car.color)
            obj.put("currentMileage", car.currentMileage)
            obj.put("notes", car.notes)
            carsArray.put(obj)
        }
        root.put("cars", carsArray)

        val recordsArray = JSONArray()
        records.forEach { record ->
            val obj = JSONObject()
            obj.put("id", record.id)
            obj.put("carId", record.carId)
            obj.put("dateMillis", record.dateMillis)
            obj.put("title", record.title)
            obj.put("description", record.description)
            obj.put("mileage", record.mileage)
            obj.put("cost", record.cost)
            obj.put("garageName", record.garageName)
            obj.put("nextServiceMileage", record.nextServiceMileage ?: JSONObject.NULL)
            obj.put("nextServiceDateMillis", record.nextServiceDateMillis ?: JSONObject.NULL)
            recordsArray.put(obj)
        }
        root.put("records", recordsArray)

        context.contentResolver.openOutputStream(uri)?.use { out ->
            out.write(root.toString(2).toByteArray(Charsets.UTF_8))
        }
    }

    suspend fun importBackup(context: Context, uri: Uri) {
        val text = context.contentResolver.openInputStream(uri)?.use { input ->
            BufferedReader(InputStreamReader(input, Charsets.UTF_8)).readText()
        } ?: throw IllegalStateException("امکان خواندن فایل وجود ندارد")

        val root = JSONObject(text)

        val carsArray = root.optJSONArray("cars") ?: JSONArray()
        val cars = mutableListOf<Car>()
        for (i in 0 until carsArray.length()) {
            val obj = carsArray.getJSONObject(i)
            cars.add(
                Car(
                    id = obj.optLong("id", 0),
                    name = obj.optString("name"),
                    brandModel = obj.optString("brandModel"),
                    plateNumber = obj.optString("plateNumber"),
                    year = if (obj.isNull("year")) null else obj.optInt("year"),
                    color = obj.optString("color"),
                    currentMileage = obj.optInt("currentMileage"),
                    notes = obj.optString("notes")
                )
            )
        }

        val recordsArray = root.optJSONArray("records") ?: JSONArray()
        val records = mutableListOf<ServiceRecord>()
        for (i in 0 until recordsArray.length()) {
            val obj = recordsArray.getJSONObject(i)
            records.add(
                ServiceRecord(
                    id = obj.optLong("id", 0),
                    carId = obj.optLong("carId"),
                    dateMillis = obj.optLong("dateMillis"),
                    title = obj.optString("title"),
                    description = obj.optString("description"),
                    mileage = obj.optInt("mileage"),
                    cost = obj.optLong("cost"),
                    garageName = obj.optString("garageName"),
                    nextServiceMileage = if (obj.isNull("nextServiceMileage")) null else obj.optInt("nextServiceMileage"),
                    nextServiceDateMillis = if (obj.isNull("nextServiceDateMillis")) null else obj.optLong("nextServiceDateMillis")
                )
            )
        }

        repository.replaceAllData(cars, records)
    }
}
