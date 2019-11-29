package com.mvp.myapplication.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.mvp.myapplication.dagger.moduls.ApiModule
import com.mvp.myapplication.data.models.api.Requests
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Flowable.create
import io.reactivex.FlowableEmitter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by havil on 19.03.2018.
 */

@Singleton open class DataBaseNetworkHelper
@Inject constructor(
        private val firebaseFirestore: FirebaseFirestore,
        private val firebaseAuth : FirebaseAuth,
        private val phoneAuthProvider: PhoneAuthProvider,
        private val firebaseStorage: FirebaseStorage,
        var api: ApiModule
) : IDataBaseNetworkHelper {

    override fun getObjects(imageBase64: String): Observable<Response<ResponseBody>> {
        val listRequests =  ArrayList<Requests.GetObjectSend_requests>()
        val listFeatures =  ArrayList<Requests.GetObjectSend_requests_features>()
        val image = Requests.GetObjectSend_image(imageBase64)
        listFeatures.add(Requests.GetObjectSend_requests_features(50, "OBJECT_LOCALIZATION"))
        listRequests.add(Requests.GetObjectSend_requests(listFeatures, image))
        val objectSender = Requests.GetObjectSend(
            listRequests
        )

        return api.provideApiInstance()
            .getObjects(Gson().toJsonTree(objectSender))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getListCity(): Observable<Requests.Tsrs> {
        return api.provideApiInstance()
            .getListTsrs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTaxi(id: String): Observable<Requests.Tsrs> {
        return api.provideApiInstance()
            .getTsrOrg(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    override fun aaa(rest: String): Flowable<String> {
        return create({ subscribe: FlowableEmitter<String> ->

        }, BackpressureStrategy.BUFFER)
    }

}