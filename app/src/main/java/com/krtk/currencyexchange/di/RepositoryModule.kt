package com.krtk.currencyexchange.di

import com.krtk.currencyexchange.dataLayer.repository.ExchangeRepositoryImpl
import com.krtk.currencyexchange.domainLayer.ExchangeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesMovieRepository(exchangeRepositoryImpl: ExchangeRepositoryImpl): ExchangeRepository

}
