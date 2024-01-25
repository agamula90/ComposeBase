package ua.com.underlake

import android.app.Application
import com.mapbox.common.MapboxOptions
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import ua.com.underlake.inject.AppModule

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        MapboxOptions.accessToken = BuildConfig.MAPBOX_TOKEN
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(AppModule().module)
        }
    }
}