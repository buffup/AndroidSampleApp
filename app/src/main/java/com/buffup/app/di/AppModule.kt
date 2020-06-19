package com.buffup.app.di

import android.app.Application
import com.buffup.app.ui.splashactivity.SplashViewModel
import com.buffup.app.ui.streamactivity.streamlist.StreamListViewModel
import com.buffup.app.utils.StreamUITransformer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {

    single {
        StreamUITransformer(get<Application>().resources.displayMetrics)
    }
}

val viewModelModule = module {

    viewModel {
        SplashViewModel()
    }

    viewModel {
        StreamListViewModel(
            get()
        )
    }
}

val appModules = listOf(commonModule, viewModelModule)