package com.openclassrooms.realestatemanager.activity.main.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.activity.main.detail.DetailFragment
import com.openclassrooms.realestatemanager.databinding.EstateListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private lateinit var binding: EstateListBinding
    private val viewModel by viewModels<ListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EstateListBinding.inflate(inflater, container, false)


        val mId = when (resources.getBoolean(R.bool.portrait_only)) {
            true -> R.id.cl_estate_list
            false -> R.id.fl_estate_detail_container
        }

        val recyclerView = binding.rvEstateList
        val adapter = ListAdapter {
            viewModel.isCurrentEstate(it.id)
            displayMasterDetailScreen(mId)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.uiStateLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.apply {
            btEstateListClearSearch.visibility = View.VISIBLE
            btEstateListClearSearch.setOnClickListener { viewModel.clearSearch() }
        }

        return binding.root
    }

    private fun displayMasterDetailScreen(id: Int) {
        parentFragmentManager.commit {
            replace<DetailFragment>(id)
            setReorderingAllowed(true)
            addToBackStack("detail")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.main_menu_edit).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }
}