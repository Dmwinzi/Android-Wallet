package com.example.wallet

import android.app.Application
import androidx.room.Room
import androidx.work.WorkManager
import com.example.wallet.data.datastore.UserPreferenceManager
import com.example.wallet.data.localDataSource.dao.TransactionDao
import com.example.wallet.data.localDataSource.walletDatabase.WalletDatabase
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
import android.content.Context
import com.example.wallet.domain.usecases.GetLocalTransactionsUseCase
import com.example.wallet.domain.usecases.GetStatementUseCase
import com.example.wallet.domain.usecases.RetryLocalTransactionUseCase
import dagger.hilt.android.qualifiers.ApplicationContext


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
        prefs: UserPreferenceManager,
        transactionDao: TransactionDao,
        workManager: WorkManager
    ): CustomerRepository = CustomerRepositoryImpl(
        api, prefs,
        transactionDao = transactionDao,
        workManager = workManager
    )

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: CustomerRepository): LoginUseCase =
        LoginUseCase(repository)

    @Provides
    @Singleton
    fun provideGetBalanceUseCase(repository: CustomerRepository): GetBalanceUseCase =
        GetBalanceUseCase(repository)

    @Provides
    @Singleton
    fun provideGetStatementUseCase(
        repository: CustomerRepository,
        userPrefs: UserPreferenceManager
    ): GetStatementUseCase = GetStatementUseCase(repository, userPrefs)

    @Provides
    @Singleton
    fun provideWalletDatabase(app: Application): WalletDatabase {
        return Room.databaseBuilder(
            app,
            WalletDatabase::class.java,
            "wallet_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTransactionDao(db: WalletDatabase): TransactionDao {
        return db.transactionDao()
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideGetLocalTransactionsUseCase(
        repository: CustomerRepository
    ): GetLocalTransactionsUseCase {
        return GetLocalTransactionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRetryLocalTransactionUseCase(
        repository: CustomerRepository
    ): RetryLocalTransactionUseCase {
        return RetryLocalTransactionUseCase(repository)
    }

}
