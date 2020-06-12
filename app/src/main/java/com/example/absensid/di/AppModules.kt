package com.example.absensid.di

import com.example.absensid.ui.permission.PermissionVm
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    //SharedPreferences
//    single { Preferences(get()) }

    //Repository
    /*factory {
        (scope: CoroutineScope) -> AuthRepo(scope = scope)
    }*/

    //ViewModel
    viewModel { PermissionVm() }
}