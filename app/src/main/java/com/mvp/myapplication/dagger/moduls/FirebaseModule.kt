package com.mvp.myapplication.dagger.moduls

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by havil on 19.03.2018.
 */

@Module class FirebaseModule{

    @Provides @Singleton fun providesFirebaseFirestoreInstance(context : Context) = FirebaseFirestore.getInstance()
    @Provides @Singleton fun providesFirebaseAuthInstance(context : Context) = FirebaseAuth.getInstance()
    @Provides @Singleton fun providesFirebaseStorageInstance(context : Context) = FirebaseStorage.getInstance()
    @Provides @Singleton fun providesPhoneAuthProviderInstance(context: Context) = PhoneAuthProvider.getInstance()

}
