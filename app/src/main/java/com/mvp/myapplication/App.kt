package com.mvp.myapplication

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.mvp.myapplication.dagger.components.DaggerIDaggerComponent
import com.mvp.myapplication.dagger.components.IDaggerComponent
import com.mvp.myapplication.dagger.moduls.ApiModule
import com.mvp.myapplication.dagger.moduls.ContextModule
import com.mvp.myapplication.dagger.moduls.FirebaseModule
import javax.inject.Inject

class App: MultiDexApplication() {
    @Inject
    lateinit var  context: Context
    lateinit var component: IDaggerComponent

    companion object {
        @SuppressLint("StaticFieldLeak") lateinit var instance: App
        operator fun get(context: Context) = context.applicationContext as App
    }

    override fun onCreate() {
        instance = this
        super.onCreate()

        component = DaggerIDaggerComponent.builder()
            .contextModule(ContextModule(this))
            .firebaseModule(FirebaseModule())
            .apiModule(ApiModule())
            .build()

        component.inject(this)

        FirebaseApp.initializeApp(context)

    }
}
