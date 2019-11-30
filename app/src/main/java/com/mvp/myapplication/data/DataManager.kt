package com.mvp.myapplication.data

import com.google.gson.Gson
import com.mvp.myapplication.data.local.DataBaseLocalHelper
import com.mvp.myapplication.data.models.api.Requests
import com.mvp.myapplication.data.network.DataBaseNetworkHelper
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton open class DataManager @Inject constructor(
    private var dataBaseLocalHelper: DataBaseLocalHelper,
    private val dataBaseNetworkHelper: DataBaseNetworkHelper
): IDataManager {

    override fun getObjects(imageBase64: String, handler: IAppCallback<Requests.OBJECT_LOCALIZATION>): Disposable {
        return dataBaseNetworkHelper.getObjects(imageBase64).subscribe(
                { it.body()?.let { body -> handler.onSuccess(Gson().fromJson(body.string(), Requests.OBJECT_LOCALIZATION::class.java)) } },
                { handler.onFailure(it.message, it) }
        )
    }

    override fun getDetailObjects(
        imageBase64: String,
        handler: IAppCallback<Requests.OBJECT_DETECTION>
    ): Disposable {
        return dataBaseNetworkHelper.getDetailObjects(imageBase64).subscribe(
            {
                it.body()?.let { body ->
                    //Log.d("TAG", body.string())
                    handler.onSuccess(Gson().fromJson(body.string(), Requests.OBJECT_DETECTION::class.java))
                }
            },
            { handler.onFailure(it.message, it) }
        )
    }

}