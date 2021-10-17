package  com.wajeez.sample.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.wajeez.sample.model.data.UserModel
import com.wajeez.sample.model.repository.UserRepository
import com.wajeez.sample.model.utils.FilterUsersType
import com.wajeez.sample.model.utils.NetworkHelper
import com.wajeez.sample.model.utils.Resource
import com.wajeez.sample.view.interfaces.UsersMainView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<UsersMainView>() {

    private val _users = MutableLiveData<Resource<List<UserModel>>>()
    val users: LiveData<Resource<List<UserModel>>>
        get() = _users

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch(Dispatchers.IO) {

            _users.postValue(Resource.loading(null))
            mIsLoading.set(true);
            if (networkHelper.isNetworkConnected()) {

                userRepository.getUsers().collect {
                    mIsLoading.set(false);
                    _users.postValue(it)
                }

            } else _users.postValue(Resource.error("No internet connection", null))
        }
    }

    fun filterUsers(filterUsersType: FilterUsersType): LiveData<Resource<List<UserModel>>>{
        val usersFilter = MutableLiveData<Resource<List<UserModel>>>()

        viewModelScope.launch(Dispatchers.Default) {

            users.value.let {
                when (filterUsersType) {

                    FilterUsersType.WITH_IMAGE ->  usersFilter.postValue(Resource.success(it?.data?.sortedBy { it.profilePictureUrl!!.isEmpty()}))
                    FilterUsersType.WITHOUT_IMAGE ->  usersFilter.postValue(Resource.success(it?.data?.sortedBy { it.profilePictureUrl!!.isEmpty()}))
                    else -> {
                        usersFilter.postValue(Resource.success(it?.data?.sortedBy { it.profilePictureUrl!!.isEmpty()}))
                    }
                }
            }
        }

        return usersFilter
    }

    fun searchUsers(text: String): LiveData<Resource<List<UserModel>>>{
        val usersFilter = MutableLiveData<Resource<List<UserModel>>>()

        viewModelScope.launch(Dispatchers.Default) {

            users.value.let {
                usersFilter.postValue(Resource.success(it?.data?.filter { it.name!!.contains(text.toString()) }))
            }
        }

        return usersFilter
    }
}