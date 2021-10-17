package com.wajeez.sample.view.interfaces

import com.wajeez.sample.model.utils.FilterUsersType
import com.wajeez.sample.view.interfaces.BaseView

interface UsersMainView: BaseView {

    fun getUsers()

    fun searchUsers(text: String)

    fun filterUsers(filterUsersType: FilterUsersType)

    fun showFilterDialogSheet()
}