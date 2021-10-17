package  com.wajeez.sample.view.interfaces

import com.wajeez.sample.model.utils.FilterUsersType

interface OnActionItemClicked {
    fun onItemClicked(filterUsersType: FilterUsersType)
}