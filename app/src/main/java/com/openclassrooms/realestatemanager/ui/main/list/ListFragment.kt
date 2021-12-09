package com.openclassrooms.realestatemanager.ui.main.list

import android.os.Bundle

import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.main.detail.DetailFragment
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
            true -> R.id.frameLayout
            false -> R.id.fl_estate_detail_container
        }

        val recyclerView = binding.rvEstateList
        val adapter = ListAdapter {
            viewModel.setCurrentEstateId(it.id)
            displayMasterDetailScreen(mId)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.uiStateLiveData.observe(viewLifecycleOwner) {it->
            adapter.submitList(it)
        }
        viewModel.isSearching.observe(viewLifecycleOwner) {
            if (it) {
                binding.btEstateListClearSearch.apply {
                    visibility = View.VISIBLE
                    setOnClickListener { viewModel.clearSearch() }
                }
            } else {
                binding.btEstateListClearSearch.visibility = View.GONE
            }
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