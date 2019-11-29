package com.mvp.myapplication.data

import com.mvp.myapplication.data.models.api.Requests
import io.reactivex.disposables.Disposable

interface IDataManager {

    fun getObjects(imageBase64: String, handler: IAppCallback<Requests.OBJECT_LOCALIZATION>): Disposable
}