package  com.wajeez.sample.viewmodel

import android.net.Uri
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import com.wajeez.sample.model.data.UserModel
import com.wajeez.sample.model.repository.UserRepository
import com.wajeez.sample.model.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateUserFragmentViewModel @Inject constructor(
    private val appUtils: AppUtils,
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {


    fun checkViews(
        id: String,
        name: EditText,
        profilePictureUrl: String
    ): LiveData<Boolean>? {

        if (!appUtils.checkViews(name)) {
            return null
        }

        val userData =
            UserModel(
                id = id,
                name = name.text.toString(),
                profilePictureUrl = profilePictureUrl,
            )

        return addUser(userData)

    }

     fun uploadImage(filePath: Uri?): LiveData<String> {

        val mUploadeImageLiveData = MutableLiveData<String>()

         viewModelScope.launch(Dispatchers.IO) {
             if (networkHelper.isNetworkConnected()) {

                 if(filePath != null){
                     userRepository.uploadImage(filePath).collect {
                         mUploadeImageLiveData.postValue(it)
                     }

                 }else{
                     mUploadeImageLiveData.postValue("")
                     //Toast.makeText(context, "Please Upload an Image", Toast.LENGTH_SHORT).show()
                 }

             } else mUploadeImageLiveData.postValue("")

         }

        return mUploadeImageLiveData
    }

    private fun addUser(userModel: UserModel): LiveData<Boolean> {

        val mUserLiveData = MutableLiveData<Boolean>()

        viewModelScope.launch(Dispatchers.IO) {

            if (networkHelper.isNetworkConnected()) {

                if (appUtils.loading.value == true) return@launch

                appUtils.loading.postValue(true)

                userRepository.addUser(userModel).collect {
                    mUserLiveData.postValue(it)
                }

            } else mUserLiveData.postValue(false)
        }

        return mUserLiveData
    }
}