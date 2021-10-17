package com.wajeez.sample.model.repository

import android.net.Uri
import com.wajeez.sample.model.api.ApiHelper
import com.wajeez.sample.model.data.UserModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiHelper: ApiHelper
)

{
    suspend fun getUsers() = apiHelper.getUsers()

    suspend fun addUser(userModel: UserModel) = apiHelper.addUser(userModel)

    suspend fun uploadImage(filePath: Uri?) = apiHelper.uploadImage(filePath)
}