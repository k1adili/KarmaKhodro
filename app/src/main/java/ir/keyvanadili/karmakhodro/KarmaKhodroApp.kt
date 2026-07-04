package ir.keyvanadili.karmakhodro

import android.app.Application
import ir.keyvanadili.karmakhodro.data.AppDatabase
import ir.keyvanadili.karmakhodro.data.Repository

class KarmaKhodroApp : Application() {

    lateinit var repository: Repository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(this)
        repository = Repository(db.carDao(), db.serviceRecordDao())
    }
}
