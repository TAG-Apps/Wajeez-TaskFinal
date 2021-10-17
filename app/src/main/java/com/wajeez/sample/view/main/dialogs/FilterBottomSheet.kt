package com.wajeez.sample.view.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wajeez.sample.R
import com.wajeez.sample.databinding.ItemFilterSheetBinding
import com.wajeez.sample.model.utils.FilterUsersType
import com.wajeez.sample.view.interfaces.OnActionItemClicked

class FilterBottomSheet constructor(
    private val listener: OnActionItemClicked
) : BottomSheetDialogFragment() {

    private lateinit var mBinding: ItemFilterSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = ItemFilterSheetBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.button.setOnClickListener(){

           if(mBinding.radioGroup.checkedRadioButtonId == R.id.withImageRadioButton) {
               listener.onItemClicked(FilterUsersType.WITH_IMAGE)
            }else{
               listener.onItemClicked(FilterUsersType.WITHOUT_IMAGE)
           }
            dismiss()
        }
    }
}