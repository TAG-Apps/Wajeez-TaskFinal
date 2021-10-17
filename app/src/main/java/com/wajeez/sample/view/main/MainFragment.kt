package  com.wajeez.sample.view.main

import android.os.Bundle
import android.view.*
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.FirebaseApp
import com.wajeez.sample.R
import com.wajeez.sample.databinding.FragmentMainBinding
import com.wajeez.sample.model.data.UserModel
import com.wajeez.sample.model.utils.FilterUsersType
import com.wajeez.sample.view.interfaces.OnActionItemClicked
import com.wajeez.sample.model.utils.Status
import com.wajeez.sample.view.fragment_factory.ParentFragment
import com.wajeez.sample.view.interfaces.UsersMainView
import com.wajeez.sample.view.main.dialogs.FilterBottomSheet
import com.wajeez.sample.viewmodel.MainFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainFragment : ParentFragment(R.layout.fragment_main), OnActionItemClicked, UsersMainView {

    private val TAG = "MainFragment"

    private val mBinding: FragmentMainBinding by viewBinding()
    private val viewModel: MainFragmentViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private lateinit var mUsersAdapter: MainAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        mBinding.mainViewModel = viewModel
        mBinding.callbackMain = this
        viewModel.setNavigator(this)

        setupUI()
        setupObserver()
    }

    private fun setupUI() {


        mUsersAdapter = MainAdapter()

        mBinding.mainRecycle.setHasFixedSize(true)
        mBinding.mainRecycle.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        mBinding.mainRecycle.adapter = mUsersAdapter

        context?.let { FirebaseApp.initializeApp(it) }
    }

    private fun setupObserver() {
        getUsers()
    }

    override fun getUsers() {

        viewModel.users.observe(requireActivity(), Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    //hideLoading()
                    it.data?.let { users -> mUsersAdapter.setUsersData(users as ArrayList<UserModel>) }
                    mBinding.mainRecycle.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    //showLoading()
                    mBinding.mainRecycle.visibility = View.GONE
                }
                Status.ERROR -> {
                    //hideLoading()
                    showAlert(it.message)
                }
            }
        })
    }

    fun search(text: CharSequence, start: Int, before: Int, count: Int){
        searchUsers(text.toString())
    }

    override fun searchUsers(text: String){

        lifecycleScope.launchWhenStarted {
            viewModel.searchUsers(text).observe(requireActivity(), {
                    mUsersAdapter.setUsersData(it.data as ArrayList<UserModel>)
            })
        }

       /* viewModel.users.observe(requireActivity(), { it ->

            mUsersAdapter.setUsersData(it.data?.filter { it.name!!.contains(text.toString()) } as ArrayList<UserModel>)

        })*/
    }

    override fun filterUsers(filterUsersType: FilterUsersType){
        lifecycleScope.launchWhenStarted {
            viewModel.filterUsers(filterUsersType).observe(requireActivity(), {
                mUsersAdapter.setUsersData(it.data?.toList() as ArrayList<UserModel>)
            })
        }

        /*viewModel.users.observe(requireActivity(), { it ->

            when (filterUsersType) {

                FilterUsersType.WITH_IMAGE ->  mUsersAdapter.setUsersData(ArrayList(it.data?.sortedBy { it.profilePictureUrl!!.isEmpty()}) )
                FilterUsersType.WITHOUT_IMAGE ->  mUsersAdapter.setUsersData(ArrayList(it.data?.sortedBy { it.profilePictureUrl!!.isNotEmpty()}) )
                else -> {
                    mUsersAdapter.setUsersData(ArrayList(it.data?.sortedBy { it.profilePictureUrl!!.isEmpty()}) )
                }
            }
        })*/
    }

    override fun showLoading() {
        mBinding.waitingProgress.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        mBinding.waitingProgress.visibility = View.GONE
    }

    override fun showAlert(error: String?) {
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
    }

    override fun showFilterDialogSheet() {
        val sheet = FilterBottomSheet(this)
        sheet.show(parentFragmentManager, sheet.tag)
    }

    override fun onItemClicked(filterUsersType: FilterUsersType) {
        // filter
        filterUsers(filterUsersType)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_user -> findNavController().navigate(MainFragmentDirections.actionMainFragmentToCreateUserFragment())
        }

        return super.onOptionsItemSelected(item)
    }
}