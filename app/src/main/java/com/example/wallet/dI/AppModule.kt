package com.example.wallet

import android.app.Application
import com.example.wallet.data.datastore.UserPreferenceManager
import com.example.wallet.data.remoteDataSource.WalletApiService
import com.example.wallet.domain.repository.CustomerRepository
import com.example.wallet.domain.usecases.LoginUseCase
import com.example.wallet.data.repository.CustomerRepositoryImpl
import com.example.wallet.domain.usecases.GetBalanceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserPreferenceManager(app: Application): UserPreferenceManager =
        UserPreferenceManager(app)

    @Provides
    @Singleton
    fun provideWalletApiService(): WalletApiService =
        Retrofit.Builder()
            .baseUrl("http://192.168.0.103:8092/springboot-rest-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WalletApiService::class.java)

    @Provides
    @Singleton
    fun provideCustomerRepository(
        api: WalletApiService,
        prefs: UserPreferenceManager
    ): CustomerRepository = CustomerRepositoryImpl(api,prefs)

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: CustomerRepository): LoginUseCase =
        LoginUseCase(repository)

    @Provides
    @Singleton
    fun provideGetBalanceUseCase(repository: CustomerRepository): GetBalanceUseCase =
        GetBalanceUseCase(repository)
}
