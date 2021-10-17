package com.wajeez.sample.model.api

import android.net.Uri
import com.wajeez.sample.model.data.UserModel
import com.wajeez.sample.model.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ApiHelper {

    suspend fun getUsers(): Flow<Resource<List<UserModel>>>

    suspend fun addUser(userData: UserModel): Flow<Boolean>

    suspend fun uploadImage(filePath: Uri?): Flow<String>
}