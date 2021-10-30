package com.openclassrooms.realestatemanager.activity.main.querysearch

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentQuerySearchBinding
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class QuerySearchFragment : Fragment() {

    lateinit var binding: FragmentQuerySearchBinding
    private val viewModel by viewModels<QuerySearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentQuerySearchBinding.inflate(inflater, container, false)

        binding.apply {
            cbSearchOnSale.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onSale = isChecked
            }
            cbSearchSold.setOnCheckedChangeListener { _, isChecked ->
                viewModel.sold = isChecked
                btSearchSoldDate.visibility = if (isChecked) View.VISIBLE else View.GONE
            }

            btSearchType.setOnClickListener {
                spSearchType.visibility =
                    if (spSearchType.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            spSearchType.apply {
                adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(R.array.type)
                )
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.type = parent?.getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
            }

            btSearchPrice.setOnClickListener {
                clSearchPrice.visibility =
                    if (clSearchPrice.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchPriceMin.addTextChangedListener {
                viewModel.priceMin = (it.toString().toIntOrNull())
            }
            etSearchPriceMax.addTextChangedListener {
                viewModel.priceMax = (it.toString().toIntOrNull())
            }

            btSearchSurface.setOnClickListener {
                clSearchSurface.visibility =
                    if (clSearchSurface.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchSurfaceMin.addTextChangedListener {
                viewModel.surfaceMin = (it.toString().toDoubleOrNull())
            }
            etSearchSurfaceMax.addTextChangedListener {
                viewModel.surfaceMax = (it.toString().toDoubleOrNull())
            }

            btSearchRoom.setOnClickListener {
                clSearchRoom.visibility =
                    if (clSearchRoom.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchRoomMin.addTextChangedListener {
                viewModel.roomMin = (it.toString().toIntOrNull())
            }
            etSearchRoomMax.addTextChangedListener {
                viewModel.roomMax = (it.toString().toIntOrNull())
            }

            btSearchBedroom.setOnClickListener {
                clSearchBedroom.visibility =
                    if (clSearchBedroom.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchBedroomMin.addTextChangedListener {
                viewModel.bedroomMin = (it.toString().toIntOrNull())
            }
            etSearchBedroomMax.addTextChangedListener {
                viewModel.bedroomMax = (it.toString().toIntOrNull())
            }

            btSearchBathroom.setOnClickListener {
                clSearchBathroom.visibility =
                    if (clSearchBathroom.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchBathroomMin.addTextChangedListener {
                viewModel.bathroomMin = it.toString().toIntOrNull()
            }
            etSearchBathroomMax.addTextChangedListener {
                viewModel.bathroomMax = it.toString().toIntOrNull()
            }

            btSearchLandsize.setOnClickListener {
                clSearchLandsize.visibility =
                    if (clSearchLandsize.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchLandsizeMin.addTextChangedListener {
                viewModel.landSizeMin = it.toString().toDoubleOrNull()
            }
            etSearchLandsizeMax.addTextChangedListener {
                viewModel.landSizeMax = it.toString().toDoubleOrNull()
            }

            btSearchInterest.setOnClickListener {
                tlSearchInterest.visibility =
                    if (tlSearchInterest.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            cbSearchSchool.setOnCheckedChangeListener { _, isChecked ->
                viewModel.school = isChecked
            }
            cbSearchStore.setOnCheckedChangeListener { _, isChecked -> viewModel.store = isChecked }
            cbSearchPark.setOnCheckedChangeListener { _, isChecked -> viewModel.park = isChecked }
            cbSearchRestaurant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.restaurant = isChecked
            }
            cbSearchMovie.setOnCheckedChangeListener { _, isChecked -> viewModel.movie = isChecked }
            cbSearchTheatre.setOnCheckedChangeListener { _, isChecked ->
                viewModel.theatre = isChecked
            }
            cbSearchSubway.setOnCheckedChangeListener { _, isChecked ->
                viewModel.subway = isChecked
            }
            cbSearchNightlife.setOnCheckedChangeListener { _, isChecked ->
                viewModel.nightlife = isChecked
            }


            btSearchEntryDate.setOnClickListener {
                clSearchEntryDate.visibility =
                    if (clSearchEntryDate.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            rgSearchEntry.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.rb_search_entry_before -> viewModel.entryDateCode = 0
//                    R.id.rb_search_entry_at -> viewModel.entryDateCode = 1
                    R.id.rb_search_entry_after -> viewModel.entryDateCode = 1
                }
            }
            dpSearchEntryDate.init(2021,0,1) { _, years, month, day ->
                viewModel.entryDateText = Utils.getLongFormatDate(years, month+1, day)
            }

            btSearchSoldDate.setOnClickListener {
                clSearchSoldDate.visibility =
                    if (clSearchSoldDate.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            rgSearchSold.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.rb_search_sold_before -> viewModel.soldDateCode = 0
//                    R.id.rb_search_sold_at -> viewModel.soldDateCode = 1
                    R.id.rb_search_sold_after -> viewModel.soldDateCode = 1
                }
            }
            dpSearchDateSold.init(2022,8,1){
                _,years,month, day->
                viewModel.soldDateText = Utils.getLongFormatDate(years, month+1, day)
            }


            btSearchPhoto.setOnClickListener {
                etSearchPhotoMin.visibility =
                    if (etSearchPhotoMin.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchPhotoMin.addTextChangedListener {
                viewModel.photoMin = it.toString().toIntOrNull()
            }

            btSearchSearch.setOnClickListener {
                viewModel.search()
            }
        }


        return binding.root
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