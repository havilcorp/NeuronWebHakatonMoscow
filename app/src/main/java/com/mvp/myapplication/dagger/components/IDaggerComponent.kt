package com.mvp.myapplication.dagger.components

import com.mvp.myapplication.App
import com.mvp.myapplication.dagger.moduls.ApiModule
import com.mvp.myapplication.dagger.moduls.ContextModule
import com.mvp.myapplication.dagger.moduls.FirebaseModule
import com.mvp.myapplication.data.DataManager
import com.mvp.myapplication.data.local.DataBaseLocalHelper
import com.mvp.myapplication.data.network.DataBaseNetworkHelper
import com.mvp.myapplication.ui.logo.LogoActivity
import com.mvp.myapplication.ui.main.MainActivity
import com.mvp.myapplication.ui.web.WebActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class,  FirebaseModule::class, ApiModule::class])
interface IDaggerComponent {

    fun inject(app: App)
    fun inject(activity: WebActivity)
    fun inject(activity: LogoActivity)
    fun inject(activity: MainActivity)

    var dataManager : DataManager
    var dataBaseNetworkHelper : DataBaseNetworkHelper
    var dataBaseLocalHelper : DataBaseLocalHelper

}
