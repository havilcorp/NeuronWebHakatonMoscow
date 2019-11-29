package com.mvp.myapplication.data.local

import android.annotation.SuppressLint
import android.content.Context
import com.mvp.myapplication.data.network.CODE
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Flowable.create
import io.reactivex.FlowableEmitter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton open class DataBaseLocalHelper
@Inject constructor(): IDataBaseLocalHelper {

    @SuppressLint("CommitPrefEdits")
    override fun saveLocalData(context: Context, key: String, data: String): Flowable<CODE> {
        return create({ subscribe: FlowableEmitter<CODE> ->
            val sharPref = context.getSharedPreferences("LAMP", Context.MODE_PRIVATE).edit()
            sharPref.putString(key, data)
            sharPref.apply()
            subscribe.onNext(CODE.SUCCESS)
        }, BackpressureStrategy.BUFFER)
    }

    override fun getLocalData(context: Context, key: String): Flowable<String> {
        return create({ subscribe: FlowableEmitter<String> ->
            context.getSharedPreferences("LAMP", Context.MODE_PRIVATE).getString(key, "")?.let {
                subscribe.onNext(it)
            }
        }, BackpressureStrategy.BUFFER)
    }

    override fun saveLocalDataBool(context: Context, key: String, data: Boolean): Flowable<CODE> {
        return create({ subscribe: FlowableEmitter<CODE> ->
            val sharPref = context.getSharedPreferences("LAMP", Context.MODE_PRIVATE).edit()
            sharPref.putBoolean(key, data)
            sharPref.apply()
            subscribe.onNext(CODE.SUCCESS)
        }, BackpressureStrategy.BUFFER)
    }

    override fun getLocalDataBool(context: Context, key: String): Flowable<Boolean> {
        return create({ subscribe: FlowableEmitter<Boolean> ->
            context.getSharedPreferences("LAMP", Context.MODE_PRIVATE).getBoolean(key, false).let {
                subscribe.onNext(it)
            }
        }, BackpressureStrategy.BUFFER)
    }

}