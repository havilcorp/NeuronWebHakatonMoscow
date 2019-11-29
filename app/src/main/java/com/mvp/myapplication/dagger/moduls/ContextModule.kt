package com.mvp.myapplication.dagger.moduls

import android.content.Context
import com.mvp.myapplication.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class ContextModule (private val application: App){

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application
    }

}