package elieoko.mobile.luka.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import elieoko.mobile.luka.core.AppConfig
import elieoko.mobile.luka.core.CrashReporter
import elieoko.mobile.luka.core.createHttpClient
import elieoko.mobile.luka.core.createPushNotifier
import elieoko.mobile.luka.data.local.LukaDatabase
import elieoko.mobile.luka.data.local.createLukaDatabaseBuilder
import elieoko.mobile.luka.data.local.createSessionDataStore
import elieoko.mobile.luka.data.platform.SentryCrashReporter
import elieoko.mobile.luka.data.remote.OfferStream
import elieoko.mobile.luka.data.remote.StompOfferStream
import elieoko.mobile.luka.data.repository.CatalogRepositoryImpl
import elieoko.mobile.luka.data.repository.SessionRepositoryImpl
import elieoko.mobile.luka.domain.repository.CatalogRepository
import elieoko.mobile.luka.domain.repository.SessionRepository
import elieoko.mobile.luka.domain.usecase.CompleteLocationUseCase
import elieoko.mobile.luka.domain.usecase.CompleteProfessionUseCase
import elieoko.mobile.luka.domain.usecase.LaunchInfiniteAnalysisUseCase
import elieoko.mobile.luka.domain.usecase.RequestOtpUseCase
import elieoko.mobile.luka.domain.usecase.ResolveDestinationUseCase
import elieoko.mobile.luka.domain.usecase.SelectPlanUseCase
import elieoko.mobile.luka.domain.usecase.UpdateProfileUseCase
import elieoko.mobile.luka.domain.usecase.VerifyOtpUseCase
import elieoko.mobile.luka.presentation.auth.AuthViewModel
import elieoko.mobile.luka.presentation.home.HomeViewModel
import elieoko.mobile.luka.presentation.profile.ProfileViewModel
import elieoko.mobile.luka.presentation.root.RootViewModel
import elieoko.mobile.luka.presentation.setup.SetupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    if (org.koin.core.context.GlobalContext.getOrNull() != null) return
    startKoin {
        appDeclaration()
        modules(lukaModule)
    }
}

val lukaModule = module {
    single { AppConfig() }
    single { Json { ignoreUnknownKeys = true; isLenient = true } }
    single { createHttpClient() }
    single<CrashReporter> { SentryCrashReporter(get()) }
    single { createPushNotifier(get()) }
    single { createSessionDataStore() }
    single {
        val builder: RoomDatabase.Builder<LukaDatabase> = createLukaDatabaseBuilder()
        builder
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single<OfferStream> { StompOfferStream(get(), get(), get(), get()) }
    singleOf(::SessionRepositoryImpl) bind SessionRepository::class
    singleOf(::CatalogRepositoryImpl) bind CatalogRepository::class
    factoryOf(::ResolveDestinationUseCase)
    factoryOf(::RequestOtpUseCase)
    factoryOf(::VerifyOtpUseCase)
    factoryOf(::CompleteProfessionUseCase)
    factoryOf(::CompleteLocationUseCase)
    factoryOf(::LaunchInfiniteAnalysisUseCase)
    factoryOf(::UpdateProfileUseCase)
    factoryOf(::SelectPlanUseCase)
    viewModelOf(::RootViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::SetupViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProfileViewModel)
}
