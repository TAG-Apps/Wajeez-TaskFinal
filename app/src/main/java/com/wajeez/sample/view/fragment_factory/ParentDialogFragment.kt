package  com.wajeez.sample.view.fragment_factory

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.wajeez.sample.model.utils.AppUtils
import com.wajeez.sample.view.interfaces.BaseView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class ParentDialogFragment
constructor(layoutRest: Int) :
    DialogFragment(layoutRest) {

    private lateinit var listener: BaseView

    @Inject
    lateinit var appUtils: AppUtils

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as BaseView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appUtils.loading.observe(viewLifecycleOwner, {
            if (it)
                listener.showLoading()
            else
                listener.hideLoading()

        })
    }

    override fun onPause() {
        super.onPause()
        listener.hideLoading()
    }
}

