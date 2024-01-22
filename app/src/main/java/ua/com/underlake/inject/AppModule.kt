package ua.com.underlake.inject

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ua.com.underlake.remote.DogService

@Module
@ComponentScan("ua.com.underlake")
class AppModule {
    @Single
    fun provideRetrofit(): Retrofit = with(Retrofit.Builder()){
        baseUrl("https://api.thedogapi.com/v1/")
        addConverterFactory(MoshiConverterFactory.create())
        build()
    }

    @Factory
    fun provideDogService(retrofit: Retrofit): DogService = retrofit.create(DogService::class.java)
}