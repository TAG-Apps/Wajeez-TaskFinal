package com.wajeez.sample.model.api

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.wajeez.sample.model.data.UserAdapter
import com.wajeez.sample.model.data.UserModel
import com.wajeez.sample.model.utils.AppUtils
import com.wajeez.sample.model.utils.Resource
import com.wajeez.sample.model.utils.USERS
import com.wajeez.sample.model.utils.USERS_IMAGES_REFERENCE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ApiHelperImpl @Inject constructor(private val appUtils: AppUtils) : ApiHelper {

    override suspend fun getUsers() = flow<Resource<List<UserModel>>> {

        val snapshot = appUtils.db.collection(USERS).get().await()
        val users = snapshot.toObjects(UserModel::class.java)

       /* val moshi: Moshi = Moshi.Builder().add(UserAdapter()).build()

        val adapter: JsonAdapter<UserModel> = moshi.adapter(UserModel::class.java)
        Log.d("errorIhab", snapshot.toString())
        val user = adapter.fromJson(snapshot.toString());

        var list = ArrayList<UserModel>()
        list.add(user!!)*/
        emit(Resource.success(users))

    }.catch {
        Log.d("errorIhab", it.message.toString())
        emit(Resource.error(it.message.toString(), null))
    }.flowOn(Dispatchers.IO)

    override suspend fun addUser(userModel: UserModel) = flow<Boolean> {

        emit(false)

        val userRef = appUtils.db.collection(USERS).add(userModel)
        emit(true)

    }.catch {
        emit(false)
    }.flowOn(Dispatchers.IO)

    override suspend fun uploadImage(filePath: Uri?): Flow<String> {

        return flow<String> {

                val ref = appUtils.storageReference?.child( USERS_IMAGES_REFERENCE + UUID.randomUUID().toString())
                ref
                    .putFile(filePath!!)
                    .await() // await() instead of snapshot
                    .storage
                    .downloadUrl
                    .await() // await the url
                    .toString()

        }.catch {
            emit("")
        }.flowOn(Dispatchers.IO)
    }
}